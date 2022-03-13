/*
 * Requests.kt created by Minki Moon(mooner1022) on 22. 2. 27. 오후 7:47
 * Copyright (c) mooner1022. all rights reserved.
 * This code is licensed under the GNU General Public License v3.0.
 */

package dev.mooner.kakaolink.internal.rest

import dev.mooner.kakaolink.entity.link.KakaoLinkObjectBuilder
import dev.mooner.kakaolink.internal.Url
import dev.mooner.kakaolink.internal.rest.data.RequestData
import io.ktor.client.statement.*
import io.ktor.http.*
import it.skrape.fetcher.Method
import it.skrape.fetcher.Request
import it.skrape.fetcher.Scraper
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.properties.Delegates.notNull

typealias ReqScraper = Scraper<Request>

internal sealed class Requests {

    abstract fun configure(client: ReqScraper): ReqScraper

    object Account: Requests() {
        override fun configure(client: ReqScraper) =
            client.apply {
                request {
                    method = Method.GET
                    url = Url.Accounts
                    headers = mapOf(HttpHeaders.Referrer to "https://accounts.kakao.com/")
                }
            }
    }

    object Tiara: Requests() {
        override fun configure(client: ReqScraper) =
            client.apply {
                request {
                    method = Method.GET
                    url = Url.Tiara
                }
            }
    }

    class LinkRes(
        private val _cookies: Map<String, String>,
        apiKey: String,
        type: String = "custom",
        params: KakaoLinkObjectBuilder,
        agent: String
    ): Requests() {
        private var requestData: String by notNull()

        override fun configure(client: ReqScraper) =
            client.apply {
                request {
                    method = Method.POST
                    url = Url.Login
                    cookies = _cookies
                    body = requestData
                }
            }

        init {
            val encodedParams = Json.encodeToString(params)
            requestData = Json.encodeToString(
                RequestData.SendLinkLoginRequest(
                    appKey = apiKey,
                    validationAction = type,
                    validationParams = encodedParams,
                    kakaoAgent = agent
                )
            )
        }
    }

    class RoomData(
        private val _cookies: Map<String, String>,
        private val csrfToken: String,
        private val apiKey: String
    ): Requests() {

        override fun configure(client: ReqScraper) =
            client.apply {
                request {
                    method = Method.GET
                    url = Url.ChatData
                    cookies = _cookies
                    headers = mapOf(
                        HttpHeaders.Referrer to Url.Login,
                        "Csrf-Token" to csrfToken,
                        "App-Key" to apiKey
                    )
                }
            }
    }

    class SendLink(
        private val _cookies: Map<String, String>,
        private val csrfToken: String,
        private val apiKey: String,
        linkParams: String,
        securityKey: String,
        receiverIds: Array<String>,
        receiverChatRoomMemberCount: Array<Int>
    ): Requests() {

        override fun configure(client: ReqScraper) =
            client.apply {
                request {
                    method = Method.POST
                    url = Url.SendLink
                    cookies = _cookies
                    headers = mapOf(
                        HttpHeaders.Referrer to Url.Login,
                        "Csrf-Token" to csrfToken,
                        "App-Key" to apiKey,
                        "Content-Type" to "application/json;charset=UTF-8"
                    )
                }
            }
    }

    class Logout(
        private val _cookies: Map<String, String>
    ): Requests() {

        override fun configure(client: ReqScraper) =
            client.apply {
                request {
                    method = Method.GET
                    url = Url.Logout
                    cookies = _cookies
                    headers = mapOf(
                        HttpHeaders.Referrer to "https://accounts.kakao.com/",
                    )
                }
            }
    }
}
