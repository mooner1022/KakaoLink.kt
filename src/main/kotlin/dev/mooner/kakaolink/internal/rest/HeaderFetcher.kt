/*
 * HeaderFetcher.kt created by Minki Moon(mooner1022) on 22. 2. 27. 오후 7:47
 * Copyright (c) mooner1022. all rights reserved.
 * This code is licensed under the GNU General Public License v3.0.
 */

package dev.mooner.kakaolink.internal.rest

import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.client.features.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.network.sockets.*
import io.ktor.util.*
import it.skrape.fetcher.*
import it.skrape.fetcher.Cookie
import kotlinx.coroutines.runBlocking

internal object HeaderFetcher: NonBlockingFetcher<Request> {

    override suspend fun fetch(request: Request): Result = configuredClient(request).toResult()

    override val requestBuilder: Request
        get() = Request()

    private suspend fun configuredClient(request: Request): HttpResponse =
        HttpClient(Apache) {
            expectSuccess = false
            followRedirects = request.followRedirects
            install(HttpTimeout)
            install(Logging) {
                level = LogLevel.NONE
            }
            HttpResponseValidator {
                handleResponseException { cause: Throwable ->
                    when (cause) {
                        is SocketTimeoutException -> {
                            throw cause
                        }
                    }
                }
            }
            engine {
                followRedirects = request.followRedirects
            }
        }.use {
            runBlocking { it.request(request.toHttpRequest()) }
        }
}

private fun Request.toHttpRequest(): HttpRequestBuilder {
    val request = this
    return HttpRequestBuilder().apply {
        method = request.method.toHttpMethod()
        url(request.url)
        headers {
            request.headers.forEach { (k, v) ->
                append(k, v)
            }
            append("User-Agent", request.userAgent)
            cookies = request.cookies
        }
        request.body?.run {
            body = this
        }
    }
}

private fun io.ktor.http.Cookie.toDomainCookie(origin: String): Cookie {
    val path = this.path ?: "/"
    val expires = this.expires?.toHttpDate().toExpires()
    val domain = when (val domainUrl = this.domain) {
        null -> Domain(origin, false)
        else -> Domain(domainUrl.urlOrigin, true)
    }
    val sameSite = this.extensions["SameSite"].toSameSite()
    val maxAge = this.maxAge.toMaxAge()

    return Cookie(this.name, this.value, expires, maxAge, domain, path, sameSite, this.secure, this.httpOnly)
}

private fun Int.toMaxAge(): Int? = when (this) {
    0 -> null
    else -> this
}

private fun String?.toExpires(): Expires {
    return when (this) {
        null -> Expires.Session
        else -> Expires.Date(this)
    }
}

private fun String?.toSameSite(): SameSite = when (this?.lowercase()) {
    "strict" -> SameSite.STRICT
    "lax" -> SameSite.LAX
    "none" -> SameSite.NONE
    else -> SameSite.LAX
}

private fun Method.toHttpMethod(): HttpMethod = when (this) {
    Method.GET -> HttpMethod.Get
    Method.POST -> HttpMethod.Post
    Method.HEAD -> HttpMethod.Head
    Method.DELETE -> HttpMethod.Delete
    Method.PATCH -> HttpMethod.Patch
    Method.PUT -> HttpMethod.Put
}

private fun HttpResponse.toResult(): Result =
    Result(
        responseBody = "",
        responseStatus = toStatus(),
        contentType = contentType()?.toString()?.replace(" ", ""),
        headers = headers.flattenEntries()
            .associateBy({ item -> item.first }, { item -> headers[item.first]!! }),
        cookies = setCookie().map { cookie -> cookie.toDomainCookie(request.url.toString().urlOrigin) },
        baseUri = request.url.toString()
    )

private fun HttpResponse.toStatus() = Result.Status(this.status.value, this.status.description)