package io.github.matrixkt.events.contents.msginfo

import io.github.matrixkt.events.EncryptedFile
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class VideoInfo(
    /**
     * The duration of the video in milliseconds.
     */
    val duration: Long? = null,

    /**
     * The height of the video in pixels.
     */
    @SerialName("h")
    val height: Long? = null,

    /**
     * The width of the video in pixels.
     */
    @SerialName("w")
    val width: Long? = null,

    /**
     * The mimetype of the video e.g. `video/mp4`.
     */
    @SerialName("mimetype")
    val mimeType: String? = null,

    /**
     * The size of the video in bytes.
     */
    val size: Long? = null,

    /**
     * The URL (typically [MXC URI](https://matrix.org/docs/spec/client_server/r0.5.0#mxc-uri))
     * to a thumbnail of the video clip.
     * Only present if the thumbnail is unencrypted.
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
