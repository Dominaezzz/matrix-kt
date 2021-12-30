package io.github.matrixkt.clientserver.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class ThirdPartyIdentifier(
    /**
     * The medium of the third party identifier.
     */
    public val medium: Medium,

    /**
     * The third party identifier address.
     */
    public val address: String,

    /**
     * The timestamp, in milliseconds, when the identifier was validated by the identity server.
     */
    @SerialName("validated_at")
    public val validatedAt: Long,

    /**
     * The timestamp, in milliseconds, when the homeserver associated the third party identifier with the user.
     */
    @SerialName("added_at")
    public val addedAt: Long
)

@Serializable
public enum class Medium {
    @SerialName("email")
    EMAIL,
    @SerialName("msisdn")
    MSISDN;
}
