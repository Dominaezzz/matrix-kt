package io.github.matrixkt.clientserver.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class RoomPreset {
    @SerialName("private_chat")
    PRIVATE_CHAT,

    @SerialName("public_chat")
    PUBLIC_CHAT,

    @SerialName("trusted_private_chat")
    TRUSTED_PRIVATE_CHAT;
}
