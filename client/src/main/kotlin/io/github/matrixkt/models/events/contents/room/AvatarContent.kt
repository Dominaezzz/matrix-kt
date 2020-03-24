package io.github.matrixkt.models.events.contents.room

import io.github.matrixkt.models.events.contents.Content
import io.github.matrixkt.models.events.contents.msginfo.ImageInfo
import kotlinx.serialization.Serializable

@Serializable
data class AvatarContent(
    /**
     * Metadata about the image referred to in [url].
     */
    val info: ImageInfo? = null,

    /**
     * The URL to the image.
     */
    val url: String
) : Content()
