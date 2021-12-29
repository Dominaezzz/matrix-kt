package io.github.matrixkt.api

import io.github.matrixkt.models.keybackup.RoomKeyBackup
import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.Serializable

/**
 * Retrieve the keys from the backup for a given room.
 */
public class GetRoomKeysByRoomId(
    public override val url: Url
) : MatrixRpc.WithAuth<RpcMethod.Get, GetRoomKeysByRoomId.Url, Nothing, RoomKeyBackup> {
    public override val body: Nothing
        get() = TODO()

    @Resource("_matrix/client/r0/room_keys/keys/{roomId}")
    @Serializable
    public class Url(
        /**
         * The backup from which to retrieve the key.
         */
        public val version: String,
        /**
         * The ID of the room that the requested key is for.
         */
        public val roomId: String
    )
}
