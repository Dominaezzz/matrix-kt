package io.github.matrixkt.api

import io.github.matrixkt.models.keybackup.RoomKeyBackup
import io.github.matrixkt.models.keybackup.RoomKeysUpdateResponse
import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.Serializable

/**
 * Store several keys in the backup for a given room.
 */
public class PutRoomKeysByRoomId(
    public override val url: Url,
    /**
     * The backup data
     */
    public override val body: RoomKeyBackup
) : MatrixRpc.WithAuth<RpcMethod.Put, PutRoomKeysByRoomId.Url, RoomKeyBackup, RoomKeysUpdateResponse> {
    @Resource("_matrix/client/r0/room_keys/keys/{roomId}")
    @Serializable
    public class Url(
        /**
         * The backup in which to store the keys. Must be the current backup.
         */
        public val version: String,
        /**
         * The ID of the room that the keys are for.
         */
        public val roomId: String
    )
}
