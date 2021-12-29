package io.github.matrixkt.api

import io.github.matrixkt.models.OneTimeKeySerializer
import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

/**
 * Claims one-time keys for use in pre-key messages.
 */
public class ClaimKeys(
    public override val url: Url,
    /**
     * Query defining the keys to be claimed
     */
    public override val body: Body
) : MatrixRpc.WithAuth<RpcMethod.Post, ClaimKeys.Url, ClaimKeys.Body, ClaimKeys.Response> {
    @Resource("_matrix/client/r0/keys/claim")
    @Serializable
    public class Url

    @Serializable
    public class Body(
        /**
         * The keys to be claimed. A map from user ID, to a map from
         * device ID to algorithm name.
         */
        @SerialName("one_time_keys")
        public val oneTimeKeys: Map<String, Map<String, String>>,
        /**
         * The time (in milliseconds) to wait when downloading keys from
         * remote servers. 10 seconds is the recommended default.
         */
        public val timeout: Long? = null
    )

    @Serializable
    public class Response(
        /**
         * If any remote homeservers could not be reached, they are
         * recorded here. The names of the properties are the names of
         * the unreachable servers.
         *
         * If the homeserver could be reached, but the user or device
         * was unknown, no failure is recorded. Instead, the corresponding
         * user or device is missing from the [oneTimeKeys] result.
         */
        public val failures: Map<String, JsonObject> = emptyMap(),
        /**
         * One-time keys for the queried devices. A map from user ID, to a
         * map from devices to a map from ``<algorithm>:<key_id>`` to the key object.
         *
         * See the `key algorithms <#key-algorithms>`_ section for information
         * on the Key Object format.
         */
        @SerialName("one_time_keys")
        public val oneTimeKeys: Map<String, Map<String, Map<String, @Serializable(with = OneTimeKeySerializer::class) Any>>>
    )
}
