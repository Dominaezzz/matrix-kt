package io.github.matrixkt.api

import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.Serializable

/**
 * Remove a mapping of room alias to room ID.
 *
 * Servers may choose to implement additional access control checks here, for instance that
 * room aliases can only be deleted by their creator or a server administrator.
 *
 * .. Note::
 *    Servers may choose to update the ``alt_aliases`` for the ``m.room.canonical_alias``
 *    state event in the room when an alias is removed. Servers which choose to update the
 *    canonical alias event are recommended to, in addition to their other relevant permission
 *    checks, delete the alias and return a successful response even if the user does not
 *    have permission to update the ``m.room.canonical_alias`` event.
 */
public class DeleteRoomAlias(
    public override val url: Url
) : MatrixRpc.WithAuth<RpcMethod.Delete, DeleteRoomAlias.Url, Nothing, Unit> {
    public override val body: Nothing
        get() = TODO()

    @Resource("_matrix/client/r0/directory/room/{roomAlias}")
    @Serializable
    public class Url(
        /**
         * The room alias to remove.
         */
        public val roomAlias: String
    )
}
