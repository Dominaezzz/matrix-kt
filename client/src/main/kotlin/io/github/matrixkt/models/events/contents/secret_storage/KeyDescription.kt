package io.github.matrixkt.models.events.contents.secret_storage

import kotlinx.serialization.*
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@SerialName("m.secret_storage.key.*")
@JsonClassDiscriminator("algorithm")
@Serializable
public abstract class KeyDescription {
    /**
     * The name of the key.
     * If not given, the client may use a generic name such as “Unnamed key”,
     * or “Default key” if the key is marked as the default key (see below).
     */
    public abstract val name: String?

    /**
     * See [deriving keys from passphrases section](https://spec.matrix.org/v1.1/client-server-api/#deriving-keys-from-passphrases)
     * for a description of this property.
     */
    public abstract val passphrase: Passphrase?

    @SerialName("m.secret_storage.v1.aes-hmac-sha2")
    @Serializable
    public data class AesHmacSha2(
        /**
         * The name of the key.
         */
        override val name: String? = null,

        override val passphrase: Passphrase? = null,

        /**
         * The 16-byte initialization vector, encoded as base64.
         */
        val iv: String? = null,

        /**
         * The MAC of the result of encrypting 32 bytes of 0, encoded as base64.
         */
        val mac: String? = null,
    ) : KeyDescription()
}

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("algorithm")
@Serializable
public abstract class Passphrase {
    @SerialName("m.pbkdf2")
    @Serializable
    public data class PBKDF2(
        /**
         * The salt used in PBKDF2.
         */
        val salt: String,

        /**
         * The number of iterations to use in PBKDF2.
         */
        val iterations: Int,

        /**
         * The number of bits to generate for the key.
         */
        val bits: Int = 256
    ) : Passphrase()
}
