package io.github.matrixkt.clientserver.models.wellknown

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class HomeServerInformation(
    /**
     * The base URL for the homeserver for client-server connections.
     */
    @SerialName("base_url")
    val baseUrl: String
)
