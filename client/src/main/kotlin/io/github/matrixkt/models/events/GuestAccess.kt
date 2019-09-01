package io.github.matrixkt.models.events

import kotlinx.serialization.Serializable
import kotlinx.serialization.internal.CommonEnumSerializer

@Serializable(GuestAccess.Companion::class)
enum class GuestAccess {
    CAN_JOIN, FORBIDDEN;

    companion object : CommonEnumSerializer<GuestAccess>(
        "GuestAccess",
        values(),
        values().map { it.name.toLowerCase() }.toTypedArray()
    )
}
