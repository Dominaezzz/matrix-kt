package io.github.matrixkt.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class RoomVisibility {
    @SerialName("public")
    PUBLIC,
    @SerialName("private")
    PRIVATE;
}
