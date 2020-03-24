package io.github.matrixkt.models.events.contents.room

import io.github.matrixkt.models.events.Membership
import io.github.matrixkt.models.events.contents.Content
import io.github.matrixkt.models.events.contents.StrippedState
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MemberContent(
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
) : Content() {
    @Serializable
    data class UnsignedData(
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
    data class Invite(
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
    data class Signed(
        /**
         * The invited matrix user ID. Must be equal to the user_id property of the event.
         */
        val mixid: String,

//        /**
//         * A single signature from the verifying server, in the format specified by the Signing Events section of the server-server API.
//         */
//        val signatures: Signatures,

        /**
         * The token property of the containing third_party_invite object.
         */
        val token: String
    )
}
