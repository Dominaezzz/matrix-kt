package io.github.matrixkt.clientserver.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class Device(
    /**
     * Identifier of this device.
     */
    @SerialName("device_id")
    public val deviceId: String,

    /**
     * Display name set by the user for this device. Absent if no name has been set.
     */
    @SerialName("display_name")
    public val displayName: String? = null,

    /**
     * The IP address where this device was last seen. (May be a few minutes out of date, for efficiency reasons).
     */
    @SerialName("last_seen_ip")
    public val lastSeenIp: String? = null,

    /**
     * The timestamp (in milliseconds since the unix epoch) when this devices was last seen.
     * (May be a few minutes out of date, for efficiency reasons).
     */
    @SerialName("last_seen_ts")
    public val lastSeenTs: Long? = null
)
