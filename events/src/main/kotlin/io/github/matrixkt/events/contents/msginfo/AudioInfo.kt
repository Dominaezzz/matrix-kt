package io.github.matrixkt.events.contents.msginfo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class AudioInfo(
    /**
     * The duration of the audio in milliseconds.
     */
    val duration: Long? = null,

    /**
     * The mimetype of the audio e.g. `audio/aac`.
     */
    @SerialName("mimetype")
    val mimeType: String? = null,

    /**
     * The size of the audio clip in bytes.
     */
    val size: Long? = null
)
