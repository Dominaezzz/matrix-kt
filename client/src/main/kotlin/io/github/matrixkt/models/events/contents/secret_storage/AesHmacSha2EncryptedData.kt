package io.github.matrixkt.models.events.contents.secret_storage

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Secrets encrypted using the m.secret_storage.v1.aes-hmac-sha2 algorithm are encrypted using AES-CTR-256, and authenticated using HMAC-SHA-256.
 *
 * The secret is encrypted as follows:
 * 1. Given the secret storage key, generate 64 bytes by performing an HKDF with SHA-256 as the hash, a salt of 32 bytes of 0, and with the secret name as the info. The first 32 bytes are used as the AES key, and the next 32 bytes are used as the MAC key
 * 2. Generate 16 random bytes, set bit 63 to 0 (in order to work around differences in AES-CTR implementations), and use this as the AES initialization vector. This becomes the iv property, encoded using base64.
 * 3. Encrypt the data using AES-CTR-256 using the AES key generated above. This encrypted data, encoded using base64, becomes the [ciphertext] property.
 * 4. Pass the raw encrypted data (prior to base64 encoding) through HMAC-SHA-256 using the MAC key generated above. The resulting MAC is base64-encoded and becomes the [mac] property.
 */
@SerialName("m.secret_storage.v1.aes-hmac-sha2")
@Serializable
public data class AesHmacSha2EncryptedData(
    /**
     * The 16-byte initialization vector, encoded as base64.
     */
    val iv: String,

    /**
     * The AES-CTR-encrypted data, encoded as base64.
     */
    val ciphertext: String,

    /**
     * The MAC, encoded as base64.
     */
    val mac: String
)
