package io.github.matrixkt.models.events

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class GuestAccess {
    @SerialName("can_join")
    CAN_JOIN,

    @SerialName("forbidden")
    FORBIDDEN;
}
