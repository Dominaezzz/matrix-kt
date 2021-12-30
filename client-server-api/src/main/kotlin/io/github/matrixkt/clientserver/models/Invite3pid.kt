package io.github.matrixkt.clientserver.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public class Invite3pid(
    /**
     * The invitee's third party identifier.
     */
    public val address: String,
    /**
     * An access token previously registered with the identity server. Servers
     * can treat this as optional to distinguish between r0.5-compatible clients
     * and this specification version.
     */
    @SerialName("id_access_token")
    public val idAccessToken: String,
    /**
     * The hostname+port of the identity server which should be used for third party identifier
     * lookups.
     */
    @SerialName("id_server")
    public val idServer: String,
    /**
     * The kind of address being passed in the address field, for example ``email``.
     */
    public val medium: Medium
)
