package io.github.matrixkt.events.contents.room

import io.github.matrixkt.events.StrippedState
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Adjusts the membership state for a user in a room.
 * It is preferable to use the membership APIs (`/rooms/<room id>/invite` etc) when performing membership
 * actions rather than adjusting the state directly as there are a restricted set of valid transformations.
 * For example, user A cannot force user B to join a room, and trying to force this state change directly will fail.
 *
 * The following membership states are specified:
 * - `invite` - The user has been invited to join a room, but has not yet joined it. They may not participate in the room until they join.
 * - `join` - The user has joined the room (possibly after accepting an invite), and may participate in it.
 * - `leave` - The user was once joined to the room, but has since left (possibly by choice, or possibly by being kicked).
 * - `ban` - The user has been banned from the room, and is no longer allowed to join it until they are un-banned from the room (by having their membership state set to a value other than ban).
 * - `knock` - This is a reserved word, which currently has no meaning.
 *
 * The `third_party_invite` property will be set if this invite is an `invite` event and is the successor of an `m.room.third_party_invite` event, and absent otherwise.
 *
 * This event may also include an `invite_room_state` key inside the event's `unsigned` data.
 * If present, this contains an array of `StrippedState` Events.
 * These events provide information on a subset of state events such as the room name.
 *
 * The user for which a membership applies is represented by the `state_key`.
 * Under some conditions, the `sender` and `state_key` may not match - this may
 * be interpreted as the `sender` affecting the membership state of the `state_key` user.
 *
 * The `membership` for a given user can change over time.
 * The table below represents the various changes over time and how clients and servers must interpret those changes.
 * Previous membership can be retrieved from the `prev_content` object on an event.
 * If not present, the user's previous membership must be assumed as `leave`.
 */
@SerialName("m.room.member")
@Serializable
public data class MemberContent(
    /**
     * The avatar URL for this user, if any. This is added by the homeserver.
     */
    @SerialName("avatar_url")
    val avatarUrl: String? = null,

    /**
     * The display name for this user, if any. This is added by the homeserver.
     */
    @SerialName("displayname")
    val displayName: String? = null,

    /**
     * The membership state of the user. One of: ["invite", "join", "knock", "leave", "ban"]
     */
    val membership: Membership,

    /**
     * Flag indicating if the room containing this event was created with the intention of being a direct chat.
     * See [Direct Messaging](https://matrix.org/docs/spec/client_server/r0.5.0#module-dm).
     */
    @SerialName("is_direct")
    val isDirect: Boolean? = null,

    @SerialName("third_party_invite")
    val thirdPartyInvite: Invite? = null,

    /**
     * Contains optional extra information about the event.
     */
    val unsigned: UnsignedData? = null
) {
    @Serializable
    public data class UnsignedData(
        /**
         * A subset of the state of the room at the time of the invite, if `membership` is `invite`.
         * Note that this state is informational, and SHOULD NOT be trusted;
         * once the client has joined the room, it SHOULD fetch the live state from the server and discard the invite_room_state.
         * Also, clients must not rely on any particular state being present here;
         * they SHOULD behave properly (with possibly a degraded but not a broken experience) in the absence of any particular events here.
         * If they are set on the room, at least the state for `m.room.avatar`, `m.room.canonical_alias`, `m.room.join_rules`, and `m.room.name` SHOULD be included.
         */
        @SerialName("invite_room_state")
        val inviteRoomState: List<StrippedState>
    )

    @Serializable
    public data class Invite(
        /**
         * A name which can be displayed to represent the user instead of their third party identifier
         */
        @SerialName("display_name")
        val displayName: String,

        /**
         * A block of content which has been signed, which servers can use to verify the event.
         * Clients should ignore this.
         */
        val signed: Signed
    )

    @Serializable
    public data class Signed(
        /**
         * The invited matrix user ID. Must be equal to the user_id property of the event.
         */
        val mxid: String,

        /**
         * A single signature from the verifying server, in the format specified by the Signing Events section of the server-server API.
         */
        val signatures: Map<String, Map<String, String>> /* Signatures */,

        /**
         * The token property of the containing third_party_invite object.
         */
        val token: String
    )
}

@Serializable
public enum class Membership {
    @SerialName("invite")
	INVITE,

    @SerialName("join")
	JOIN,

    @SerialName("knock")
	KNOCK,

    @SerialName("leave")
	LEAVE,

    @SerialName("ban")
	BAN;
}
