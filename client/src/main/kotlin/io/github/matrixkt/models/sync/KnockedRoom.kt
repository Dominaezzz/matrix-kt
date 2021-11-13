package io.github.matrixkt.models.sync

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class KnockedRoom(
    /**
     * The state of a room that the user has knocked upon. The state
     * events contained here have the same restrictions as `InviteState`
     * above.
     */
    @SerialName("knock_state")
    public val knockState: KnockState? = null
)
