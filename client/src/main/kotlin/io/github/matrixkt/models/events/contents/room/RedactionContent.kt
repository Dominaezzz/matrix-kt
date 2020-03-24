package io.github.matrixkt.models.events.contents.room

import io.github.matrixkt.models.events.contents.Content
import kotlinx.serialization.Serializable

@Serializable
data class RedactionContent(
    /**
     * The reason for the redaction, if any.
     */
    val reason: String? = null
) : Content()
