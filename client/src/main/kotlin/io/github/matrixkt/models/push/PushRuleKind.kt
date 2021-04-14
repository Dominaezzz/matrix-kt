package io.github.matrixkt.models.push

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class PushRuleKind {
    @SerialName("override")
    OVERRIDE,
    @SerialName("underride")
    UNDERRIDE,
    @SerialName("sender")
    SENDER,
    @SerialName("room")
    ROOM,
    @SerialName("content")
    CONTENT;
}
