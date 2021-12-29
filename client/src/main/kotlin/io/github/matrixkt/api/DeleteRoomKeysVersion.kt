package io.github.matrixkt.api

import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.Serializable

/**
 * Delete an existing key backup. Both the information about the backup,
 * as well as all key data related to the backup will be deleted.
 */
public class DeleteRoomKeysVersion(
    public override val url: Url
) : MatrixRpc.WithAuth<RpcMethod.Delete, DeleteRoomKeysVersion.Url, Nothing, Unit> {
    public override val body: Nothing
        get() = TODO()

    @Resource("_matrix/client/r0/room_keys/version/{version}")
    @Serializable
    public class Url(
        /**
         * The backup version to delete, as returned in the `version`
         * parameter in the response of
         * [`POST
         * /_matrix/client/r0/room_keys/version`](/client-server-api/#post_matrixclientv3room_keysversion)
         * or [`GET
         * /_matrix/client/r0/room_keys/version/{version}`](/client-server-api/#get_matrixclientv3room_keysversionversion).
         */
        public val version: String
    )
}
