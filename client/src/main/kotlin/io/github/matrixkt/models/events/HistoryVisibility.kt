package io.github.matrixkt.models.events

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class HistoryVisibility {
    @SerialName("invited")
	INVITED,

    @SerialName("joined")
	JOINED,

    @SerialName("shared")
	SHARED,

    @SerialName("world_readable")
	WORLD_READABLE;
}
