package io.github.matrixkt.api

import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.github.matrixkt.utils.resource.Resource
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

/**
 * This returns the full state for this user, with an optional limit on the
 * number of messages per room to return.
 *
 * This endpoint was deprecated in r0 of this specification. Clients
 * should instead call the |/sync|_ API with no ``since`` parameter. See
 * the `migration guide
 * <https://matrix.org/docs/guides/client-server-migrating-from-v1.html#deprecated-endpoints>`_.
 */
@Suppress("DEPRECATION")
@Deprecated("This endpoint has been deprecated.")
public class InitialSync(
    public override val url: Url
) : MatrixRpc.WithAuth<RpcMethod.Get, InitialSync.Url, Any?, InitialSync.Response> {
    public override val body: Any?
        get() = null

    @Resource("/_matrix/client/r0/initialSync")
    @Serializable
    public class Url(
        /**
         * The maximum number of messages to return for each room.
         */
        public val limit: Long? = null,
        /**
         * Whether to include rooms that the user has left. If ``false`` then
         * only rooms that the user has been invited to or has joined are
         * included. If set to ``true`` then rooms that the user has left are
         * included as well. By default this is ``false``.
         */
        public val archived: Boolean? = null
    )

    @Serializable
    public class Event(
        /**
         * The fields in this object will vary depending on the type of event. When interacting with
         * the REST API, this is the HTTP body.
         */
        public val content: JsonObject,
        /**
         * The type of event. This SHOULD be namespaced similar to Java package naming conventions
         * e.g. 'com.example.subdomain.event.type'
         */
        public val type: String
    )

    @Serializable
    public class Signed(
        /**
         * The invited matrix user ID. Must be equal to the user_id property of the event.
         */
        public val mxid: String,
        /**
         * A single signature from the verifying server, in the format specified by the Signing
         * Events section of the server-server API.
         */
        public val signatures: Map<String, Map<String, String>>,
        /**
         * The token property of the containing third_party_invite object.
         */
        public val token: String
    )

    @Serializable
    public class Invite(
        /**
         * A name which can be displayed to represent the user instead of their third party
         * identifier
         */
        @SerialName("display_name")
        public val displayName: String,
        /**
         * A block of content which has been signed, which servers can use to verify the event.
         * Clients should ignore this.
         */
        public val signed: Signed
    )

    @Serializable
    public class StrippedState(
        /**
         * The ``content`` for the event.
         */
        public val content: JsonObject,
        /**
         * The ``sender`` for the event.
         */
        public val sender: String,
        /**
         * The ``state_key`` for the event.
         */
        @SerialName("state_key")
        public val stateKey: String,
        /**
         * The ``type`` for the event.
         */
        public val type: String
    )

    @Serializable
    public class UnsignedData(
        /**
         * A subset of the state of the room at the time of the invite, if ``membership`` is
         * ``invite``. Note that this state is informational, and SHOULD NOT be trusted; once the
         * client has joined the room, it SHOULD fetch the live state from the server and discard the
         * invite_room_state. Also, clients must not rely on any particular state being present here;
         * they SHOULD behave properly (with possibly a degraded but not a broken experience) in the
         * absence of any particular events here. If they are set on the room, at least the state for
         * ``m.room.avatar``, ``m.room.canonical_alias``, ``m.room.join_rules``, and ``m.room.name``
         * SHOULD be included.
         */
        @SerialName("invite_room_state")
        public val inviteRoomState: List<StrippedState>? = null
    )

    @Serializable
    public class EventContent(
        /**
         * The avatar URL for this user, if any.
         */
        @SerialName("avatar_url")
        public val avatarUrl: String? = null,
        /**
         * Flag indicating if the room containing this event was created with the intention of being
         * a direct chat. See `Direct Messaging`_.
         */
        @SerialName("is_direct")
        public val isDirect: Boolean? = null,
        /**
         * The membership state of the user.
         */
        public val membership: String,
        @SerialName("third_party_invite")
        public val thirdPartyInvite: Invite? = null,
        /**
         * Contains optional extra information about the event.
         */
        public val unsigned: UnsignedData? = null
    )

    @Serializable
    public class InviteEvent(
        public val content: EventContent? = null,
        /**
         * The ``user_id`` this membership event relates to. In all cases except for when
         * ``membership`` is
         * ``join``, the user ID sending the event does not need to match the user ID in the
         * ``state_key``,
         * unlike other events. Regular authorisation rules still apply.
         */
        @SerialName("state_key")
        public val stateKey: String? = null,
        public val type: String? = null
    )

    @Serializable
    public class RoomEvent(
        /**
         * The ID of the room associated with this event. Will not be present on events
         * that arrive through ``/sync``, despite being required everywhere else.
         */
        @SerialName("room_id")
        public val roomId: String
    )

    @Serializable
    public class PaginationChunk(
        /**
         * If the user is a member of the room this will be a
         * list of the most recent messages for this room. If
         * the user has left the room this will be the
         * messages that preceeded them leaving. This array
         * will consist of at most ``limit`` elements.
         */
        public val chunk: List<RoomEvent>,
        /**
         * A token which correlates to the last value in ``chunk``.
         * Used for pagination.
         */
        public val end: String,
        /**
         * A token which correlates to the first value in ``chunk``.
         * Used for pagination.
         */
        public val start: String
    )

    @Serializable
    public class RoomInfo(
        /**
         * The private data that this user has attached to
         * this room.
         */
        @SerialName("account_data")
        public val accountData: List<Event>? = null,
        /**
         * The invite event if ``membership`` is ``invite``
         */
        public val invite: InviteEvent? = null,
        /**
         * The user's membership state in this room.
         */
        public val membership: String,
        /**
         * The pagination chunk for this room.
         */
        public val messages: PaginationChunk? = null,
        /**
         * The ID of this room.
         */
        @SerialName("room_id")
        public val roomId: String,
        /**
         * If the user is a member of the room this will be the
         * current state of the room as a list of events. If the
         * user has left the room this will be the state of the
         * room when they left it.
         */
        public val state: List<JsonElement>? = null,
        /**
         * Whether this room is visible to the ``/publicRooms`` API
         * or not."
         */
        public val visibility: String? = null
    )

    @Serializable
    public class Response(
        /**
         * The global private data created by this user.
         */
        @SerialName("account_data")
        public val accountData: List<Event>? = null,
        /**
         * A token which correlates to the last value in ``chunk``. This
         * token should be used with the ``/events`` API to listen for new
         * events.
         */
        public val end: String,
        /**
         * A list of presence events.
         */
        public val presence: List<Event>,
        public val rooms: List<RoomInfo>
    )
}