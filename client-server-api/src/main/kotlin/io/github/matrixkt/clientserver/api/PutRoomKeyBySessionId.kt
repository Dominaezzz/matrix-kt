package io.github.matrixkt.clientserver.api

import io.github.matrixkt.clientserver.models.keybackup.KeyBackupData
import io.github.matrixkt.clientserver.models.keybackup.RoomKeysUpdateResponse
import io.github.matrixkt.clientserver.MatrixRpc
import io.github.matrixkt.clientserver.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.Serializable

/**
 * Store a key in the backup.
 */
public class PutRoomKeyBySessionId(
    public override val url: Url,
    /**
     * The key data.
     */
    public override val body: KeyBackupData
) : MatrixRpc.WithAuth<RpcMethod.Put, PutRoomKeyBySessionId.Url, KeyBackupData, RoomKeysUpdateResponse> {
    @Resource("_matrix/client/r0/room_keys/keys/{roomId}/{sessionId}")
    @Serializable
    public class Url(
        /**
         * The backup in which to store the key. Must be the current backup.
         */
        public val version: String,
        /**
         * The ID of the room that the key is for.
         */
        public val roomId: String,
        /**
         * The ID of the megolm session that the key is for.
         */
        public val sessionId: String
    )
}
