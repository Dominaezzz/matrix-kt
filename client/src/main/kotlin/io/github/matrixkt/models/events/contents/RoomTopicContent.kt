package io.github.matrixkt.models.events.contents

import kotlinx.serialization.Serializable

@Serializable
data class RoomTopicContent(
    /**
     * The topic text.
     */
    val topic: String
) : Content()
