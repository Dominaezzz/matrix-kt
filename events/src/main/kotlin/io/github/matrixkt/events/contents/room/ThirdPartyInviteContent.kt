package io.github.matrixkt.events.contents.room

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * An invitation to a room issued to a third party identifier, rather than a matrix user ID.
 *
 * Acts as an ``m.room.member`` invite event, where there isn't a target user_id to invite.
 * This event contains a token and a public key whose private key must be used to sign the token.
 * Any user who can present that signature may use this invitation to join the target room.
 */
@SerialName("m.room.third_party_invite")
@Serializable
public data class ThirdPartyInviteContent(
    /**
     * A user-readable string which represents the user who has been invited. This should not contain the user's third party ID, as otherwise when the invite is accepted it would leak the association between the matrix ID and the third party ID.
     */
    @SerialName("display_name")
    val displayName: String,

    /**
     * A URL which can be fetched, with querystring public_key=public_key, to validate whether the key has been revoked. The URL must return a JSON object containing a boolean property named 'valid'.
     */
    @SerialName("key_validity_url")
    val keyValidityUrl: String,

    /**
     * A base64-encoded ed25519 key with which token must be signed (though a signature from any entry in public_keys is also sufficient).
     * This exists for backwards compatibility.
     */
    @SerialName("public_key")
    val publicKey: String,

    /**
     * Keys with which the token may be signed.
     */
    @SerialName("public_keys")
    val publicKeys: List<PublicKeys> = emptyList()
) {
    @Serializable
    public data class PublicKeys(
        /**
         * An optional URL which can be fetched, with querystring public_key=public_key, to validate whether the key has been revoked. The URL must return a JSON object containing a boolean property named 'valid'.
         * If this URL is absent, the key must be considered valid indefinitely.
         */
        @SerialName("key_validity_url")
        val keyValidityUrl: String? = null,

        /**
         * A base-64 encoded ed25519 key with which token may be signed.
         */
        @SerialName("public_key")
        val publicKey: String
    )
}
