package io.github.matrixkt.models.events.contents.room

import io.github.matrixkt.models.events.contents.Content
import kotlinx.serialization.Serializable

@Serializable
data class NameContent(
    /**
     * The name of the room. This MUST NOT exceed 255 bytes.
     */
    val name: String
) : Content()
