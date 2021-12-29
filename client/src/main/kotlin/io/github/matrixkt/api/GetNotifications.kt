package io.github.matrixkt.api

import io.github.matrixkt.models.events.SyncEvent
import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * This API is used to paginate through the list of events that the
 * user has been, or would have been notified about.
 */
public class GetNotifications(
    public override val url: Url
) : MatrixRpc.WithAuth<RpcMethod.Get, GetNotifications.Url, Nothing, GetNotifications.Response> {
    public override val body: Nothing
        get() = TODO()

    @Resource("_matrix/client/r0/notifications")
    @Serializable
    public class Url(
        /**
         * Pagination token given to retrieve the next set of events.
         */
        public val from: String? = null,
        /**
         * Limit on the number of events to return in this request.
         */
        public val limit: Long? = null,
        /**
         * Allows basic filtering of events returned. Supply ``highlight``
         * to return only events where the notification had the highlight
         * tweak set.
         */
        public val only: String? = null
    )

    @Serializable
    public class Notification(
        /**
         * The action(s) to perform when the conditions for this rule are met.
         * See `Push Rules: API`_.
         */
        public val actions: List<JsonElement>,
        /**
         * The Event object for the event that triggered the notification.
         */
        public val event: SyncEvent,
        /**
         * The profile tag of the rule that matched this event.
         */
        @SerialName("profile_tag")
        public val profileTag: String? = null,
        /**
         * Indicates whether the user has sent a read receipt indicating
         * that they have read this message.
         */
        public val read: Boolean,
        /**
         * The ID of the room in which the event was posted.
         */
        @SerialName("room_id")
        public val roomId: String,
        /**
         * The unix timestamp at which the event notification was sent,
         * in milliseconds.
         */
        public val ts: Long
    )

    @Serializable
    public class Response(
        /**
         * The token to supply in the ``from`` param of the next
         * ``/notifications`` request in order to request more
         * events. If this is absent, there are no more results.
         */
        @SerialName("next_token")
        public val nextToken: String? = null,
        /**
         * The list of events that triggered notifications.
         */
        public val notifications: List<Notification>
    )
}
