package io.github.matrixkt.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
public data class CrossSigningKey(
    /**
     * The public key.  The object must have exactly one property, whose name is
     * in the form `<algorithm>:<unpadded_base64_public_key>`, and whose value
     * is the unpadded base64 public key.
     */
    public val keys: Map<String, String>,
    /**
     * Signatures of the key, calculated using the process described at [Signing JSON](/appendices/#signing-json).
     * Optional for the master key. Other keys must be signed by the user's master key.
     */
    public val signatures: JsonObject? = null,
    /**
     * What the key is used for.
     */
    public val usage: List<String>,
    /**
     * The ID of the user the key belongs to.
     */
    @SerialName("user_id")
    public val userId: String
)
