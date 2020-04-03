package io.github.matrixkt.models.events.contents.room

import io.github.matrixkt.models.events.contents.Content
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This event is used to inform the room about which alias should be considered the canonical one.
 * This could be for display purposes or as suggestion to users which alias to use to advertise the room.
 *
 * A room with an `m.room.canonical_alias` event with an absent, null,
 * or empty alias field should be treated the same as a room with no `m.room.canonical_alias` event.
 */
@SerialName("m.room.canonical_alias")
@Serializable
data class CanonicalAliasContent(
    /**
     * The canonical alias.
     */
    val alias: String
) : Content()
