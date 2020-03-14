package io.github.matrixkt.models

import io.github.matrixkt.models.events.MatrixEvent
import io.github.matrixkt.models.events.UnsignedData
import io.github.matrixkt.models.events.contents.RoomMemberContent
import io.github.matrixkt.models.push.PushCondition
import io.github.matrixkt.models.push.RuleSet
import io.github.matrixkt.models.wellknown.DiscoveryInformation
import kotlinx.serialization.*
import kotlinx.serialization.json.JsonInput
import kotlinx.serialization.json.JsonLiteral
import kotlinx.serialization.json.JsonObject

@Serializable
data class Invite3pid(
    /**
     * The hostname+port of the identity server which should be used for third party identifier lookups.
     */
    @SerialName("id_server")
    val idServer: String,

    /**
     * The kind of address being passed in the address field, for example email.
     */
    val medium: String,

    /**
     * The invitee's third party identifier.
     */
    val address: String
)

@Serializable
data class CreateRoomRequest(
    /**
     * A `public` visibility indicates that the room will be shown in the published room list.
     * A `private` visibility will hide the room from the published room list.
     * Rooms default to `private` visibility if this key is not included.
     * NB: This should not be confused with `join_rules` which also uses the word public.
     * One of: ["public", "private"]
     */
    val visibility: RoomVisibility? = null,

    /**
     * The desired room alias **local part**.
     * If this is included, a room alias will be created and mapped to the newly created room.
     * The alias will belong on the same homeserver which created the room.
     * For example, if this was set to "foo" and sent to the homeserver "example.com" the complete room alias would be `#foo:example.com`.
     *
     * The complete room alias will become the canonical alias for the room.
     */
    @SerialName("room_alias_name")
    val roomAliasName: String? = null,

    /**
     * If this is included, an `m.room.name` event will be sent into the room to indicate the name of the room.
     * See Room Events for more information on `m.room.name`.
     */
    val name: String? = null,

    /**
     * 	If this is included, an `m.room.topic` event will be sent into the room to indicate the topic for the room.
     * 	See Room Events for more information on `m.room.topic`.
     */
    val topic: String? = null,

    /**
     * A list of user IDs to invite to the room.
     * This will tell the server to invite everyone in the list to the newly created room.
     */
    val invite: List<String> = emptyList(),

    /**
     * A list of objects representing third party IDs to invite into the room.
     */
    @SerialName("invite_3pid")
    val invite3pid: List<Invite3pid> = emptyList(),

    /**
     * The room version to set for the room.
     * If not provided, the homeserver is to use its configured default.
     * If provided, the homeserver will return a 400 error with the errcode `M_UNSUPPORTED_ROOM_VERSION` if it does not support the room version.
     */
    @SerialName("room_version")
    val roomVersion: String? = null,

    /**
     * Extra keys, such as `m.federate`, to be added to the content of the `m.room.create` event.
     * The server will clobber the following keys: `creator`, `room_version`.
     * Future versions of the specification may allow the server to clobber other keys.
     */
    @SerialName("creation_content")
    val creationContent: JsonObject? = null,

    /**
     * A list of state events to set in the new room.
     * This allows the user to override the default state events set in the new room.
     * The expected format of the state events are an object with type, state_key and content keys set.
     *
     * Takes precedence over events set by [preset], but gets overridden by [name] and [topic] keys.
     */
    @SerialName("intial_state")
    val initialState: List<StateEvent> = emptyList(),

    /**
     * Convenience parameter for setting various default state events based on a preset.
     *
     * If unspecified, the server should use the [visibility] to determine which preset to use.
     * A visibility of `public` equates to a preset of `public_chat` and `private` visibility equates to a preset of `private_chat`.
     * One of: ["private_chat", "public_chat", "trusted_private_chat"]
     */
    val preset: RoomPreset? = null,

    /**
     * This flag makes the server set the `is_direct` flag on the `m.room.member` events sent to the users in [invite] and [invite3pid].
     * See [Direct Messaging](https://matrix.org/docs/spec/client_server/r0.5.0#module-dm) for more information.
     */
    @SerialName("is_direct")
    val isDirect: Boolean? = null,

    /**
     * The power level content to override in the default power level event.
     * This object is applied on top of the generated `m.room.power_levels` event content prior to it being sent to the room.
     * Defaults to overriding nothing.
     */
    @SerialName("power_level_content_override")
    val powerLevelContentOverride: JsonObject? = null
) {
    @Serializable
    data class StateEvent(
        /**
         * The type of event to send.
         */
        val type: String,

        /**
         * The state_key of the state event. Defaults to an empty string.
         */
        @SerialName("state_key")
        val stateKey: String? = null,

        /**
         * The content of the event.
         */
        val content: JsonObject
    )
}

@Serializable
data class CreateRoomResponse(
    /**
     * The created room's ID.
     */
    @SerialName("room_id")
    val roomId: String
)

@Serializable
data class CreateRoomAliasRequest(
    /**
     * The room ID to set.
     */
    @SerialName("room_id")
    val roomId: String
)

@Serializable
data class ResolveRoomAliasResponse(
    /**
     * 	The room ID for this room alias.
     */
    @SerialName("room_id")
    val roomId: String,

    /**
     * A list of servers that are aware of this room alias.
     */
    val servers: List<String>
)

@Serializable
data class GetJoinedRoomsResponse(
    /**
     * The ID of each room in which the user has joined membership.
     */
    @SerialName("joined_rooms")
    val joinedRooms: List<String>
)

@Serializable
data class InviteRequest(
    /**
     * The fully qualified user ID of the invitee.
     */
    @SerialName("user_id")
    val userId: String
)

@Serializable
data class JoinRoomRequest(
    /**
     * A signature of an `m.third_party_invite` token to prove that this
     * user owns a third party identity which has been invited to the room.
     */
    @SerialName("third_party_signed")
    val thirdPartySigned: ThirdPartySigned
)

@Serializable
data class ThirdPartySigned(
    /**
     * The Matrix ID of the user who issued the invite.
     */
    val sender: String,

    /**
     * The Matrix ID of the invitee.
     */
    val mixid: String,

    /**
     * The state key of the `m.third_party_invite` event.
     */
    val token: String

//    /**
//     * A signatures object containing a signature of the entire signed object.
//     */
//    val signatures: Signatures
)

@Serializable
data class JoinRoomResponse(
    @SerialName("room_id")
    val roomId: String
)

@Serializable
data class KickRequest(
    /**
     * The fully qualified user ID of the user being kicked.
     */
    @SerialName("user_id")
    val userId: String,

    /**
     * 	The reason the user has been kicked.
     * 	This will be supplied as the reason on the target's updated `m.room.member` event.
     */
    val reason: String? = null
)

@Serializable
data class BanRequest(
    /**
     * The fully qualified user ID of the user being banned.
     */
    @SerialName("user_id")
    val userId: String,

    /**
     * 	The reason the user has been banned.
     * 	This will be supplied as the reason on the target's updated `m.room.member` event.
     */
    val reason: String? = null
)

@Serializable
data class UnBanRequest(
    /**
     * The fully qualified user ID of the user being unbanned.
     */
    @SerialName("user_id")
    val userId: String
)

@Serializable
data class VisibilityResponse(
    /**
     * The visibility of the room in the directory. One of: ["private", "public"]
     */
    val visibility: RoomVisibility
)

@Serializable
data class VisibilityRequest(
    /**
     * The new visibility setting for the room. Defaults to 'public'. One of: ["private", "public"]
     */
    val visibility: RoomVisibility
)

@Serializable
data class PublicRoomsChunk(
    /**
     * Aliases of the room. May be empty.
     */
    val aliases: List<String>? = null,

    /**
     * The canonical alias of the room, if any.
     */
    @SerialName("canonical_alias")
    val canonicalAlias: String? = null,

    /**
     * The name of the room, if any.
     */
    val name: String? = null,

    /**
     * The number of members joined to the room.
     */
    @SerialName("num_joined_members")
    val numJoinedMembers: Int,

    /**
     * The ID of the room.
     */
    @SerialName("room_id")
    val roomId: String,

    /**
     * The topic of the room, if any.
     */
    val topic: String? = null,

    /**
     * Whether the room may be viewed by guest users without joining.
     */
    @SerialName("world_readable")
    val worldReadable: Boolean,

    /**
     * Whether guest users may join the room and participate in it.
     * If they can, they will be subject to ordinary power level rules like any other user.
     */
    @SerialName("guest_can_join")
    val guestCanJoin: Boolean,

    /**
     * The URL for the room's avatar, if one is set.
     */
    @SerialName("avatar_url")
    val avatarUrl: String? = null
)

@Serializable
data class PublicRoomsResponse(
    /**
     * A paginated chunk of public rooms.
     */
    val chunk: List<PublicRoomsChunk>,

    /**
     * A pagination token for the response.
     * The absence of this token means there are no more results to fetch and the client should stop paginating.
     */
    @SerialName("next_batch")
    val nextBatch: String? = null,

    /**
     * A pagination token that allows fetching previous results.
     * The absence of this token means there are no results before this batch, i.e. this is the first batch.
     */
    @SerialName("prev_batch")
    val prevBatch: String? = null,

    /**
     * An estimate on the total number of public rooms, if the server has an estimate.
     */
    @SerialName("total_room_count_estimate")
    val totalRoomCountEstimate: Int? = null
)

@Serializable
data class SearchPublicRoomsRequest(
    /**
     * Limit the number of results returned.
     */
    val limit: Int? = null,

    /**
     * 	A pagination token from a previous request, allowing clients to get the next (or previous) batch of rooms.
     * 	The direction of pagination is specified solely by which token is supplied, rather than via an explicit flag.
     */
    val since: String? = null,

    /**
     * Filter to apply to the results.
     */
    val filter: Filter? = null,

    /**
     * Whether or not to include all known networks/protocols from application services on the homeserver.
     * Defaults to false.
     */
    @SerialName("include_all_networks")
    val includeAllNetworks: Boolean? = null,

    /**
     * 	The specific third party network/protocol to request from the homeserver.
     * 	Can only be used if `include_all_networks` is false.
     */
    @SerialName("third_party_instance_id")
    val thirdPartyInstanceId: String? = null
) {
    @Serializable
    data class Filter(
        @SerialName("generic_search_term")
        val genericSearchTerm: String? = null
    )
}


@Serializable
data class UpgradeRoomRequest(
    /**
     * The new version for the room.
     */
    @SerialName("new_version")
    val newVersion: String
)

@Serializable
data class UpgradeRoomResponse(
    /**
     * The ID of the new room.
     */
    @SerialName("replacement_room")
    val replacementRoom: String
)

@Serializable
data class SyncResponse(
    /**
     * The batch token to supply in the since param of the next `/sync` request.
     */
    @SerialName("next_batch")
    val nextBatch: String,

    /**
     * Updates to rooms.
     */
    val rooms: Rooms? = null,

    /**
     * The updates to the presence status of other users.
     */
    val presence: Presence? = null,

    /**
     * The global private data created by this user.
     */
    @SerialName("account_data")
    val accountData: AccountData? = null,

    /**
     * Information on the send-to-device messages for the client device,
     * as defined in [Send-to-Device messaging](https://matrix.org/docs/spec/client_server/r0.5.0#send-to-device-sync).
     */
    @SerialName("to_device")
    val toDevice: ToDevice? = null,

//    /**
//     * Information on end-to-end device updates, as specified in [End-to-end encryption](https://matrix.org/docs/spec/client_server/r0.5.0#device-lists-sync).
//     */
//    @SerialName("device_lists")
//    val deviceLists: DeviceLists? = null,

    /**
     * Information on end-to-end encryption keys, as specified in [End-to-end encryption](https://matrix.org/docs/spec/client_server/r0.5.0#device-lists-sync).
     */
    @SerialName("device_one_time_keys_count")
    val deviceOneTimeKeysCount: Map<String, Long>? = null
) {
    @Serializable
    data class Rooms(
        /**
         * The rooms that the user has joined.
         */
        val join: Map<String, JoinedRoom>? = null,

        /**
         * The rooms that the user has been invited to.
         */
        val invite: Map<String, InvitedRoom>? = null,

        /**
         * The rooms that the user has left or been banned from.
         */
        val leave: Map<String, LeftRoom>? = null
    )

    @Serializable
    data class JoinedRoom(
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

    @Serializable
    data class RoomSummary(
        /**
         * The users which can be used to generate a room name if the room does not have one.
         * Required if the room's `m.room.name` or `m.room.canonical_alias` state events are unset or empty.
         *
         * This should be the first 5 members of the room, ordered by stream ordering, which are joined or invited.
         * The list must never include the client's own user ID.
         * When no joined or invited members are available, this should consist of the banned and left users.
         * More than 5 members may be provided, however less than 5 should only be provided when there are less than 5 members to represent.
         *
         * When lazy-loading room members is enabled, the membership events for the heroes MUST be included in the state, unless they are redundant.
         * When the list of users changes, the server notifies the client by sending a fresh list of heroes.
         * If there are no changes since the last sync, this field may be omitted.
         */
        @SerialName("m.heroes")
        val heroes: List<String>? = null,

        /**
         * The number of users with `membership` of join, including the client's own user ID.
         * If this field has not changed since the last sync, it may be omitted.
         * Required otherwise.
         */
        @SerialName("m.joined_member_count")
        val joinedMemberCount: Long? = null,

        /**
         * The number of users with `membership` of `invite`.
         * If this field has not changed since the last sync, it may be omitted.
         * Required otherwise.
         */
        @SerialName("m.invited_member_count")
        val invitedMemberCount: Long? = null
    )

    @Serializable
    data class Ephemeral(
        /**
         * List of events.
         */
        val events: List<Event>? = null
    )

    @Serializable
    data class UnreadNotificationCounts(
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

    @Serializable
    data class InvitedRoom(
        /**
         * The state of a room that the user has been invited to.
         * These state events may only have the `sender`, `type`, `state_key` and `content` keys present.
         * These events do not replace any state that the client already has for the room, for example if the client has archived the room.
         * Instead the client should keep two separate copies of the state: the one from the `invite_state` and one from the archived `state`.
         * If the client joins the room then the current state will be given as a delta against the archived `state` not the `invite_state`.
         */
        @SerialName("invite_state")
        val inviteState: InviteState? = null
    )

    @Serializable
    data class InviteState(
        /**
         * The StrippedState events that form the invite state.
         */
        val events: List<StrippedState>? = null
    )

    @Serializable
    data class StrippedState(
        /**
         * The `content` for the event.
         */
        val content: JsonObject,

        /**
         * The `state_key` for the event.
         */
        @SerialName("state_key")
        val stateKey: String,

        /**
         * The `type` for the event.
         */
        val type: String,

        /**
         * The `sender` for the event.
         */
        val sender: String
    )

    @Serializable
    data class LeftRoom(
        /**
         * The state updates for the room up to the start of the timeline.
         */
        val state: State? = null,

        /**
         * The timeline of messages and state changes in the room up to the point when the user left.
         */
        val timeline: Timeline? = null,

        /**
         * The private data that this user has attached to this room.
         */
        @SerialName("account_data")
        val accountData: AccountData? = null
    )

    @Serializable
    data class State(
        /**
         * List of events.
         */
        val events: List<MatrixEvent>? = null
    )

    @Serializable
    data class Timeline(
        /**
         * List of events.
         */
        val events: List<MatrixEvent>? = null,

        /**
         * 	True if the number of events returned was limited by the `limit` on the filter.
         */
        val limited: Boolean? = null,

        /**
         * A token that can be supplied to the `from` parameter of the rooms/{roomId}/messages endpoint.
         */
        @SerialName("prev_batch")
        val prevBatch: String? = null
    )

    @Serializable
    data class Presence(
        /**
         * List of events.
         */
        val events: List<Event>? = null
    )

    @Serializable
    data class AccountData(
        /**
         * List of events.
         */
        val events: List<Event>? = null
    )

    @Serializable
    data class Event(
        /**
         * The fields in this object will vary depending on the type of event.
         * When interacting with the REST API, this is the HTTP body.
         */
        val content: JsonObject,

        /**
         * The type of event.
         * This SHOULD be namespaced similar to Java package naming conventions e.g. 'com.example.subdomain.event.type'
         */
        val type: String,

        /**
         * The Matrix user ID of the user who sent this event.
         */
        val sender: String? = null
    )

    @Serializable
    data class ToDevice(
        /**
         * List of send-to-device messages.
         */
        val events: List<Event> = emptyList()
    )
}

@Serializable
data class GetMembersResponse(
    val chunk: List<MemberEvent>
) {
    @Serializable
    data class MemberEvent(
        /**
         * The fields in this object will vary depending on the type of event.
         * When interacting with the REST API, this is the HTTP body.
         */
        val content: RoomMemberContent,

        /**
         * The type of event.
         * This SHOULD be namespaced similar to Java package naming conventions e.g. 'com.example.subdomain.event.type'
         */
        val type: String,

        /**
         * The globally unique event identifier.
         */
        @SerialName("event_id")
        val eventId: String,

        /**
         * Contains the fully-qualified ID of the user who sent this event.
         */
        val sender: String,

        /**
         * Timestamp in milliseconds on originating homeserver when this event was sent.
         */
        @SerialName("origin_server_ts")
        val originServerTimestamp: Long,

        /**
         * Contains optional extra information about the event.
         */
        val unsigned: UnsignedData? = null,

        /**
         * The ID of the room associated with this event.
         * Will not be present on events that arrive through `/sync`,
         * despite being required everywhere else.
         */
        @SerialName("room_id")
        val roomId: String,

        /**
         * A unique key which defines the overwriting semantics for this piece of room state.
         * This value is often a zero-length string.
         * The presence of this key makes this event a State Event.
         * State keys starting with an @ are reserved for referencing user IDs, such as room members.
         * With the exception of a few events, state events set with a given user's ID as the state key MUST only be set by that user.
         */
        @SerialName("state_key")
        val stateKey: String? = null,

        /**
         * The previous content for this event. If there is no previous content, this key will be missing.
         */
        @SerialName("prev_content")
        val prevContent: RoomMemberContent? = null
    )
}

@Serializable
data class RoomMember(
    /**
     * The display name of the user this object is representing.
     */
    @SerialName("display_name")
    val displayName: String? = null,

    /**
     * The mxc avatar url of the user this object is representing.
     */
    @SerialName("avatar_url")
    val avatarUrl: String? = null
)

@Serializable
data class JoinedMembersResponse(
    /**
     * A map from user ID to a [RoomMember] object.
     */
    val joined: Map<String, RoomMember>
)

@Serializable
enum class Direction {
    @SerialName("f")
    F,
    @SerialName("b")
    B;
}

@Serializable
data class MessagesResponse(
    /**
     * 	The token the pagination starts from.
     * 	If `dir=b` this will be the token supplied in `from`.
     */
    val start: String? = null,

    /**
     * The token the pagination ends at.
     * If `dir=b` this token should be used again to request even earlier events.
     */
    val end: String? = null,

    /**
     * A list of room events.
     * The order depends on the dir parameter.
     * For `dir=b` events will be in reverse-chronological order, for `dir=f` in chronological order,
     * so that events start at the `from` point.
     */
    val chunk: List<MatrixEvent>? = null,

    /**
     * A list of state events relevant to showing the `chunk`.
     * For example, if `lazy_load_members` is enabled in the filter then this
     * may contain the membership events for the senders of events in the `chunk`.
     *
     * Unless `include_redundant_members` is `true`,
     * the server may remove membership events which would have already been sent to the client in prior calls to this endpoint,
     * assuming the membership of those members has not changed.
     */
    val state: List<MatrixEvent>? = null
)

@Serializable
data class SendStateEventResponse(
    /**
     * A unique identifier for the event.
     */
    @SerialName("event_id")
    val eventId: String
)

@Serializable
data class SendMessageEventResponse(
    /**
     * A unique identifier for the event.
     */
    @SerialName("event_id")
    val eventId: String
)

@Serializable
data class SearchUsersRequest(
    /**
     * The term to search for.
     */
    @SerialName("search_term")
    val searchTerm: String,

    /**
     * The maximum number of results to return. Defaults to 10.
     */
    val limit: Long? = null
)

@Serializable
data class SearchUsersResponse(
    /**
     * Ordered by rank and then whether or not profile info is available.
     */
    val results: List<User>,

    /**
     * Indicates if the result list has been truncated by the limit.
     */
    val limited: Boolean
) {
    @Serializable
    data class User(
        /**
         * The user's matrix user ID.
         */
        @SerialName("user_id")
        val userId: String,

        /**
         * The display name of the user, if one exists.
         */
        @SerialName("display_name")
        val displayName: String? = null,

        /**
         * The avatar url, as an MXC, if one exists.
         */
        @SerialName("avatar_url")
        val avatarUrl: String? = null
    )
}

@Serializable
data class SetDisplayNameRequest(
    /**
     * The new display name for this user.
     */
    @SerialName("display_name")
    val displayName: String? = null
)

@Serializable
data class GetDisplayNameResponse(
    /**
     * The user's display name if they have set one, otherwise not present.
     */
    @SerialName("displayname")
    val displayName: String? = null
)

@Serializable
data class SetAvatarUrlRequest(
    /**
     * The new avatar URL for this user.
     */
    @SerialName("avatar_url")
    val avatarUrl: String? = null
)

@Serializable
data class GetAvatarUrlResponse(
    /**
     * The user's avatar URL if they have set one, otherwise not present.
     */
    @SerialName("avatar_url")
    val avatarUrl: String? = null
)

@Serializable
data class GetUserProfileResponse(
    /**
     * The user's avatar URL if they have set one, otherwise not present.
     */
    @SerialName("avatar_url")
    val avatarUrl: String? = null,

    /**
     * The user's display name if they have set one, otherwise not present.
     */
    @SerialName("displayname")
    val displayName: String? = null
)

@Serializable
data class UploadResponse(
    /**
     * The [MXC URI](https://matrix.org/docs/spec/client_server/r0.5.0#mxc-uri) to the uploaded content.
     */
    @SerialName("content_uri")
    val contentUri: String
)

@Serializable
enum class ThumbnailMethod {
    @SerialName("crop")
    CROP,
    @SerialName("scale")
    SCALE;
}

@Serializable
data class UrlPreviewResponse(
    /**
     * The byte-size of the image. Omitted if there is no image attached.
     */
    @SerialName("matrix:image:size")
    val imageSize: Long? = null,

    /**
     * An [MXC URI](https://matrix.org/docs/spec/client_server/r0.5.0#mxc-uri) to the image. Omitted if there is no image.
     */
    @SerialName("og:image")
    val image: String? = null,

    @SerialName("og:title")
    val title: String? = null,

    @SerialName("og:description")
    val description: String? = null,

    @SerialName("og:image:type")
    val imageType: String? = null,

    @SerialName("og:image:height")
    val imageHeight: Int? = null,

    @SerialName("og:image:width")
    val imageWidth: Int? = null
)

@Serializable
data class ConfigResponse(
    /**
     * The maximum size an upload can be in bytes.
     * Clients SHOULD use this as a guide when uploading content.
     * If not listed or null, the size limit should be treated as unknown.
     */
    @SerialName("m.upload.size")
    val uploadSize: Int? = null
)

@Serializable
data class LoginFlow(
    /**
     * The login type. This is supplied as the type when logging in.
     */
    val type: String
)

@Serializable
data class LoginFlowsResponse(
    /**
     * The homeserver's supported login types.
     */
    val flows: List<LoginFlow>
)

@Serializable
data class LoginRequest(
    /**
     * The login type being used.
     * One of: ["m.login.password", "m.login.token"]
     */
    val type: String,

    /**
     * Identification information for the user.
     */
    val identifier: UserIdentifier? = null,

    /**
     * The fully qualified user ID or just local part of the user ID, to log in.
     * Deprecated in favour of [identifier].
     */
    @Deprecated(message = "Deprecated in favour of `identifier`")
    val user: String? = null,

    /**
     * When logging in using a third party identifier, the medium of the identifier.
     * Must be 'email'.
     * Deprecated in favour of [identifier].
     */
    @Deprecated(message = "Deprecated in favour of `identifier`")
    val medium: String? = null,

    /**
     * Third party identifier for the user.
     * Deprecated in favour of [identifier].
     */
    @Deprecated(message = "Deprecated in favour of `identifier`")
    val address: String? = null,

    /**
     * Required when [type] is `m.login.password`.
     * The user's password.
     */
    val password: String? = null,

    /**
     * Required when [type] is `m.login.token`.
     * Part of [Token-based](https://matrix.org/docs/spec/client_server/r0.5.0#token-based) login.
     */
    val token: String? = null,

    @SerialName("device_id")
    val deviceId: String? = null,

    /**
     * A display name to assign to the newly-created device.
     * Ignored if [deviceId] corresponds to a known device.
     */
    val initialDeviceDisplayName: String? = null
)

@Serializable
data class LoginResponse(
    /**
     * The fully-qualified Matrix ID that has been registered.
     */
    @SerialName("user_id")
    val userId: String? = null,

    /**
     * An access token for the account.
     * This access token can then be used to authorize other requests.
     */
    @SerialName("access_token")
    val accessToken: String? = null,

    /**
     * The server_name of the homeserver on which the account has been registered.
     *
     * Deprecated. Clients should extract the server_name from user_id (by splitting at the first colon) if they require it.
     * Note also that homeserver is not spelt this way.
     */
    @Deprecated("Clients should extract the server_name from user_id (by splitting at the first colon) if they require it")
    @SerialName("home_server")
    val homeServer: String? = null,

    /**
     * ID of the logged-in device.
     * Will be the same as the corresponding parameter in the request, if one was specified.
     */
    @SerialName("device_id")
    val deviceId: String? = null,

    /**
     * Optional client configuration provided by the server.
     * If present, clients SHOULD use the provided object to reconfigure themselves, optionally validating the URLs within.
     * This object takes the same form as the one returned from .well-known autodiscovery.
     */
    @SerialName("well_known")
    val wellKnown: DiscoveryInformation? = null
)

@Serializable
enum class RegistrationKind {
    @SerialName("guest")
    GUEST,
    @SerialName("user")
    USER;
}

@Serializable
data class RegisterRequest(
    /**
     * Additional authentication information for the user-interactive authentication API.
     * Note that this information is not used to define how the registered user should be authenticated,
     * but is instead used to authenticate the `register` call itself.
     */
    val auth: AuthenticationData? = null,

    /**
     * If true, the server binds the email used for authentication to the Matrix ID with the identity server.
     */
    @SerialName("bind_email")
    val bindEmail: Boolean? = null,

    /**
     * If true, the server binds the phone number used for authentication to the Matrix ID with the identity server.
     */
    @SerialName("bind_msisdn")
    val bindMsisdn: Boolean? = null,

    /**
     * The basis for the localpart of the desired Matrix ID.
     * If omitted, the homeserver MUST generate a Matrix ID local part.
     */
    val username: String? = null,

    /**
     * The desired password for the account.
     */
    val password: String? = null,

    /**
     * ID of the client device.
     * If this does not correspond to a known client device,
     * a new device will be created.
     * The server will auto-generate a `device_id` if this is not specified.
     */
    @SerialName("device_id")
    val deviceId: String? = null,

    /**
     * A display name to assign to the newly-created device.
     * Ignored if `device_id` corresponds to a known device.
     */
    @SerialName("initial_device_display_name")
    val initialDeviceDisplayName: String? = null,

    /**
     * If true, an `access_token` and `device_id` should not be returned from this call,
     * therefore preventing an automatic login.
     * Defaults to false.
     */
    @SerialName("inhibit_login")
    val inhibitLogin: Boolean? = null
)

@Serializable
data class RegisterResponse(
    /**
     * The fully-qualified Matrix user ID (MXID) that has been registered.
     *
     * Any user ID returned by this API must conform to the grammar given in the
     * [Matrix specification](https://matrix.org/docs/spec/appendices.html#user-identifiers).
     */
    @SerialName("user_id")
    val userId: String,

    /**
     * An access token for the account.
     * This access token can then be used to authorize other requests.
     * Required if the [RegisterRequest.inhibitLogin] option is false.
     */
    @SerialName("access_token")
    val accessToken: String? = null,

    /**
     * The server_name of the homeserver on which the account has been registered.
     *
     * Deprecated. Clients should extract the `server_name` from `user_id`
     * (by splitting at the first colon) if they require it.
     * Note also that `homeserver` is not spelt this way.
     */
    @SerialName("home_server")
    @Deprecated("Clients should extract the `server_name` from `user_id.")
    val homeServer: String? = null,

    /**
     * ID of the registered device.
     * Will be the same as the corresponding parameter in the request, if one was specified.
     * Required if the `inhibit_login` option is false.
     */
    @SerialName("device_id")
    val deviceId: String? = null
)

@Serializable
data class ChangePasswordRequest(
    /**
     * The new password for the account.
     */
    @SerialName("new_password")
    val newPassword: String,

    /**
     * Additional authentication information for the user-interactive authentication API.
     */
    val auth: AuthenticationData? = null
)

@Serializable
data class DeactivateRequest(
    /**
     * Additional authentication information for the user-interactive authentication API.
     */
    val auth: AuthenticationData? = null,

    /**
     * The identity server to unbind all of the user's 3PIDs from.
     * If not provided, the homeserver MUST use the `id_server` that was originally use to bind each identifier.
     * If the homeserver does not know which `id_server` that was,
     * it must return an `id_server_unbind_result` of no-support.
     */
    val idServer: String? = null
)

@Serializable
data class DeactivateResponse(
    /**
     * An indicator as to whether or not the homeserver was able to unbind the user's 3PIDs from the identity server(s).
     * `success` indicates that all identifiers have been unbound from the identity server while
     * `no-support` indicates that one or more identifiers failed to unbind due to the identity server
     * refusing the request or the homeserver being unable to determine an identity server to unbind from.
     * This must be success if the homeserver has no identifiers to unbind for the user.
     * One of: ["success", "no-support"]
     */
    @SerialName("id_server_unbind_result")
    val idServerUnbindResult: String
)


@Serializable
data class GetDevicesResponse(
    /**
     * A list of all registered devices for this user.
     */
    val devices: List<Device>
)

@Serializable
data class EncryptedFile(
    /**
     * The URL to the file.
     */
    val url: String,

    /**
     * A [JSON Web Key](https://tools.ietf.org/html/rfc7517#appendix-A.3) object.
     */
    val key: JWK,

    /**
     * The Initialisation Vector used by AES-CTR, encoded as unpadded base64.
     */
    @SerialName("iv")
    val initialisationVector: String,

    /**
     * A map from an algorithm name to a hash of the ciphertext, encoded as unpadded base64.
     * Clients should support the SHA-256 hash, which uses the key sha256.
     */
    val hashes: Map<String, String>,

    /**
     * Version of the encrypted attachments protocol.
     * Must be `v2`.
     */
    @SerialName("v")
    val version: String
)

@Serializable
data class JWK(
    /**
     * Key type. Must be `oct`.
     */
    @SerialName("kty")
    val keyType: String,

    /**
     * Key operations.
     * Must at least contain `encrypt` and `decrypt`.
     */
    @SerialName("key_ops")
    val keyOps: List<String>,

    /**
     * Algorithm. Must be `A256CTR`.
     */
    @SerialName("alg")
    val algorithm: String,

    /**
     * The key, encoded as urlsafe unpadded base64.
     */
    @SerialName("k")
    val key: String,

    /**
     * Extractable. Must be true. This is a [W3C extension](https://w3c.github.io/webcrypto/#iana-section-jwk).
     */
    @SerialName("ext")
    val extractable: Boolean
)

@Serializable
data class SendToDeviceRequest(
    val messages: Map<String, Map<String, @ContextualSerialization Any>>? = null
)

@Serializable
data class TurnServerResponse(
    /**
     * The username to use.
     */
    val username: String,

    /**
     * The password to use.
     */
    val password: String,

    /**
     * A list of TURN URIs.
     */
    val uris: List<String>,

    /**
     * The time-to-live in seconds.
     */
    val ttl: Long
)

@Serializable
data class TypingRequest(
    /**
     * Whether the user is typing or not.
     * If `false`, the `timeout` key can be omitted.
     */
    val typing: Boolean,

    /**
     * The length of time in milliseconds to mark this user as typing.
     */
    val timeout: Long? = null
)

@Serializable
data class ReadMarkersRequest(
    /**
     * The event ID the read marker should be located at.
     * The event MUST belong to the room.
     */
    @SerialName("m.fully_read")
    val fullyRead: String,

    /**
     * The event ID to set the read receipt location at.
     * This is equivalent to calling `/receipt/m.read/$elsewhere:example.org` and is provided here to save that extra call.
     */
    @SerialName("m.read")
    val read: String? = null
)

@Serializable
data class SetPresenceRequest(
    /**
     * The new presence state. One of: ["online", "offline", "unavailable"]
     */
    val presence: Presence,

    /**
     * The status message to attach to this state.
     */
    @SerialName("status_msg")
    val statusMsg: String? = null
)

@Serializable
data class GetPresenceResponse(
    /**
     * This user's presence. One of: ["online", "offline", "unavailable"]
     */
    val presence: Presence,

    /**
     * The length of time in milliseconds since an action was performed by this user.
     */
    @SerialName("last_active_ago")
    val lastActiveAgo: Long? = null,

    /**
     * The state message for this user if one was set.
     */
    @SerialName("status_msg")
    val statusMessage: String? = null,

    /**
     * Whether the user is currently active.
     */
    @SerialName("currently_active")
    val currentlyActive: Boolean? = null
)

@Serializable
data class Get3PidsResponse(
    val threepids: List<ThirdPartyIdentifier>
)

@Serializable
enum class Medium {
    @SerialName("email")
    EMAIL,
    @SerialName("msisdn")
    MSISDN;
}

@Serializable
data class Add3PidRequest(
    /**
     * The third party credentials to associate with the account.
     */
    @SerialName("three_pid_creds")
    val threePidCredentials: ThreePidCredentials,

    /**
     * Whether the homeserver should also bind this third party identifier to the account's Matrix ID with the passed identity server.
     * Default: false.
     */
    val bind: Boolean? = null
)

@Serializable
data class ThreePidCredentials(
    /**
     * The client secret used in the session with the identity server.
     */
    @SerialName("client_secret")
    val clientSecret: String,

    /**
     * The identity server to use.
     */
    @SerialName("id_server")
    val idServer: String,

    /**
     * The session identifier given by the identity server.
     */
    val sid: String
)

@Serializable
data class Remove3PidRequest(
    /**
     * The identity server to unbind from.
     * If not provided, the homeserver MUST use the `idServer` the identifier was added through.
     * If the homeserver does not know the original `id_server`, it MUST return a `id_server_unbind_result` of no-support.
     */
    @SerialName("id_server")
    val idServer: String? = null,

    /**
     * The medium of the third party identifier being removed.
     * One of: ["email", "msisdn"]
     */
    val medium: Medium,

    /**
     * The third party address being removed.
     */
    val address: String
)

@Serializable
data class Remove3PidResponse(
    /**
     * An indicator as to whether or not the homeserver was able to unbind the 3PID from the identity server.
     * `success` indicates that the indentity server has unbound the identifier whereas `no-support` indicates
     * that the identity server refuses to support the request or the homeserver was not able to determine an
     * identity server to unbind from.
     * One of: ["no-support", "success"]
     */
    @SerialName("id_server_unbind_result")
    val idServerUnbindResult: IdServerUnbindResult
)

@Serializable
enum class IdServerUnbindResult {
    @SerialName("no-support")
    NO_SUPPORT,
    @SerialName("success")
    SUCCESS;
}

@Serializable
data class WhoAmIResponse(
    /**
     * The user id that owns the access token.
     */
    @SerialName("user_id")
    val userId: String
)

@Serializable
data class Versions(
    /**
     * The supported versions.
     */
    val versions: List<String>,

    /**
     * Experimental features the server supports.
     * Features not listed here, or the lack of this property all together,
     * indicate that a feature is not supported.
     */
    @SerialName("unstable_features")
    val unstableFeatures: Map<String, Boolean>? = null
)

@Serializable
data class GetCapabilitiesResponse(
    /**
     * The custom capabilities the server supports, using the Java package naming convention.
     */
    val capabilities: Capabilities
)

@Serializable
data class Capabilities(
    /**
     * Capability to indicate if the user can change their password.
     */
    @SerialName("m.change_password")
    val changePassword: ChangePasswordCapability? = null,

    /**
     * The room versions the server supports.
     */
    @SerialName("m.room_versions")
    val roomVersions: RoomVersionsCapability? = null
)

@Serializable
data class ChangePasswordCapability(
    /**
     * True if the user can change their password, false otherwise.
     */
    val enabled: Boolean
)

@Serializable
data class RoomVersionsCapability(
    /**
     * The default room version the server is using for new rooms.
     */
    val default: String,

    /**
     * A detailed description of the room versions the server supports.
     */
    val available: Map<String, RoomVersionStability>
)

/**
 * The stability of the room version.
 */
@Serializable
enum class RoomVersionStability {
    @SerialName("stable")
    STABLE,
    @SerialName("unstable")
    UNSTABLE;
}

@Serializable
enum class PushRuleKind {
    @SerialName("override")
    OVERRIDE,
    @SerialName("underride")
    UNDERRIDE,
    @SerialName("sender")
    SENDER,
    @SerialName("room")
    ROOM,
    @SerialName("content")
    CONTENT;
}

@Serializable
data class GetPushRulesResponse(
    val global: RuleSet
)

@Serializable
enum class PushRuleAction {
    @SerialName("notify")
    NOTIFY,
    @SerialName("dont_notify")
    DONT_NOTIFY,
    @SerialName("coalesce")
    COALESCE,
    @SerialName("set_tweak")
    SET_TWEAK;

}

@Serializable
data class SetPushRuleRequest(
    /**
     * The action(s) to perform when the conditions for this rule are met.
     */
    val actions: List<PushRuleAction>,

    /**
     * The conditions that must hold true for an event in order for a rule to be applied to an event.
     * A rule with no conditions always matches.
     * Only applicable to ``underride`` and ``override`` rules.
     */
    val conditions: List<PushCondition>? = null,

    /**
     * Only applicable to ``content`` rules. The glob-style pattern to match against.
     */
    val pattern: String? = null
)

@Serializable
data class PushRuleEnabled(
    /**
     * Whether the push rule is enabled or not.
     */
    val enabled: Boolean
)

@Serializable
data class PushRuleActions(
    /**
     * The action(s) to perform for this rule.
     */
    val actions: List<String>
)

@Serializable
data class Pushers(
    /**
     * An array containing the current pushers for the user.
     */
    val pushers: List<Pusher>
)

@Serializable
data class Pusher(
    /**
     * This is a unique identifier for this pusher. See ``/set`` for more detail.
     * Max length, 512 bytes.
     */
    @SerialName("pushkey")
    val pushKey: String,

    /**
     * The kind of pusher. ``"http"`` is a pusher that sends HTTP pokes.
     */
    val kind: String,

    /**
     * This is a reverse-DNS style identifier for the application.
     * Max length, 64 chars.
     */
    @SerialName("app_id")
    val appId: String,

    /**
     * A string that will allow the user to identify what application owns this pusher.
     */
    @SerialName("app_display_name")
    val appDisplayName: String,

    /**
     * A string that will allow the user to identify what device owns this pusher.
     */
    @SerialName("device_display_name")
    val deviceDisplayName: String,

    /**
     * This string determines which set of device specific rules this pusher executes.
     */
    @SerialName("profile_tag")
    val profileTag: String? = null,

    /**
     * The preferred language for receiving notifications (e.g. 'en' or 'en-US')
     */
    val lang: String,

    /**
     * A dictionary of information for the pusher implementation itself.
     */
    val data: Data
) {
    @Serializable
    data class Data(
        /**
         * Required if ``kind`` is ``http``.
         * The URL to use to send notifications to.
         */
        val url: String? = null,

        /**
         * The format to use when sending notifications to the Push Gateway.
         */
        val format: String? = null
    )
}

@Serializable
data class UploadKeysRequest(
    /**
     * Identity keys for the device.
     * May be absent if no new identity keys are required.
     */
    @SerialName("device_keys")
    val deviceKeys: DeviceKeys? = null,

    /**
     * One-time public keys for "pre-key" messages.
     * The names of the properties should be in the format ``<algorithm>:<key_id>``.
     * The format of the key is determined by the `key algorithm <#key-algorithms>`_.
     *
     * May be absent if no new one-time keys are required.
     */
    @SerialName("one_time_keys")
    val oneTimeKeys: Map<String, @Serializable(OneTimeKeySerializer::class) Any>? = null
)

@Serializable
data class KeyObject(
    /**
     * The key, encoded using unpadded base64.
     */
    val key: String,

    /**
     * Signature for the device. Mapped from user ID to signature object.
     */
    val signatures: Map<String, Map<String, String>>
)

object OneTimeKeySerializer : KSerializer<Any> {
    override val descriptor = SerialDescriptor("OneTimeKey")

    override fun deserialize(decoder: Decoder): Any {
        if (decoder !is JsonInput) throw SerializationException("This class can be loaded only by Json")

        val tree = decoder.decodeJson()
        return if (tree is JsonObject) {
            decoder.json.fromJson(KeyObject.serializer(), tree)
        } else if (tree is JsonLiteral && tree.isString) {
            tree.content
        } else {
            throw SerializationException("Expected JsonObject or JsonLiteral")
        }
    }

    override fun serialize(encoder: Encoder, value: Any) {
        return when (value) {
            is KeyObject -> encoder.encode(KeyObject.serializer(), value)
            is String -> encoder.encodeString(value)
            else -> TODO("Only KeyObject and String supported")
        }
    }
}

@Serializable
data class UploadKeysResponse(
    /**
     * For each key algorithm, the number of unclaimed one-time keys
     * of that type currently held on the server for this device.
     */
    @SerialName("one_time_key_counts")
    val oneTimeKeyCounts: Map<String, Int>
)

@Serializable
data class QueryKeysRequest(
    /**
     * The time (in milliseconds) to wait when downloading keys from remote servers.
     * 10 seconds is the recommended default.
     */
    val timeout: Long? = null,

    /**
     * The keys to be downloaded.
     * A map from user ID, to a list of device IDs,
     * or to an empty list to indicate all devices for the corresponding user.
     */
    @SerialName("device_keys")
    val deviceKeys: Map<String, List<String>>,

    /**
     * If the client is fetching keys as a result of a device update received in a sync request,
     * this should be the 'since' token of that sync request, or any later sync token.
     * This allows the server to ensure its response contains the keys advertised by the notification in that sync.
     */
    val token: String? = null
)

@Serializable
data class QueryKeysResponse(
    /**
     * If any remote homeservers could not be reached, they are recorded here.
     * The names of the properties are the names of the unreachable servers.
     *
     * If the homeserver could be reached, but the user or device was unknown,
     * no failure is recorded.
     * Instead, the corresponding user or device is missing from the ``device_keys`` result.
     */
    val failures: JsonObject,

    /**
     * Information on the queried devices.
     * A map from user ID, to a map from device ID to device information.
     * For each device, the information returned will be the same as uploaded
     * via ``/keys/upload``, with the addition of an ``unsigned`` property.
     */
    @SerialName("device_keys")
    val deviceKeys: Map<String, Map<String, DeviceKeys>>
)

@Serializable
data class UnsignedDeviceInfo(
    /**
     * The display name which the user set on the device.
     */
    @SerialName("device_display_name")
    val deviceDisplayName: String? = null
)

@Serializable
data class ClaimKeysRequest(
    /**
     * The time (in milliseconds) to wait when downloading keys from remote servers.
     * 10 seconds is the recommended default.
     */
    val timeout: Long? = null,

    /**
     * The keys to be claimed.
     * A map from user ID, to a map from device ID to algorithm name.
     */
    @SerialName("one_time_keys")
    val oneTimeKeys: Map<String, Map<String, String>>
)

@Serializable
data class ClaimKeysResponse(
    /**
     * If any remote homeservers could not be reached, they are recorded here.
     * The names of the properties are the names of the unreachable servers.
     *
     * If the homeserver could be reached, but the user or device was unknown, no failure is recorded.
     * Instead, the corresponding user or device is missing from the ``one_time_keys`` result.
     */
    val failures: JsonObject? = null,

    /**
     * One-time keys for the queried devices.
     * A map from user ID, to a map from devices to a map from ``<algorithm>:<key_id>`` to the key object.
     * See the `key algorithms <#key-algorithms>`_ section for information on the Key Object format.
     */
    @SerialName("one_time_keys")
    val oneTimeKeys: Map<String, Map<String, Map<String, @Serializable(with = OneTimeKeySerializer::class) Any>>>
)

/**
 * The list of users who updated their devices.
 */
@Serializable
data class KeyChangesResponse(
    /**
     * The Matrix User IDs of all users who updated their device identity keys.
     */
    val changed: List<String>,

    /**
     * The Matrix User IDs of all users who may have left all the
     * end-to-end encrypted rooms they previously shared with the user.
     */
    val left: List<String>
)
