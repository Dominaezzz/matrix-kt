package io.github.matrixkt.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ThirdPartyIdentifier(
    /**
     * The medium of the third party identifier.
     */
    val medium: Medium,

    /**
     * The third party identifier address.
     */
    val address: String,

    /**
     * The timestamp, in milliseconds, when the identifier was validated by the identity server.
     */
    @SerialName("validated_at")
    val validatedAt: Long,

    /**
     * The timestamp, in milliseconds, when the homeserver associated the third party identifier with the user.
     */
    @SerialName("added_at")
    val addedAt: Long
)
