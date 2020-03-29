package io.github.matrixkt.models.events.contents

import kotlinx.serialization.Serializable

@Serializable
data class TagContent(
    /**
     * The tags on the room and their contents.
     */
    val tags: Map<String, Tag> = emptyMap()
) : Content() {
    @Serializable
    data class Tag(
        /**
         * A number in a range [0,1] describing a relative position of the room under the given tag.
         */
        val order: Double? = null
    )
}
