package io.github.matrixkt.api

import io.github.matrixkt.models.RoomVisibility
import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.Serializable

/**
 * Sets the visibility of a given room in the server's public room
 * directory.
 *
 * Servers may choose to implement additional access control checks
 * here, for instance that room visibility can only be changed by
 * the room creator or a server administrator.
 */
public class SetRoomVisibilityOnDirectory(
    public override val url: Url,
    /**
     * The new visibility for the room on the room directory.
     */
    public override val body: Body
) : MatrixRpc.WithAuth<RpcMethod.Put, SetRoomVisibilityOnDirectory.Url, SetRoomVisibilityOnDirectory.Body, Unit> {
    @Resource("_matrix/client/r0/directory/list/room/{roomId}")
    @Serializable
    public class Url(
        /**
         * The room ID.
         */
        public val roomId: String
    )

    @Serializable
    public class Body(
        /**
         * The new visibility setting for the room.
         * Defaults to 'public'.
         */
        public val visibility: RoomVisibility? = null
    )
}
