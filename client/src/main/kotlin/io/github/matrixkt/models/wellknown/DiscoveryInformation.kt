package io.github.matrixkt.models.wellknown

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DiscoveryInformation(
    /**
     * Used by clients to discover homeserver information.
     */
    @SerialName("m.homeserver")
    val homeServer: HomeServerInformation,

    /**
     * Used by clients to discover identity server information.
     */
    @SerialName("m.identity_server")
    val identityServer: IdentityServerInformation? = null
)
