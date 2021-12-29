package io.github.matrixkt.events.contents.msginfo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class ThumbnailInfo(
    /**
     * The intended display height of the image in pixels.
     * This may differ from the intrinsic dimensions of the image file.
     */
    @SerialName("h")
    val height: Long? = null,

    /**
     * The intended display width of the image in pixels.
     * This may differ from the intrinsic dimensions of the image file.
     */
    @SerialName("w")
    val width: Long? = null,

    /**
     * The mimetype of the image, e.g. `image/jpeg`.
     */
    @SerialName("mimetype")
    val mimeType: String? = null,

    /**
     * Size of the image in bytes.
     */
    val size: Long? = null
)
