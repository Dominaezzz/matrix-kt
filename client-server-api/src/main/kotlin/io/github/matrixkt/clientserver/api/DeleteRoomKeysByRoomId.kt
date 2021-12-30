package io.github.matrixkt.clientserver.api

import io.github.matrixkt.clientserver.models.keybackup.RoomKeysUpdateResponse
import io.github.matrixkt.clientserver.MatrixRpc
import io.github.matrixkt.clientserver.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.Serializable

/**
 * Delete the keys from the backup for a given room.
 */
public class DeleteRoomKeysByRoomId(
    public override val url: Url
) : MatrixRpc.WithAuth<RpcMethod.Delete, DeleteRoomKeysByRoomId.Url, Nothing, RoomKeysUpdateResponse> {
    public override val body: Nothing
        get() = TODO()

    @Resource("_matrix/client/r0/room_keys/keys/{roomId}")
    @Serializable
    public class Url(
        /**
         * The backup from which to delete the key.
         */
        public val version: String,
        /**
         * The ID of the room that the specified key is for.
         */
        public val roomId: String
    )
}
