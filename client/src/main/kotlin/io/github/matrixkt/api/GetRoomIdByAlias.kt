package io.github.matrixkt.api

import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Requests that the server resolve a room alias to a room ID.
 *
 * The server will use the federation API to resolve the alias if the
 * domain part of the alias does not correspond to the server's own
 * domain.
 */
public class GetRoomIdByAlias(
    public override val url: Url
) : MatrixRpc<RpcMethod.Get, GetRoomIdByAlias.Url, Nothing, GetRoomIdByAlias.Response> {
    public override val body: Nothing
        get() = TODO()

    @Resource("_matrix/client/r0/directory/room/{roomAlias}")
    @Serializable
    public class Url(
        /**
         * The room alias.
         */
        public val roomAlias: String
    )

    @Serializable
    public class Response(
        /**
         * The room ID for this room alias.
         */
        @SerialName("room_id")
        public val roomId: String? = null,
        /**
         * A list of servers that are aware of this room alias.
         */
        public val servers: List<String> = emptyList()
    )
}
