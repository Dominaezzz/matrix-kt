package io.github.matrixkt.events.contents.msginfo

import io.github.matrixkt.events.EncryptedFile
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class ImageInfo(
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
    val size: Long? = null,

    /**
     * 	The URL (typically [MXC URI](https://matrix.org/docs/spec/client_server/r0.5.0#mxc-uri)) to a thumbnail of the image.
     * 	Only present if the thumbnail is unencrypted.
     */
    @SerialName("thumbnail_url")
    val thumbnailUrl: String? = null,

    /**
     * Information on the encrypted thumbnail file, as specified in [End-to-end encryption](https://matrix.org/docs/spec/client_server/r0.5.0#encrypted-files).
     * Only present if the thumbnail is encrypted.
     */
    @SerialName("thumbnail_file")
    val thumbnailFile: EncryptedFile? = null,

    /**
     * Metadata about the image referred to in [thumbnailUrl].
     */
    @SerialName("thumbnail_info")
    val thumbnailInfo: ThumbnailInfo? = null
)
