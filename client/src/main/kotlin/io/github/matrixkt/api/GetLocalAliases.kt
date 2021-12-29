package io.github.matrixkt.api

import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.Serializable

/**
 * Get a list of aliases maintained by the local server for the
 * given room.
 *
 * This endpoint can be called by users who are in the room (external
 * users receive an ``M_FORBIDDEN`` error response). If the room's
 * ``m.room.history_visibility`` maps to ``world_readable``, any
 * user can call this endpoint.
 *
 * Servers may choose to implement additional access control checks here,
 * such as allowing server administrators to view aliases regardless of
 * membership.
 *
 * .. Note::
 *    Clients are recommended not to display this list of aliases prominently
 *    as they are not curated, unlike those listed in the ``m.room.canonical_alias``
 *    state event.
 */
public class GetLocalAliases(
    public override val url: Url
) : MatrixRpc.WithAuth<RpcMethod.Get, GetLocalAliases.Url, Nothing, GetLocalAliases.Response> {
    public override val body: Nothing
        get() = TODO()

    @Resource("_matrix/client/r0/rooms/{roomId}/aliases")
    @Serializable
    public class Url(
        /**
         * The room ID to find local aliases of.
         */
        public val roomId: String
    )

    @Serializable
    public class Response(
        /**
         * The server's local aliases on the room. Can be empty.
         */
        public val aliases: List<String>
    )
}
