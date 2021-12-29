package io.github.matrixkt.api

import io.github.matrixkt.models.RoomVisibility
import io.github.matrixkt.models.events.MatrixEvent
import io.github.matrixkt.models.events.StateEvent
import io.github.matrixkt.models.events.UnsignedData
import io.github.matrixkt.models.events.contents.room.MemberContent
import io.github.matrixkt.models.events.contents.room.Membership
import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
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
) : MatrixRpc.WithAuth<RpcMethod.Get, InitialSync.Url, Nothing, InitialSync.Response> {
    public override val body: Nothing
        get() = TODO()

    @Resource("_matrix/client/r0/initialSync")
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
    public class PaginationChunk(
        /**
         * If the user is a member of the room this will be a
         * list of the most recent messages for this room. If
         * the user has left the room this will be the
         * messages that preceeded them leaving. This array
         * will consist of at most ``limit`` elements.
         */
        public val chunk: List<MatrixEvent>,
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
        public val invite: StateEvent<MemberContent, UnsignedData>? = null,
        /**
         * The user's membership state in this room.
         */
        public val membership: Membership,
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
        public val state: List<StateEvent<JsonObject, JsonObject>>? = null,
        /**
         * Whether this room is visible to the ``/publicRooms`` API
         * or not."
         */
        public val visibility: RoomVisibility? = null
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