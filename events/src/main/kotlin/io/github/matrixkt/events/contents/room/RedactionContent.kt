package io.github.matrixkt.events.contents.room

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Events can be redacted by either room or server admins.
 * Redacting an event means that all keys not required by the protocol are stripped off,
 * allowing admins to remove offensive or illegal content that may have been attached to any event.
 * This cannot be undone, allowing server owners to physically delete the offending data.
 * There is also a concept of a moderator hiding a message event, which can be undone,
 * but cannot be applied to state events.
 * The event that has been redacted is specified in the `redacts` event level key.
 */
@SerialName("m.room.redaction")
@Serializable
public data class RedactionContent(
    /**
     * The reason for the redaction, if any.
     */
    val reason: String? = null
)
