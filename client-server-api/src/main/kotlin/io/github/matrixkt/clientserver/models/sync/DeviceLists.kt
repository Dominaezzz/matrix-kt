package io.github.matrixkt.clientserver.models.sync

import kotlinx.serialization.Serializable

@Serializable
public data class DeviceLists(
    /**
     * List of users who have updated their device identity keys, or who now share an encrypted room with the client since the previous sync response.
     */
    val changed: List<String> = emptyList(),

    /**
     * List of users with whom we do not share any encrypted rooms anymore since the previous sync response.
     */
    val left: List<String> = emptyList()
)
