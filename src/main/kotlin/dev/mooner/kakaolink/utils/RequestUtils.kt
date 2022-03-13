/*
 * RequestUtils.kt created by Minki Moon(mooner1022) on 22. 2. 20. 오전 2:15
 * Copyright (c) mooner1022. all rights reserved.
 * This code is licensed under the GNU General Public License v3.0.
 */

package dev.mooner.kakaolink.utils

import it.skrape.fetcher.Scraper
import it.skrape.fetcher.response
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

suspend inline fun <reified T> Scraper<*>.deserialize(json: Json = Json) =
    with(T::class) {
        response {
            json.decodeFromString<T>(responseBody)
        }
    }