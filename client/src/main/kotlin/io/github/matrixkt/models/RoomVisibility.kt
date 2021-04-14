package io.github.matrixkt.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class RoomVisibility {
    @SerialName("public")
    PUBLIC,
    @SerialName("private")
    PRIVATE;
}
