package io.github.matrixkt.models.events.contents.secret_storage

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
public data class Secret(
    /**
     * Map from key ID the encrypted data.
     * The exact format for the encrypted data is dependent on the key algorithm.
     * See the definition of [AesHmacSha2EncryptedData] in the [m.secret_storage.v1.aes-hmac-sha2](https://spec.matrix.org/v1.1/client-server-api/#msecret_storagev1aes-hmac-sha2) section.
     */
    val encrypted: Map<String, JsonObject>
)
