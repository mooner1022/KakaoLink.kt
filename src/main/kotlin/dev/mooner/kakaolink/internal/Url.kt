/*
 * Url.kt created by Minki Moon(mooner1022) on 22. 2. 19. 오후 9:17
 * Copyright (c) mooner1022. all rights reserved.
 * This code is licensed under the GNU General Public License v3.0.
 */

package dev.mooner.kakaolink.internal

import dev.mooner.kakaolink.entity.tiara.TiaraUrlData
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URLEncoder

internal object Url {

    const val Login = "https://sharer.kakao.com/talk/friends/picker/link"

    const val Logout = "https://sharer.kakao.com/talk/friends/picker/logout"

    val Tiara by lazy {
        val j = with(Json { encodeDefaults = true }) {
            "https://stat.tiara.kakao.com/track?d=" + URLEncoder.encode(encodeToString(TiaraUrlData()), Charsets.UTF_8)
        }
        println(j)
        j
    }

    const val Auth = "https://accounts.kakao.com/weblogin/authenticate.json"

    const val ChatData = "https://sharer.kakao.com/api/talk/chats"

    const val SendLink = "https://sharer.kakao.com/api/talk/message/link"

    const val Accounts = "https://accounts.kakao.com/login?continue=https%3A%2F%2Faccounts.kakao.com%2Fweblogin%2Faccount%2Finfo"
}