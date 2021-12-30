package io.github.matrixkt.clientserver.models.sync

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class JoinedRoom(
    /**
     * Information about the room which clients may need to correctly render it to users.
     */
    val summary: RoomSummary? = null,

    /**
     * Updates to the state, between the time indicated by the `since` parameter,
     * and the start of the `timeline` (or all state up to the start of the `timeline`, if `since` is not given, or `full_state` is true).
     *
     * N.B. state updates for `m.room.member` events will be incomplete if `lazy_load_members` is enabled in the `/sync` filter,
     * and only return the member events required to display the senders of the timeline events in this response.
     */
    val state: State? = null,

    /**
     * The timeline of messages and state changes in the room.
     */
    val timeline: Timeline? = null,

    /**
     * The ephemeral events in the room that aren't recorded in the timeline or state of the room. e.g. typing.
     */
    val ephemeral: Ephemeral? = null,

    /**
     * The private data that this user has attached to this room.
     */
    @SerialName("account_data")
    val accountData: AccountData? = null,

    /**
     * Counts of unread notifications for this room.
     * See the [Receiving notifications section](https://matrix.org/docs/spec/client_server/r0.5.0#receiving-notifications)
     * for more information on how these are calculated.
     */
    @SerialName("unread_notification")
    val unreadNotifications: UnreadNotificationCounts? = null
)
