package io.github.matrixkt.models.search

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class GroupKey {
    @SerialName("room_id")
    ROOM_ID,

    @SerialName("sender")
    SENDER;
}
