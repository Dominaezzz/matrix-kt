package io.github.matrixkt.api

import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

/**
 * Update information about an existing backup.  Only `auth_data` can be modified.
 */
public class PutRoomKeysVersion(
    public override val url: Url,
    /**
     * The backup configuration
     */
    public override val body: Body
) : MatrixRpc.WithAuth<RpcMethod.Put, PutRoomKeysVersion.Url, PutRoomKeysVersion.Body, Unit> {
    @Resource("_matrix/client/r0/room_keys/version/{version}")
    @Serializable
    public class Url(
        /**
         * The backup version to update, as returned in the `version`
         * parameter in the response of
         * [`POST
         * /_matrix/client/r0/room_keys/version`](/client-server-api/#post_matrixclientv3room_keysversion)
         * or [`GET
         * /_matrix/client/r0/room_keys/version/{version}`](/client-server-api/#get_matrixclientv3room_keysversionversion).
         */
        public val version: String
    )

    @Serializable
    public class Body(
        /**
         * The algorithm used for storing backups.  Must be the same as
         * the algorithm currently used by the backup.
         */
        public val algorithm: String,
        /**
         * Algorithm-dependent data. See the documentation for the backup
         * algorithms in [Server-side key backups](/client-server-api/#server-side-key-backups) for
         * more information on the
         * expected format of the data.
         */
        @SerialName("auth_data")
        public val authData: JsonObject,
        /**
         * The backup version.  If present, must be the same as the
         * version in the path parameter.
         */
        public val version: String? = null
    )
}
