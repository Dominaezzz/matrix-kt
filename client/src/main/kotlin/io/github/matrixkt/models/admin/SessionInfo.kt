package io.github.matrixkt.models.admin

import kotlinx.serialization.Serializable

@Serializable
class SessionInfo(
    /**
     * Information particular connections in the session.
     */
    val connections: List<ConnectionInfo> = emptyList()
)
