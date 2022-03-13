/*
 * RequestData.kt created by Minki Moon(mooner1022) on 22. 2. 20. 오전 1:02
 * Copyright (c) mooner1022. all rights reserved.
 * This code is licensed under the GNU General Public License v3.0.
 */

package dev.mooner.kakaolink.internal.rest.data

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
internal sealed class RequestData {

    @Suppress("UNUSED")
    @kotlinx.serialization.Serializable
    data class AuthRequest(
        val email: String,
        val password: String,
        val `continue`: String,
        @SerialName("authenticity_token")
        val authToken: String
    ) {
        val os = "web"
        @SerialName("webview_v")
        val webviewV = "2"
        @SerialName("stay_signed_in")
        val staySignedIn = "true"
        val third = "false"
        val k = "true"
    }

    @kotlinx.serialization.Serializable
    data class SendLinkLoginRequest(
        @SerialName("app_key")
        val appKey: String,
        @SerialName("validation_action")
        val validationAction: String,
        @SerialName("validation_params")
        val validationParams: String,
        @SerialName("ka")
        val kakaoAgent: String
    )

    @kotlinx.serialization.Serializable
    data class SendLinkRequest(
        val validatedTalkLink: String
    )
}
