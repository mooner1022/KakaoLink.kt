/*
 * TemplateObject.kt created by Minki Moon(mooner1022) on 22. 2. 22. 오후 1:32
 * Copyright (c) mooner1022. all rights reserved.
 * This code is licensed under the GNU General Public License v3.0.
 */

package dev.mooner.kakaolink.entity.link.template.`object`

import kotlinx.serialization.SerialName
import kotlin.properties.Delegates.notNull

@kotlinx.serialization.Serializable
data class TemplateObject(
    @SerialName("object_type")
    val objectType: String,
    @SerialName("button_title")
    val buttonTitle: String?,
    val content: TemplateContent,
    val buttons: List<TemplateButton> = listOf()
)

class TemplateObjectBuilder {

    var objectType: ObjectType by notNull()

    var buttonTitle: String? = null

    private var content: TemplateContent by notNull()

    fun content(builder: TemplateContentBuilder.() -> Unit) {
        content = TemplateContentBuilder().apply(builder).build()
    }

    private var buttons: MutableList<TemplateButton> = arrayListOf()

    fun buttons(builder: TemplateButtonListBuilder.() -> Unit) {
        buttons = TemplateButtonListBuilder().apply(builder).build()
    }

    fun build() = TemplateObject(
        buttonTitle = buttonTitle!!,
        objectType = objectType.type,
        content = content,
        buttons = buttons
    )
}

sealed class ObjectType {

    abstract val type: String

    object Feed: ObjectType() {
        override val type = "feed"
    }
    object List: ObjectType() {
        override val type = "list"
    }
    object Location: ObjectType() {
        override val type = "location"
    }
    object Commerce: ObjectType() {
        override val type = "commerce"
    }
    object Text: ObjectType() {
        override val type = "text"
    }
}