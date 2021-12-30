package io.github.matrixkt.clientserver.models.sync

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class LeftRoom(
    /**
     * The state updates for the room up to the start of the timeline.
     */
    val state: State? = null,

    /**
     * The timeline of messages and state changes in the room up to the point when the user left.
     */
    val timeline: Timeline? = null,

    /**
     * The private data that this user has attached to this room.
     */
    @SerialName("account_data")
    val accountData: AccountData? = null
)
