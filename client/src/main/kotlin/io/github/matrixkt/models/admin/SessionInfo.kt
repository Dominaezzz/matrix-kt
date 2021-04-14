package io.github.matrixkt.models.admin

import kotlinx.serialization.Serializable

@Serializable
public class SessionInfo(
    /**
     * Information particular connections in the session.
     */
    public val connections: List<ConnectionInfo> = emptyList()
)
