package io.github.matrixkt.events.contents

import io.github.matrixkt.events.contents.msginfo.ImageInfo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This message represents a single sticker image.
 */
@SerialName("m.sticker")
@Serializable
public data class StickerContent(
    /**
     * A textual representation or associated description of the sticker image.
     * This could be the alt text of the original image, or a message
     * to accompany and further describe the sticker.
     */
    val body: String,

    /**
     * Metadata about the image referred to in [url] including a thumbnail representation.
     */
    val info: ImageInfo,

    /**
     * The URL to the sticker image. This must be a valid ``mxc://`` URI.
     */
    val url: String
)
