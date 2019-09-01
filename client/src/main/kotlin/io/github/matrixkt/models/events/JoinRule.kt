package io.github.matrixkt.models.events

import kotlinx.serialization.Serializable
import kotlinx.serialization.internal.CommonEnumSerializer

@Serializable(JoinRule.Companion::class)
enum class JoinRule {
    PUBLIC, KNOCK, INVITE, PRIVATE;

    companion object : CommonEnumSerializer<JoinRule>(
        "JoinRule",
        values(),
        values().map { it.name.toLowerCase() }.toTypedArray()
    )
}
