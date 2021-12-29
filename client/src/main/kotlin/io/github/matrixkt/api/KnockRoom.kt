package io.github.matrixkt.api

import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * *Note that this API takes either a room ID or alias, unlike other membership APIs.*
 *
 * This API "knocks" on the room to ask for permission to join, if the user
 * is allowed to knock on the room. Acceptance of the knock happens out of
 * band from this API, meaning that the client will have to watch for updates
 * regarding the acceptance/rejection of the knock.
 *
 * If the room history settings allow, the user will still be able to see
 * history of the room while being in the "knock" state. The user will have
 * to accept the invitation to join the room (acceptance of knock) to see
 * messages reliably. See the `/join` endpoints for more information about
 * history visibility to the user.
 *
 * The knock will appear as an entry in the response of the
 * [`/sync`](/client-server-api/#get_matrixclientv3sync) API.
 */
public class KnockRoom(
    public override val url: Url,
    public override val body: Body
) : MatrixRpc.WithAuth<RpcMethod.Post, KnockRoom.Url, KnockRoom.Body, KnockRoom.Response> {
    @Resource("_matrix/client/r0/knock/{roomIdOrAlias}")
    @Serializable
    public class Url(
        /**
         * The room identifier or alias to knock upon.
         */
        public val roomIdOrAlias: String,
        /**
         * The servers to attempt to knock on the room through. One of the servers
         * must be participating in the room.
         */
        @SerialName("server_name")
        public val serverName: List<String>? = null
    )

    @Serializable
    public class Body(
        /**
         * Optional reason to be included as the `reason` on the subsequent
         * membership event.
         */
        public val reason: String? = null
    )

    @Serializable
    public class Response(
        /**
         * The knocked room ID.
         */
        @SerialName("room_id")
        public val roomId: String
    )
}
