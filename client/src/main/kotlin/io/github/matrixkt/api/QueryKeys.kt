package io.github.matrixkt.api

import io.github.matrixkt.models.DeviceKeys
import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.github.matrixkt.utils.resource.Resource
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
    public override val body: Body? = null
) : MatrixRpc.WithAuth<RpcMethod.Post, QueryKeys.Url, QueryKeys.Body?, QueryKeys.Response> {
    @Resource("/_matrix/client/r0/keys/query")
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
         * ``/keys/upload``, with the addition of an ``unsigned``
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
         * user or device is missing from the ``device_keys`` result.
         */
        public val failures: Map<String, JsonObject> = emptyMap()
    )
}
