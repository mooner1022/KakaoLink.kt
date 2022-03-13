/*
 * TemplateContent.kt created by Minki Moon(mooner1022) on 22. 2. 22. 오후 1:32
 * Copyright (c) mooner1022. all rights reserved.
 * This code is licensed under the GNU General Public License v3.0.
 */

package dev.mooner.kakaolink.entity.link.template.`object`

import kotlinx.serialization.SerialName
import kotlin.properties.Delegates.notNull

@kotlinx.serialization.Serializable
data class TemplateContent(
    val title: String,
    @SerialName("image_url")
    val imageUrl: String,
    val link: TemplateLink,
    val description: String
)

class TemplateContentBuilder {

    var title: String by notNull()

    var imageUrl: String by notNull()

    private var link: TemplateLink = TemplateLink()

    fun link(builder: TemplateLink.() -> Unit) = link.apply(builder)

    var description: String by notNull()

    internal fun build() = TemplateContent(title, imageUrl, link, description)
}