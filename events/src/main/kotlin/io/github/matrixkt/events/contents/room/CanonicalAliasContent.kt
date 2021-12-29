package io.github.matrixkt.events.contents.room

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Informs the room as to which alias is the canonical one.
 *
 * This event is used to inform the room about which alias should be considered the canonical one.
 * This could be for display purposes or as suggestion to users which alias to use to advertise the room.
 */
@SerialName("m.room.canonical_alias")
@Serializable
public data class CanonicalAliasContent(
    /**
     * The canonical alias for the room.
     * If not present, null, or empty the room should be considered to have no canonical alias.
     */
    val alias: String? = null,

    /**
     * Alternative aliases the room advertises.
     * This list can have aliases despite the [alias] field being null, empty, or otherwise not present.
     */
    @SerialName("alt_aliases")
    val altAliases: List<String> = emptyList()
)
