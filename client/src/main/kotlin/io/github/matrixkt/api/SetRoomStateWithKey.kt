package io.github.matrixkt.api

import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

/**
 * .. For backwards compatibility with older links...
 * .. _`put-matrix-client-unstable-rooms-roomid-state-eventtype`:
 *
 * State events can be sent using this endpoint.  These events will be
 * overwritten if ``<room id>``, ``<event type>`` and ``<state key>`` all
 * match.
 *
 * Requests to this endpoint **cannot use transaction IDs**
 * like other ``PUT`` paths because they cannot be differentiated from the
 * ``state_key``. Furthermore, ``POST`` is unsupported on state paths.
 *
 * The body of the request should be the content object of the event; the
 * fields in this object will vary depending on the type of event. See
 * `Room Events`_ for the ``m.`` event specification.
 *
 * If the event type being sent is ``m.room.canonical_alias`` servers
 * SHOULD ensure that any new aliases being listed in the event are valid
 * per their grammar/syntax and that they point to the room ID where the
 * state event is to be sent. Servers do not validate aliases which are
 * being removed or are already present in the state event.
 */
public class SetRoomStateWithKey(
    public override val url: Url,
    public override val body: JsonObject
) : MatrixRpc.WithAuth<RpcMethod.Put, SetRoomStateWithKey.Url, JsonObject,
        SetRoomStateWithKey.Response> {
    @Resource("_matrix/client/r0/rooms/{roomId}/state/{eventType}/{stateKey}")
    @Serializable
    public class Url(
        /**
         * The room to set the state in
         */
        public val roomId: String,
        /**
         * The type of event to send.
         */
        public val eventType: String,
        /**
         * The state_key for the state to send. Defaults to the empty string. When
         * an empty string, the trailing slash on this endpoint is optional.
         */
        public val stateKey: String
    )

    @Serializable
    public class Response(
        /**
         * A unique identifier for the event.
         */
        @SerialName("event_id")
        public val eventId: String
    )
}
