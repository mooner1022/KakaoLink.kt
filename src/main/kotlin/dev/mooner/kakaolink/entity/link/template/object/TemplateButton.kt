/*
 * TemplateButton.kt created by Minki Moon(mooner1022) on 22. 2. 22. 오후 1:32
 * Copyright (c) mooner1022. all rights reserved.
 * This code is licensed under the GNU General Public License v3.0.
 */

package dev.mooner.kakaolink.entity.link.template.`object`

import kotlin.properties.Delegates.notNull

@kotlinx.serialization.Serializable
data class TemplateButton(
    val title: String,
    val link: TemplateLink
)

class TemplateButtonBuilder {

    var title: String by notNull()

    private var link: TemplateLink = TemplateLink()

    fun link(builder: TemplateLink.() -> Unit) = link.apply(builder)

    internal fun build() = TemplateButton(title, link)
}

class TemplateButtonListBuilder {

    private val buttons: MutableList<TemplateButton> = arrayListOf()

    fun button(builder: TemplateButtonBuilder.() -> Unit) {
        buttons += TemplateButtonBuilder().apply(builder).build()
    }

    internal fun build() = buttons
}