package io.github.matrixkt.clientserver.models.sync

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class UnreadNotificationCounts(
    /**
     * The number of unread notifications for this room with the highlight flag set.
     */
    @SerialName("highlight_count")
    val highlightCount: Long? = null,

    /**
     * The total number of unread notifications for this room.
     */
    @SerialName("notification_count")
    val notificationCount: Long? = null
)
