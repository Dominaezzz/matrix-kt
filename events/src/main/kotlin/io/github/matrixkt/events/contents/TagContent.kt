package io.github.matrixkt.events.contents

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Informs the client of tags on a room.
 */
@SerialName("m.tag")
@Serializable
public data class TagContent(
    /**
     * The tags on the room and their contents.
     */
    val tags: Map<String, Tag> = emptyMap()
) {
    @Serializable
    public data class Tag(
        /**
         * A number in a range [0,1] describing a relative position of the room under the given tag.
         */
        val order: Double? = null
    )
}
