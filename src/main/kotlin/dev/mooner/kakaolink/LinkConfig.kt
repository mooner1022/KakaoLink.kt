/*
 * LinkConfig.kt created by Minki Moon(mooner1022) on 22. 2. 19. 오후 8:58
 * Copyright (c) mooner1022. all rights reserved.
 * This code is licensed under the GNU General Public License v3.0.
 */

package dev.mooner.kakaolink

data class LinkConfig(
    val email: String,
    val password: String,
    val apiKey: String,
    val url: String
)