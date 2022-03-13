/*
 * TiaraData.kt created by Minki Moon(mooner1022) on 22. 2. 19. 오후 9:22
 * Copyright (c) mooner1022. all rights reserved.
 * This code is licensed under the GNU General Public License v3.0.
 */

package dev.mooner.kakaolink.entity.tiara

typealias StrMap = Map<String, String>

@kotlinx.serialization.Serializable
internal data class TiaraUrlData(
    val sdk: StrMap = mapOf(
        "type" to "WEB",
        "version" to "1.1.17"
    ),
    val env: StrMap = mapOf(
        "screen" to "1920X1080",
        "tz" to "+9",
        "cke" to "Y"
    ),
    val common: StrMap = mapOf(
        "svcdomain" to "accounts.kakao.com",
        "deployment" to "production",
        "url" to "https://accounts.kakao.com/login",
        "referrer" to "https://m.search.daum.net/",
        "title" to "카카오계정",
        "section" to "login",
        "page" to "pageLogin"
    ),
    val action: StrMap = mapOf(
        "type" to "Pageview",
        "name" to "pageLogin",
        "kind" to ""
    )
)