package io.github.matrixkt.events

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class JWK(
    /**
     * Key type. Must be `oct`.
     */
    @SerialName("kty")
    val keyType: String,

    /**
     * Key operations.
     * Must at least contain `encrypt` and `decrypt`.
     */
    @SerialName("key_ops")
    val keyOps: List<String>,

    /**
     * Algorithm. Must be `A256CTR`.
     */
    @SerialName("alg")
    val algorithm: String,

    /**
     * The key, encoded as urlsafe unpadded base64.
     */
    @SerialName("k")
    val key: String,

    /**
     * Extractable. Must be true. This is a [W3C extension](https://w3c.github.io/webcrypto/#iana-section-jwk).
     */
    @SerialName("ext")
    val extractable: Boolean
)
