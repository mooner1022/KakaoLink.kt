/*
 * LinkConfigBuilder.kt created by Minki Moon(mooner1022) on 22. 2. 19. 오후 9:00
 * Copyright (c) mooner1022. all rights reserved.
 * This code is licensed under the GNU General Public License v3.0.
 */

package dev.mooner.kakaolink

import kotlin.properties.Delegates.notNull

class LinkConfigBuilder {

    var email: String by notNull()

    var password: String by notNull()

    var apiKey: String by notNull()

    var url: String by notNull()

    internal fun build() = LinkConfig(email, password, apiKey, url)
}