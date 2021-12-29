package io.github.matrixkt.events.contents.room

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This event is used to "pin" particular events in a room for other participants to review later.
 * The order of the pinned events is guaranteed and based upon the order supplied in the event.
 * Clients should be aware that the current user may not be able to see some of the events pinned due to visibility settings in the room.
 * Clients are responsible for determining if a particular event in the pinned list is displayable,
 * and have the option to not display it if it cannot be pinned in the client.
 */
@SerialName("m.room.pinned_events")
@Serializable
public data class PinnedEventsContent(
    /**
     * An ordered list of event IDs to pin.
     */
    val pinned: List<String>
)
