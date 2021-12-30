package io.github.matrixkt.clientserver.models.wellknown

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class IdentityServerInformation(
    /**
     * The base URL for the identity server for client-server connections.
     */
    @SerialName("base_url")
    val baseUrl: String
)
