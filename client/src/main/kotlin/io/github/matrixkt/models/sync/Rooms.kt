package io.github.matrixkt.models.sync

import kotlinx.serialization.Serializable

@Serializable
public data class Rooms(
    /**
     * The rooms that the user has joined.
     */
    val join: Map<String, JoinedRoom> = emptyMap(),

    /**
     * The rooms that the user has been invited to.
     */
    val invite: Map<String, InvitedRoom> = emptyMap(),

    /**
     * The rooms that the user has left or been banned from.
     */
    val leave: Map<String, LeftRoom> = emptyMap(),

    /**
     * The rooms that the user has knocked upon, mapped as room ID to room information.
     */
    val knock: Map<String, KnockedRoom>? = emptyMap()
)
