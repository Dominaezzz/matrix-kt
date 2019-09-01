package io.github.matrixkt.models.events.contents

import kotlinx.serialization.Serializable

@Serializable
data class RoomPinnedEventsContent(
    /**
     * An ordered list of event IDs to pin.
     */
    val pinned: List<String>
) : Content()
