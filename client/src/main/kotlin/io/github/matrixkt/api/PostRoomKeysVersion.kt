package io.github.matrixkt.api

import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

/**
 * Creates a new backup.
 */
public class PostRoomKeysVersion(
    public override val url: Url,
    /**
     * The backup configuration.
     */
    public override val body: Body
) : MatrixRpc.WithAuth<RpcMethod.Post, PostRoomKeysVersion.Url, PostRoomKeysVersion.Body, PostRoomKeysVersion.Response> {
    @Resource("_matrix/client/r0/room_keys/version")
    @Serializable
    public class Url

    @Serializable
    public class Body(
        /**
         * The algorithm used for storing backups.
         */
        public val algorithm: String,
        /**
         * Algorithm-dependent data. See the documentation for the backup
         * algorithms in [Server-side key backups](/client-server-api/#server-side-key-backups) for
         * more information on the
         * expected format of the data.
         */
        @SerialName("auth_data")
        public val authData: JsonObject
    )

    @Serializable
    public class Response(
        /**
         * The backup version. This is an opaque string.
         */
        public val version: String
    )
}