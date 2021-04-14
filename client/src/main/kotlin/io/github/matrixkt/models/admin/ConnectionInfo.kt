package io.github.matrixkt.models.admin

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public class ConnectionInfo(
    /**
     * Most recently seen IP address of the session.
     */
    public val ip: String? = null,

    /**
     * Unix timestamp that the session was last active.
     */
    @SerialName("last_seen")
    public val lastSeen: Long? = null,

    /**
     * User agent string last seen in the session.
     */
    @SerialName("user_agent")
    public val userAgent: String? = null
)
