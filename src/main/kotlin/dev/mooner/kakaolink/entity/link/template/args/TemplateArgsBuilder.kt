/*
 * TemplateArgsBuilder.kt created by Minki Moon(mooner1022) on 22. 2. 22. 오후 1:32
 * Copyright (c) mooner1022. all rights reserved.
 * This code is licensed under the GNU General Public License v3.0.
 */

package dev.mooner.kakaolink.entity.link.template.args

class TemplateArgsBuilder {

    private val args: MutableMap<String, String> = hashMapOf()

    fun argument(key: String, value: String) {
        args[key] = value
    }

    fun argument(key: String, value: () -> String) = argument(key, value())

    internal fun build() = args
}