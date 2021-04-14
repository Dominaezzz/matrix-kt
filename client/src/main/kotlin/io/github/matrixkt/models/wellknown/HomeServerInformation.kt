package io.github.matrixkt.models.wellknown

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
