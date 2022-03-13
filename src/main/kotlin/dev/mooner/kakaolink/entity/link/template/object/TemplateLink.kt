/*
 * TemplateLink.kt created by Minki Moon(mooner1022) on 22. 2. 22. 오후 1:16
 * Copyright (c) mooner1022. all rights reserved.
 * This code is licensed under the GNU General Public License v3.0.
 */

package dev.mooner.kakaolink.entity.link.template.`object`

import kotlinx.serialization.SerialName
import kotlin.properties.Delegates.notNull

@kotlinx.serialization.Serializable
class TemplateLink {

    @SerialName("web_url")
    var webUrl: String by notNull()

    @SerialName("mobile_web_url")
    var mobileWebUrl: String by notNull()
}