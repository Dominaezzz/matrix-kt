package io.github.matrixkt.api

import io.github.matrixkt.models.keybackup.RoomKeyBackup
import io.github.matrixkt.models.keybackup.RoomKeysUpdateResponse
import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.Serializable

/**
 * Store several keys in the backup.
 */
public class PutRoomKeys(
    public override val url: Url,
    /**
     * The backup data.
     */
    public override val body: Body
) : MatrixRpc.WithAuth<RpcMethod.Put, PutRoomKeys.Url, PutRoomKeys.Body, RoomKeysUpdateResponse> {
    @Resource("_matrix/client/r0/room_keys/keys")
    @Serializable
    public class Url(
        /**
         * The backup in which to store the keys. Must be the current backup.
         */
        public val version: String
    )

    @Serializable
    public class Body(
        /**
         * A map of room IDs to room key backup data.
         */
        public val rooms: Map<String, RoomKeyBackup>
    )
}
