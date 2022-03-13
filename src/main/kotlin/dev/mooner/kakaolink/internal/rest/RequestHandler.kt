/*
 * RequestHandler.kt created by Minki Moon(mooner1022) on 22. 2. 27. 오후 7:47
 * Copyright (c) mooner1022. all rights reserved.
 * This code is licensed under the GNU General Public License v3.0.
 */

package dev.mooner.kakaolink.internal.rest

import it.skrape.fetcher.AsyncFetcher
import it.skrape.fetcher.Request
import it.skrape.fetcher.skrape
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

internal class RequestHandler(
    private val ua: String
) {

    private suspend fun getClient() = skrape(AsyncFetcher) {
        request {
            userAgent = ua
            followRedirects = true
        }
    }

    @OptIn(ExperimentalContracts::class)
    suspend fun request(request: Request.() -> Unit): it.skrape.fetcher.Result {
        contract {
            callsInPlace(request, InvocationKind.EXACTLY_ONCE)
        }

        return getClient().apply {
            request(request)
        }.scrape()
    }

    suspend fun request(request: Requests) = request.configure(getClient()).scrape()
}