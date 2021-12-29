package io.github.matrixkt.events

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class EncryptedFile(
    /**
     * The URL to the file.
     */
    val url: String,

    /**
     * A [JSON Web Key](https://tools.ietf.org/html/rfc7517#appendix-A.3) object.
     */
    val key: JWK,

    /**
     * The Initialisation Vector used by AES-CTR, encoded as unpadded base64.
     */
    @SerialName("iv")
    val initialisationVector: String,

    /**
     * A map from an algorithm name to a hash of the ciphertext, encoded as unpadded base64.
     * Clients should support the SHA-256 hash, which uses the key sha256.
     */
    val hashes: Map<String, String>,

    /**
     * Version of the encrypted attachments protocol.
     * Must be `v2`.
     */
    @SerialName("v")
    val version: String
)
