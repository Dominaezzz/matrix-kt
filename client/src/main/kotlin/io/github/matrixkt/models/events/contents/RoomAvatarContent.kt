package io.github.matrixkt.models.events.contents

import io.github.matrixkt.models.events.contents.msginfo.ImageInfo
import kotlinx.serialization.Serializable

@Serializable
data class RoomAvatarContent(
    /**
     * Metadata about the image referred to in [url].
     */
    val info: ImageInfo? = null,

    /**
     * The URL to the image.
     */
    val url: String
) : Content()
