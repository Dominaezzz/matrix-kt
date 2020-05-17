package io.github.matrixkt.models.notifications

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class NotificationsResponse(
    /**
     * The token to supply in the from param of the next `/notifications` request in order to request more events.
     * If this is absent, there are no more results.
     */
    @SerialName("next_token")
    val nextToken: String? = null,

    /**
     * The list of events that triggered notifications.
     */
    val notifications: List<Notification>
)
