package io.github.matrixkt.models.admin

import kotlinx.serialization.Serializable

@Serializable
public class DeviceInfo(
    /**
     * A user's sessions (i.e. what they did with an access token from one login).
     */
    public val sessions: List<SessionInfo> = emptyList()
)
