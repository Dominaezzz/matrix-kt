package io.github.matrixkt.models.admin

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class WhoIsResponse(
    /**
     * The Matrix user ID of the user.
     */
    @SerialName("user_id")
    val userId: String? = null,

    /**
     * Each key is an identitfier for one of the user's devices.
     */
    val devices: Map<String, DeviceInfo> = emptyMap()
)
