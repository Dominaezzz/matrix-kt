package io.github.matrixkt.models.events

import kotlinx.serialization.Serializable
import kotlinx.serialization.internal.CommonEnumSerializer

@Serializable(Membership.Companion::class)
enum class Membership {
    INVITE, JOIN, KNOCK, LEAVE, BAN;

    companion object : CommonEnumSerializer<Membership>(
        "Membership",
        values(),
        values().map { it.name.toLowerCase() }.toTypedArray()
    )
}
