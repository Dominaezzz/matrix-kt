package io.github.matrixkt.api

import io.github.matrixkt.models.DeviceKeys
import io.github.matrixkt.models.OneTimeKeySerializer
import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Publishes end-to-end encryption keys for the device.
 */
public class UploadKeys(
    public override val url: Url,
    /**
     * The keys to be published
     */
    public override val body: Body
) : MatrixRpc.WithAuth<RpcMethod.Post, UploadKeys.Url, UploadKeys.Body, UploadKeys.Response> {
    @Resource("_matrix/client/r0/keys/upload")
    @Serializable
    public class Url

    @Serializable
    public class Body(
        /**
         * Identity keys for the device. May be absent if no new
         * identity keys are required.
         */
        @SerialName("device_keys")
        public val deviceKeys: DeviceKeys? = null,
        /**
         * One-time public keys for "pre-key" messages.  The names of
         * the properties should be in the format
         * ``<algorithm>:<key_id>``. The format of the key is determined
         * by the `key algorithm <#key-algorithms>`_.
         *
         * May be absent if no new one-time keys are required.
         */
        @SerialName("one_time_keys")
        public val oneTimeKeys: Map<String, @Serializable(OneTimeKeySerializer::class) Any>? = null
    )

    @Serializable
    public class Response(
        /**
         * For each key algorithm, the number of unclaimed one-time keys
         * of that type currently held on the server for this device.
         */
        @SerialName("one_time_key_counts")
        public val oneTimeKeyCounts: Map<String, Long>
    )
}