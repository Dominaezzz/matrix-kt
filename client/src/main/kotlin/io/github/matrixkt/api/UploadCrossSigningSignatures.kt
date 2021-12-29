package io.github.matrixkt.api

import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

/**
 * Publishes cross-signing signatures for the user.  The request body is a
 * map from user ID to key ID to signed JSON object.
 */
public class UploadCrossSigningSignatures(
    public override val url: Url,
    /**
     * The signatures to be published.
     */
    public override val body: Map<String, Map<String, JsonObject>>
) : MatrixRpc.WithAuth<RpcMethod.Post, UploadCrossSigningSignatures.Url, Map<String, Map<String, JsonObject>>, UploadCrossSigningSignatures.Response> {
    @Resource("_matrix/client/r0/keys/signatures/upload")
    @Serializable
    public class Url

    @Serializable
    public class Response(
        /**
         * A map from user ID to key ID to an error for any signatures
         * that failed.  If a signature was invalid, the `errcode` will
         * be set to `M_INVALID_SIGNATURE`.
         */
        public val failures: Map<String, Map<String, JsonObject>>? = null
    )
}
