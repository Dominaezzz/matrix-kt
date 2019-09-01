package io.github.matrixkt.models.events.contents

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FullyReadContent(
    /**
     * The event the user's read marker is located at in the room.
     */
    @SerialName("event_id")
    val eventId: String
): Content()