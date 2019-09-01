package io.github.matrixkt.models.events

import kotlinx.serialization.Serializable
import kotlinx.serialization.internal.CommonEnumSerializer

@Serializable(HistoryVisibility.Companion::class)
enum class HistoryVisibility {
    INVITED, JOINED, SHARED, WORLD_READABLE;

    companion object : CommonEnumSerializer<HistoryVisibility>(
        "HistoryVisibility",
        values(),
        values().map { it.name.toLowerCase() }.toTypedArray()
    )
}
