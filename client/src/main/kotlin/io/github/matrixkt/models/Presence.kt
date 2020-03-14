package io.github.matrixkt.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Presence {
    @SerialName("offline")
    OFFLINE,
    @SerialName("online")
    ONLINE,
    @SerialName("unavailable")
    UNAVAILABLE;
}
