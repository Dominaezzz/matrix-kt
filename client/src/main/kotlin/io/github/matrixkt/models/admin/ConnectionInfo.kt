package io.github.matrixkt.models.admin

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class ConnectionInfo(
    /**
     * Most recently seen IP address of the session.
     */
    val ip: String? = null,

    /**
     * Unix timestamp that the session was last active.
     */
    @SerialName("last_seen")
    val lastSeen: Long? = null,

    /**
     * User agent string last seen in the session.
     */
    @SerialName("user_agent")
    val userAgent: String? = null
)
