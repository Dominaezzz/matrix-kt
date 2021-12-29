package io.github.matrixkt.api

import io.github.matrixkt.models.keybackup.KeyBackupData
import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.Serializable

/**
 * Retrieve a key from the backup.
 */
public class GetRoomKeyBySessionId(
    public override val url: Url
) : MatrixRpc.WithAuth<RpcMethod.Get, GetRoomKeyBySessionId.Url, Nothing, KeyBackupData> {
    public override val body: Nothing
        get() = TODO()

    @Resource("_matrix/client/r0/room_keys/keys/{roomId}/{sessionId}")
    @Serializable
    public class Url(
        /**
         * The backup from which to retrieve the key.
         */
        public val version: String,
        /**
         * The ID of the room that the requested key is for.
         */
        public val roomId: String,
        /**
         * The ID of the megolm session whose key is requested.
         */
        public val sessionId: String
    )
}
