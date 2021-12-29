package io.github.matrixkt.events.contents.room

import io.github.matrixkt.events.contents.msginfo.ImageInfo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A picture that is associated with the room.
 * This can be displayed alongside the room information.
 */
@SerialName("m.room.avatar")
@Serializable
public data class AvatarContent(
    /**
     * Metadata about the image referred to in [url].
     */
    val info: ImageInfo? = null,

    /**
     * The URL to the image.
     */
    val url: String
)
