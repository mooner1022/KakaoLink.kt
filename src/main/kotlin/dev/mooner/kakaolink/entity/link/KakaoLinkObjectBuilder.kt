/*
 * KakaoLinkObjectBuilder.kt created by Minki Moon(mooner1022) on 22. 2. 20. 오후 1:59
 * Copyright (c) mooner1022. all rights reserved.
 * This code is licensed under the GNU General Public License v3.0.
 */

package dev.mooner.kakaolink.entity.link

import dev.mooner.kakaolink.entity.link.template.`object`.TemplateObject
import dev.mooner.kakaolink.entity.link.template.`object`.TemplateObjectBuilder
import dev.mooner.kakaolink.entity.link.template.args.TemplateArgsBuilder
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
class KakaoLinkObjectBuilder {

    @SerialName("link_ver")
    var linkVer: String = "4.0"

    @SerialName("template_id")
    var templateId: Int? = null

    @SerialName("template_args")
    private var templateArgs: Map<String, String>? = null

    fun templateArgs(builder: TemplateArgsBuilder.() -> Unit) {
        templateArgs = TemplateArgsBuilder().apply(builder).build()
    }

    @SerialName("template_object")
    private var templateObject: TemplateObject? = null

    fun templateObject(builder: TemplateObjectBuilder.() -> Unit) {
        templateObject = TemplateObjectBuilder().apply(builder).build()
    }
}