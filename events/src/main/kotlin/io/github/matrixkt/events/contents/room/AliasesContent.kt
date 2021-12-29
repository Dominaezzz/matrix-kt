package io.github.matrixkt.events.contents.room

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This event is sent by a homeserver directly to inform of changes to the list of aliases it knows about for that room.
 * The `state_key` for this event is set to the homeserver which owns the room alias.
 * The entire set of known aliases for the room is the union of all the `m.room.aliases` events, one for each homeserver.
 * Clients **should** check the validity of any room alias given in this list before presenting it to the user as trusted fact.
 * The lists given by this event should be considered simply as advice on which aliases might exist,
 * for which the client can perform the lookup to confirm whether it receives the correct room ID.
 */
@SerialName("m.room.aliases")
@Serializable
public data class AliasesContent(
    /**
     * A list of room aliases.
     */
    val aliases: List<String>
)
