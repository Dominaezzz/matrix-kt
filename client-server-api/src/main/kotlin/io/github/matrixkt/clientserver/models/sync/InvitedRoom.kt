package io.github.matrixkt.clientserver.models.sync

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class InvitedRoom(
    /**
     * The state of a room that the user has been invited to.
     * These state events may only have the `sender`, `type`, `state_key` and `content` keys present.
     * These events do not replace any state that the client already has for the room, for example if the client has archived the room.
     * Instead the client should keep two separate copies of the state: the one from the `invite_state` and one from the archived `state`.
     * If the client joins the room then the current state will be given as a delta against the archived `state` not the `invite_state`.
     */
    @SerialName("invite_state")
    val inviteState: InviteState? = null
)
