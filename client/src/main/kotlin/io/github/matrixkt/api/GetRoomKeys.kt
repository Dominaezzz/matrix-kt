package io.github.matrixkt.api

import io.github.matrixkt.models.keybackup.RoomKeyBackup
import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.Serializable

/**
 * Retrieve the keys from the backup.
 */
public class GetRoomKeys(
    public override val url: Url
) : MatrixRpc.WithAuth<RpcMethod.Get, GetRoomKeys.Url, Nothing, GetRoomKeys.Response> {
    public override val body: Nothing
        get() = TODO()

    @Resource("_matrix/client/r0/room_keys/keys")
    @Serializable
    public class Url(
        /**
         * The backup from which to retrieve the keys.
         */
        public val version: String
    )

    @Serializable
    public class Response(
        /**
         * A map of room IDs to room key backup data.
         */
        public val rooms: Map<String, RoomKeyBackup>
    )
}
