package io.github.matrixkt.events.contents

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Persists the user's preferred identity server, or preference to not use an identity server at all, in the user's account data.
 */
@SerialName("m.identity_server")
@Serializable
public data class IdentityServerContent(
    /**
     * The URL of the identity server the user prefers to use, or ``null`` if the user does not want to use an identity server.
     * This value is similar in structure to the ``base_url`` for identity servers in the ``.well-known/matrix/client`` schema.
     */
    @SerialName("base_url")
    val baseUrl: String? = null
)
