package io.github.matrixkt.models.events.contents.room

import io.github.matrixkt.models.events.contents.Content
import kotlinx.serialization.Serializable

@Serializable
data class TopicContent(
    /**
     * The topic text.
     */
    val topic: String
) : Content()
