package io.github.matrixkt.api

import io.github.matrixkt.models.AuthenticationData
import io.github.matrixkt.models.CrossSigningKey
import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Publishes cross-signing keys for the user.
 *
 * This API endpoint uses the [User-Interactive Authentication
 * API](/client-server-api/#user-interactive-authentication-api).
 */
public class UploadCrossSigningKeys(
    public override val url: Url,
    /**
     * The keys to be published.
     */
    public override val body: Body
) : MatrixRpc.WithAuth<RpcMethod.Post, UploadCrossSigningKeys.Url, UploadCrossSigningKeys.Body, Unit> {
    @Resource("_matrix/client/r0/keys/device_signing/upload")
    @Serializable
    public class Url

    @Serializable
    public class Body(
        /**
         * Additional authentication information for the
         * user-interactive authentication API.
         */
        public val auth: AuthenticationData? = null,
        /**
         * Optional. The user's master key.
         */
        @SerialName("master_key")
        public val masterKey: CrossSigningKey? = null,
        /**
         * Optional. The user's self-signing key. Must be signed by
         * the accompanying master key, or by the user's most recently
         * uploaded master key if no master key is included in the
         * request.
         */
        @SerialName("self_signing_key")
        public val selfSigningKey: CrossSigningKey? = null,
        /**
         * Optional. The user's user-signing key. Must be signed by
         * the accompanying master key, or by the user's most recently
         * uploaded master key if no master key is included in the
         * request.
         */
        @SerialName("user_signing_key")
        public val userSigningKey: CrossSigningKey? = null
    )
}
