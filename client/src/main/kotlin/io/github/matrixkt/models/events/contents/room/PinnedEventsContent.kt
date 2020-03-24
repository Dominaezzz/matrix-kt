package io.github.matrixkt.models.events.contents.room

import io.github.matrixkt.models.events.contents.Content
import kotlinx.serialization.Serializable

@Serializable
data class PinnedEventsContent(
    /**
     * An ordered list of event IDs to pin.
     */
    val pinned: List<String>
) : Content()
