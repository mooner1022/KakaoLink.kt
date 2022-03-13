/*
 * ReceiveData.kt created by Minki Moon(mooner1022) on 22. 2. 27. 오후 7:47
 * Copyright (c) mooner1022. all rights reserved.
 * This code is licensed under the GNU General Public License v3.0.
 */

package dev.mooner.kakaolink.internal.rest.data

internal sealed class ReceiveData {

    @kotlinx.serialization.Serializable
    data class Auth(
        val status: Int,
        val message: String? = null
    )

    @kotlinx.serialization.Serializable
    data class RoomData(
        val chats: List<ChatRoom>,
        val securityKey: String
    ) {

        @kotlinx.serialization.Serializable
        data class ChatRoom(
            val id: String,
            val title: String,
            val memberCount: Int,
            val profileImageURLs: List<String>
        )
    }
}