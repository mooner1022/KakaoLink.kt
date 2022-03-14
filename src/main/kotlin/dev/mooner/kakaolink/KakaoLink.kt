/*
 * KakaoLink.kt created by Minki Moon(mooner1022) on 22. 2. 19. 오후 8:37
 * Copyright (c) mooner1022. all rights reserved.
 * This code is licensed under the GNU General Public License v3.0.
 */

package dev.mooner.kakaolink

import dev.mooner.kakaolink.cipher.AESCipher
import dev.mooner.kakaolink.entity.link.KakaoLinkObjectBuilder
import dev.mooner.kakaolink.internal.Url
import dev.mooner.kakaolink.internal.rest.HeaderFetcher
import dev.mooner.kakaolink.internal.rest.RequestHandler
import dev.mooner.kakaolink.internal.rest.Requests
import dev.mooner.kakaolink.internal.rest.data.ReceiveData
import dev.mooner.kakaolink.internal.rest.data.RequestData
import dev.mooner.kakaolink.utils.pairOf
import io.ktor.http.*
import it.skrape.core.document
import it.skrape.fetcher.Method
import it.skrape.fetcher.response
import it.skrape.fetcher.skrape
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URLDecoder
import java.net.URLEncoder
import kotlin.properties.Delegates.notNull

class KakaoLink(
    builder: LinkConfigBuilder.() -> Unit
) {

    private val requestHandler: RequestHandler

    private val config: LinkConfig

    private var kakaoAgent: String by notNull()

    init {
        config = LinkConfigBuilder().apply(builder).build()

        kakaoAgent = "sdk/1.25.7 os/javascript lang/ko-kr device/MacIntel origin/${URLEncoder.encode(config.url, Charsets.UTF_8)}"
        requestHandler = RequestHandler(kakaoAgent)
    }

    private var isLoggedIn: Boolean = false
    private var referrer: String = ""
    private val cookies: MutableMap<String, String> = hashMapOf()

    private val jsonSerializer = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    suspend fun login() {
        val response = requestHandler.request(Requests.Account)

        response.responseStatus.let {
            require(it.code == 200) { "Login failed with status code: ${it.code}" }
        }

        this.referrer = response.baseUri
        appendAllCookies(response.cookies)

        val (cryptoKey, authToken) = response.document.let {
            pairOf(
                it.findFirst("input[name=p]").attribute("value"),
                it.findFirst("head > meta:nth-child(3)").attribute("content")
            )
        }
        println(cryptoKey)
        println(authToken)

        skrape(HeaderFetcher) {
            request {
                userAgent = kakaoAgent
                followRedirects = true
            }
            apply(Requests.Tiara::configure)
            response {
                appendAllCookies(cookies)
            }
        }
        println(cookies)

        //val tiara = requestHandler.request(Requests.Tiara)
        //println(tiara.cookies)
        //appendAllCookies(tiara.cookies)

        val authCall = requestHandler.request {
            method = Method.POST
            url = Url.Auth
            headers = mapOf(HttpHeaders.Referrer to referrer)
            cookies = this@KakaoLink.cookies
            println(cookies)
            body {
                data = jsonSerializer.encodeToString(
                    RequestData.AuthRequest(
                        email = encrypt(config.email, cryptoKey),
                        password = encrypt(config.password, cryptoKey),
                        `continue` = URLDecoder.decode(referrer.split("=")[1], Charsets.UTF_8),
                        authToken = authToken
                    )
                )
                println(data)
                contentType = "application/json"
            }
        }

        val received = jsonSerializer.decodeFromString<ReceiveData.Auth>(authCall.responseBody)
        when(received.status) {
            0 -> null
            -484 -> "Encryption failure"
            -435 -> "The country you are trying to access is blocked"
            -450 -> "Email or password is incorrect"
            -473 -> "Login rate limit exceeded, please try later"
            -481 -> "CI result mismatch, message: ${received.message}"
            else -> "Unknown error with message: ${received.message}"
        }?.let { error("${received.status} $it") }

        isLoggedIn = true
    }

    @KakaoLinkDsl
    suspend fun sendLink(room: String, type: String, builder: KakaoLinkObjectBuilder.() -> Unit) {
        require(isLoggedIn) { "You can't access the KakaoLink API before logging in. Use login() first." }

        val obj = KakaoLinkObjectBuilder().apply(builder)

        val linkRequest = requestHandler.request(Requests.LinkRes(cookies, config.apiKey, type, obj, kakaoAgent))

        when(val statusCode = linkRequest.status { code }) {
            200 -> null
            401 -> "Please check the apiKey again"
            else -> "Unknown error with status code: $statusCode"
        }?.let { error(it) }

        println(linkRequest.responseBody)
        appendAllCookies(linkRequest.cookies)
        val (linkParams, csrfToken) = linkRequest.document.let {
            pairOf(
                it.findFirst("#validatedTalkLink").attribute("value"),
                it.findLast("div").attribute("ng-init").drop(6).replace("'", "")
            )
        }

        val roomData: ReceiveData.RoomData
        requestHandler.request(Requests.RoomData(cookies, csrfToken, config.apiKey)).let {
            roomData = jsonSerializer.decodeFromString(it.responseBody)
        }

        val mRoom = roomData.chats.find { it.title == room }
            ?: error("Unable to find room with name `$room`")

        println(linkParams)

        //requestHandler.request {  }
    }

    suspend fun logout() {
        if (!isLoggedIn) return
        val logoutRequest = requestHandler.request(Requests.Logout(cookies))

        val statusCode = logoutRequest.status { code }
        require(statusCode == 200) { "Logout failed with status: $statusCode" }

        cookies.clear()
    }

    private fun appendAllCookies(_cookies: List<it.skrape.fetcher.Cookie>) = cookies.putAll(_cookies.associate { it.name to it.value })

    private fun encrypt(message: String, passphrase: String) = AESCipher.encrypt(message, passphrase).toString()
}