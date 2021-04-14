package io.github.matrixkt.models.admin

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public class WhoIsResponse(
    /**
     * The Matrix user ID of the user.
     */
    @SerialName("user_id")
    public val userId: String? = null,

    /**
     * Each key is an identitfier for one of the user's devices.
     */
    public val devices: Map<String, DeviceInfo> = emptyMap()
)
