package io.github.matrixkt.clientserver.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class RoomVisibility {
    @SerialName("public")
    PUBLIC,
    @SerialName("private")
    PRIVATE;
}
