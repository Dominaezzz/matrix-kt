package io.github.matrixkt.clientserver.models.sync

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class RoomSummary(
    /**
     * The users which can be used to generate a room name if the room does not have one.
     * Required if the room's `m.room.name` or `m.room.canonical_alias` state events are unset or empty.
     *
     * This should be the first 5 members of the room, ordered by stream ordering, which are joined or invited.
     * The list must never include the client's own user ID.
     * When no joined or invited members are available, this should consist of the banned and left users.
     * More than 5 members may be provided, however less than 5 should only be provided when there are less than 5 members to represent.
     *
     * When lazy-loading room members is enabled, the membership events for the heroes MUST be included in the state, unless they are redundant.
     * When the list of users changes, the server notifies the client by sending a fresh list of heroes.
     * If there are no changes since the last sync, this field may be omitted.
     */
    @SerialName("m.heroes")
    val heroes: List<String>? = null,

    /**
     * The number of users with `membership` of join, including the client's own user ID.
     * If this field has not changed since the last sync, it may be omitted.
     * Required otherwise.
     */
    @SerialName("m.joined_member_count")
    val joinedMemberCount: Long? = null,

    /**
     * The number of users with `membership` of `invite`.
     * If this field has not changed since the last sync, it may be omitted.
     * Required otherwise.
     */
    @SerialName("m.invited_member_count")
    val invitedMemberCount: Long? = null
)
