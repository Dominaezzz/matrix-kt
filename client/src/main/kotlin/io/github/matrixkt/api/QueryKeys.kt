package io.github.matrixkt.api

import io.github.matrixkt.models.CrossSigningKey
import io.github.matrixkt.models.DeviceKeys
import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

/**
 * Returns the current devices and identity keys for the given users.
 */
public class QueryKeys(
    public override val url: Url,
    /**
     * Query defining the keys to be downloaded
     */
    public override val body: Body
) : MatrixRpc.WithAuth<RpcMethod.Post, QueryKeys.Url, QueryKeys.Body, QueryKeys.Response> {
    @Resource("_matrix/client/r0/keys/query")
    @Serializable
    public class Url

    @Serializable
    public class Body(
        /**
         * The keys to be downloaded. A map from user ID, to a list of
         * device IDs, or to an empty list to indicate all devices for the
         * corresponding user.
         */
        @SerialName("device_keys")
        public val deviceKeys: Map<String, List<String>>,
        /**
         * The time (in milliseconds) to wait when downloading keys from
         * remote servers. 10 seconds is the recommended default.
         */
        public val timeout: Long? = null,
        /**
         * If the client is fetching keys as a result of a device update received
         * in a sync request, this should be the 'since' token of that sync request,
         * or any later sync token. This allows the server to ensure its response
         * contains the keys advertised by the notification in that sync.
         */
        public val token: String? = null
    )


    @Serializable
    public class Response(
        /**
         * Information on the queried devices. A map from user ID, to a
         * map from device ID to device information.  For each device,
         * the information returned will be the same as uploaded via
         * `/keys/upload`, with the addition of an `unsigned`
         * property.
         */
        @SerialName("device_keys")
        public val deviceKeys: Map<String, Map<String, DeviceKeys>> = emptyMap(),
        /**
         * If any remote homeservers could not be reached, they are
         * recorded here. The names of the properties are the names of
         * the unreachable servers.
         *
         * If the homeserver could be reached, but the user or device
         * was unknown, no failure is recorded. Instead, the corresponding
         * user or device is missing from the `device_keys` result.
         */
        public val failures: Map<String, JsonObject> = emptyMap(),
        /**
         * Information on the master cross-signing keys of the queried users.
         * A map from user ID, to master key information.  For each key, the
         * information returned will be the same as uploaded via
         * `/keys/device_signing/upload`, along with the signatures
         * uploaded via `/keys/signatures/upload` that the requesting user
         * is allowed to see.
         */
        @SerialName("master_keys")
        public val masterKeys: Map<String, CrossSigningKey>? = null,
        /**
         * Information on the self-signing keys of the queried users. A map
         * from user ID, to self-signing key information.  For each key, the
         * information returned will be the same as uploaded via
         * `/keys/device_signing/upload`.
         */
        @SerialName("self_signing_keys")
        public val selfSigningKeys: Map<String, CrossSigningKey>? = null,
        /**
         * Information on the user-signing key of the user making the
         * request, if they queried their own device information. A map
         * from user ID, to user-signing key information.  The
         * information returned will be the same as uploaded via
         * `/keys/device_signing/upload`.
         */
        @SerialName("user_signing_keys")
        public val userSigningKeys: Map<String, CrossSigningKey>? = null
    )
}
