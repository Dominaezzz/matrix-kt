package io.github.matrixkt.clientserver.api

import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

/**
 * Get information about an existing backup.
 */
public class GetRoomKeysVersion(
    public override val url: Url
) : MatrixRpc.WithAuth<RpcMethod.Get, GetRoomKeysVersion.Url, Nothing, GetRoomKeysVersion.Response> {
    public override val body: Nothing
        get() = TODO()

    @Resource("_matrix/client/r0/room_keys/version/{version}")
    @Serializable
    public class Url(
        /**
         * The backup version to get, as returned in the `version` parameter
         * of the response in
         * [`POST
         * /_matrix/client/r0/room_keys/version`](/client-server/#post_matrixclientv3room_keysversion)
         * or this endpoint.
         */
        public val version: String
    )

    @Serializable
    public class Response(
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
        public val authData: JsonObject,
        /**
         * The number of keys stored in the backup.
         */
        public val count: Long,
        /**
         * An opaque string representing stored keys in the backup.
         * Clients can compare it with the `etag` value they received
         * in the request of their last key storage request.  If not
         * equal, another client has modified the backup.
         */
        public val etag: String,
        /**
         * The backup version.
         */
        public val version: String
    )
}
