package io.github.matrixkt.apis

import io.github.matrixkt.models.*
import io.github.matrixkt.models.events.MatrixEvent
import io.github.matrixkt.models.events.Membership
import kotlinx.serialization.json.JsonObject

interface RoomApi {
    /**
     * Create a new room with various configuration options.
     *
     * The server MUST apply the normal state resolution rules when creating the new room, including checking power levels for each event. It MUST apply the events implied by the request in the following order:
     *
     * - A default `m.room.power_levels` event, giving the room creator (and not other members) permission to send state events. Overridden by the `power_level_content_override` parameter.
     * - Events set by the preset. Currently these are the `m.room.join_rules`, `m.room.history_visibility`, and `m.room.guest_access` state events.
     * - Events listed in `initial_state`, in the order that they are listed.
     * - Events implied by `name` and `topic` (`m.room.name` and `m.room.topic` state events).
     * - Invite events implied by `invite` and `invite_3pid` (`m.room.member` with `membership: invite` and `m.room.third_party_invite`).
     *
     * The available presets do the following with respect to room state:
     *
     * ```
     * | Preset               | join_rules | history_visibility | guest_access | Other                                                            |
     * |----------------------|------------|--------------------|--------------|------------------------------------------------------------------|
     * | private_chat         | invite     | shared             | can_join     |                                                                  |
     * | trusted_private_chat | invite     | shared             | can_join     | All invitees are given the same power level as the room creator. |
     * | public_chat          | public     | shared             | forbidden    |                                                                  |
     * ```
     *
     * The server will create a `m.room.create` event in the room with the requesting user as the creator, alongside other keys provided in the `creation_content`.
     *
     * **Rate-limited**: No.
     *
     * **Requires auth**: Yes.
     *
     * @return The created room's ID.
     */
    suspend fun createRoom(params: CreateRoomRequest): String

    /**
     * Create a new mapping from room alias to room ID.
     *
     * **Rate-limited**: No.
     *
     * **Requires auth**: Yes.
     *
     * @param[roomAlias] The room alias to set.
     * @param[roomId] The room ID to set.
     */
    suspend fun setRoomAlias(roomAlias: String, roomId: String)

    /**
     * Requests that the server resolve a room alias to a room ID.
     *
     * The server will use the federation API to resolve the alias if the
     * domain part of the alias does not correspond to the server's own domain.
     *
     * **Rate-limited**: No.
     *
     * **Requires auth**: Yes.
     *
     * @param[roomAlias] The room alias.
     */
    suspend fun getRoomIdByAlias(roomAlias: String): ResolveRoomAliasResponse

    /**
     * Remove a mapping of room alias to room ID.
     *
     * Servers may choose to implement additional access control checks here,
     * for instance that room aliases can only be deleted by their creator or a server administrator.
     *
     * **Rate-limited**: No.
     *
     * **Requires auth**: Yes.
     *
     * @param[roomAlias] The room alias to remove.
     */
    suspend fun deleteRoomAlias(roomAlias: String)

    /**
     * This API returns a list of the user's current rooms.
     *
     * **Rate-limited**: No.
     *
     * **Requires auth**: Yes.
     *
     * @return The ID of each room in which the user has joined membership.
     */
    suspend fun getJoinedRooms(): List<String>

    /**
     * Note that there are two forms of this API, which are documented separately.
     * This version of the API requires that the inviter knows the Matrix identifier of the invitee.
     * The other is documented in the third party invites section.
     *
     * This API invites a user to participate in a particular room.
     * They do not start participating in the room until they actually join the room.
     *
     * Only users currently in a particular room can invite other users to join that room.
     *
     * If the user was invited to the room, the homeserver will append a `m.room.member` event to the room.
     *
     * **Rate-limited**: Yes.
     *
     * **Requires auth**: Yes.
     *
     * @param[roomId] The room identifier (not alias) to which to invite the user.
     * @param[userId] The fully qualified user ID of the invitee.
     */
    suspend fun inviteUser(roomId: String, userId: String)

    /**
     * Note that this API requires a room ID, not alias.
     * `/join/{roomIdOrAlias}` exists if you have a room alias.
     *
     * This API starts a user participating in a particular room,
     * if that user is allowed to participate in that room.
     * After this call, the client is allowed to see all current state events in the room,
     * and all subsequent events associated with the room until the user leaves the room.
     *
     * After a user has joined a room,
     * the room will appear as an entry in the response of the `/initialSync` and `/sync` APIs.
     *
     * If a `third_party_signed` was supplied,
     * the homeserver must verify that it matches a pending `m.room.third_party_invite` event in the room,
     * and perform key validity checking if required by the event.
     *
     * **Rate-limited**: Yes.
     *
     * **Requires auth**: Yes.
     *
     * @param[roomId] The room identifier (not alias) to join.
     * @return The joined room ID.
     */
    suspend fun joinRoomById(roomId: String, params: JoinRoomRequest): String

    /**
     * Note that this API takes either a room ID or alias, unlike `/room/{roomId}/join`.
     *
     * This API starts a user participating in a particular room,
     * if that user is allowed to participate in that room.
     * After this call, the client is allowed to see all current state events in the room,
     * and all subsequent events associated with the room until the user leaves the room.
     *
     * After a user has joined a room,
     * the room will appear as an entry in the response of the `/initialSync` and `/sync` APIs.
     *
     * If a `third_party_signed` was supplied,
     * the homeserver must verify that it matches a pending `m.room.third_party_invite` event in the room,
     * and perform key validity checking if required by the event.
     *
     * **Rate-limited**: Yes.
     *
     * **Requires auth**: Yes.
     *
     * @param[roomIdOrAlias] The room identifier or alias to join.
     * @param[servers] The servers to attempt to join the room through. One of the servers must be participating in the room.
     * @return The joined room ID.
     */
    suspend fun joinRoom(roomIdOrAlias: String, servers: List<String>, params: JoinRoomRequest): String

    /**
     * This API stops a user participating in a particular room.
     *
     * If the user was already in the room, they will no longer be able to see new events in the room.
     * If the room requires an invite to join, they will need to be re-invited before they can re-join.
     *
     * If the user was invited to the room, but had not joined, this call serves to reject the invite.
     *
     * The user will still be allowed to retrieve history from the room which they were previously allowed to see.
     *
     * **Rate-limited**: Yes.
     *
     * **Requires auth**: Yes.
     *
     * @param[roomId] The room identifier to leave.
     */
    suspend fun leaveRoom(roomId: String)

    /**
     * This API stops a user remembering about a particular room.
     *
     * In general, history is a first class citizen in Matrix.
     * After this API is called, however, a user will no longer be able to retrieve history for this room.
     * If all users on a homeserver forget a room, the room is eligible for deletion from that homeserver.
     *
     * If the user is currently joined to the room, they must leave the room before calling this API.
     *
     * **Rate-limited**: Yes.
     *
     * **Requires auth**: Yes.
     *
     * @param[roomId] The room identifier to forget.
     */
    suspend fun forgetRoom(roomId: String)

    /**
     * Kick a user from the room.
     *
     * The caller must have the required power level in order to perform this operation.
     *
     * Kicking a user adjusts the target member's membership state to be leave with an optional reason.
     * Like with other membership changes,
     * a user can directly adjust the target member's state by making a request
     * to `/rooms/<room id>/state/m.room.member/<user id>`.
     *
     * **Rate-limited**: No.
     *
     * **Requires auth**: Yes.
     *
     * @param[roomId] The room identifier (not alias) from which the user should be kicked.
     */
    suspend fun kick(roomId: String, params: KickRequest)

    /**
     * Ban a user in the room. If the user is currently in the room, also kick them.
     *
     * When a user is banned from a room, they may not join it or be invited to it until they are unbanned.
     *
     * The caller must have the required power level in order to perform this operation.
     *
     * **Rate-limited**: No.
     *
     * **Requires auth**: Yes.
     *
     * @param[roomId] The room identifier (not alias) from which the user should be banned.
     */
    suspend fun ban(roomId: String, params: BanRequest)

    /**
     * Unban a user from the room.
     * This allows them to be invited to the room,
     * and join if they would otherwise be allowed to join according to its join rules.
     *
     * The caller must have the required power level in order to perform this operation.
     *
     * **Rate-limited**: No.
     *
     * **Requires auth**: Yes.
     *
     * @param[roomId] The room identifier (not alias) from which the user should be unbanned.
     * @param[userId] The fully qualified user ID of the user being unbanned.
     */
    suspend fun unban(roomId: String, userId: String)

    /**
     * Lists the public rooms on the server.
     *
     * This API returns paginated responses.
     * The rooms are ordered by the number of joined members, with the largest rooms first.
     *
     * **Rate-limited**: No.
     *
     * **Requires auth**: No.
     *
     * @param[limit] Limit the number of results returned.
     * @param[since] A pagination token from a previous request, allowing clients to get the next (or previous) batch of rooms. The direction of pagination is specified solely by which token is supplied, rather than via an explicit flag.
     * @param[server] The server to fetch the public room lists from. Defaults to the local server.
     */
    suspend fun getPublicRooms(limit: Int? = null, since: String? = null, server: String? = null): PublicRoomsResponse

    /**
     * Lists the public rooms on the server, with optional filter.
     *
     * This API returns paginated responses.
     * The rooms are ordered by the number of joined members, with the largest rooms first.
     *
     * **Rate-limited**: No.
     *
     * **Requires auth**: Yes.
     *
     * @param[server] The server to fetch the public room lists from. Defaults to the local server.
     */
    suspend fun queryPublicRooms(server: String? = null, params: SearchPublicRoomsRequest): PublicRoomsResponse

    /**
     * Upgrades the given room to a particular room version.
     *
     * **Rate-limited**: No.
     *
     * **Requires auth**: Yes.
     *
     * @param[roomId] The ID of the room to upgrade.
     * @param[newVersion] The new version for the room.
     * @return The ID of the new room.
     */
    suspend fun upgradeRoom(roomId: String, newVersion: String): String

    /**
     * This tells the server that the user is typing for the next N milliseconds where N is the value specified in the `timeout` key.
     * Alternatively, if `typing` is `false`, it tells the server that the user has stopped typing.
     *
     * **Rate-limited**: Yes.
     *
     * **Requires auth**: Yes.
     *
     * @param[userId] The user who has started to type.
     * @param[roomId] The room in which the user is typing.
     */
    suspend fun setTyping(userId: String, roomId: String, params: TypingRequest)

    /**
     * This API updates the marker for the given receipt type to the event ID specified.
     *
     * **Rate-limited**: Yes.
     *
     * **Requires auth**: Yes.
     *
     * @param[roomId] The room in which to send the event.
     * @param[receiptType] The type of receipt to send. One of: ["m.read"]
     * @param[eventId] The event ID to acknowledge up to.
     */
    suspend fun postReceipt(roomId: String, receiptType: String, eventId: String)

    /**
     * Sets the position of the read marker for a given room, and optionally the read receipt's location.
     *
     * **Rate-limited**: Yes.
     *
     * **Requires auth**: Yes.
     *
     * @param[roomId] The room ID to set the read marker in for the user.
     */
    suspend fun setReadMarker(roomId: String, params: ReadMarkersRequest)

    /**
     * Get the list of members for this room.
     *
     * **Rate-limited**: No.
     *
     * **Requires auth**: Yes.
     *
     * @param[roomId] The room to get the member events for.
     * @param[at] The point in time (pagination token) to return members for in the room.
     * This token can be obtained from a prev_batch token returned for each room by the sync API.
     * Defaults to the current state of the room, as determined by the server.
     * @param[membership] The kind of membership to filter for.
     * Defaults to no filtering if unspecified.
     * When specified alongside not_membership, the two parameters create an 'or' condition: either the
     * membership is the same as membership or is not the same as not_membership.
     * One of: ["join", "invite", "leave", "ban"]
     * @param[notMembership] The kind of membership to exclude from the results.
     * Defaults to no filtering if unspecified. One of: ["join", "invite", "leave", "ban"]
     */
    suspend fun getMembersByRoom(roomId: String, at: String? = null, membership: Membership? = null, notMembership: Membership? = null): GetMembersResponse

    /**
     * This API returns a map of MXIDs to member info objects for members of the room.
     * The current user must be in the room for it to work, unless it is
     * an Application Service in which case any of the AS's users must be in the room.
     * This API is primarily for Application Services and should be faster to respond
     * than `/members` as it can be implemented more efficiently on the server.
     *
     * **Rate-limited**: No.
     *
     * **Requires auth**: Yes.
     *
     * @param[roomId] The room to get the members of.
     * @return A map from user ID to a [RoomMember] object.
     */
    suspend fun getJoinedMembersByRoom(roomId: String): Map<String, RoomMember>

    /**
     * This API returns a list of message and state events for a room.
     * It uses pagination query parameters to paginate history in the room.
     *
     * Note: This endpoint supports lazy-loading of room member events.
     * See [Lazy-loading room members](https://matrix.org/docs/spec/client_server/r0.5.0#lazy-loading-room-members) for more information.
     *
     * **Rate-limited**: No.
     *
     * **Requires auth**: Yes.
     *
     * @param[roomId] The room to get events from.
     * @param[from] The token to start returning events from.
     * This token can be obtained from a `prev_batch` token returned for each room by the sync API,
     * or from a `start` or `end` token returned by a previous request to this endpoint.
     * @param[to] The token to stop returning events at.
     * This token can be obtained from a `prev_batch` token returned for each room by the sync endpoint,
     * or from a `start` or `end` token returned by a previous request to this endpoint.
     * @param[dir] The direction to return events from. One of: ["b", "f"]
     * @param[limit] The maximum number of events to return. Default: 10.
     * @param[filter] A JSON RoomEventFilter to filter returned events with.
     */
    suspend fun getRoomEvents(roomId: String, from: String, to: String? = null, dir: Direction, limit: Long? = null, filter: String? = null): MessagesResponse

    /**
     * State events can be sent using this endpoint.
     * These events will be overwritten if <room id>, <event type> and <state key> all match.
     *
     * Requests to this endpoint **cannot use transaction IDs** like other PUT paths because they cannot be differentiated from the state_key.
     * Furthermore, POST is unsupported on state paths.
     *
     * The body of the request should be the content object of the event; the fields in this object will vary depending on the type of event.
     * See Room Events for the m. event specification.
     *
     * **Rate-limited**: No.
     *
     * **Requires auth**: Yes.
     *
     * @param[roomId] The room to set the state in
     * @param[eventType] The type of event to send.
     * @param[stateKey] The state_key for the state to send.
     * Defaults to the empty string.
     * When an empty string, the trailing slash on this endpoint is optional.
     * @return A unique identifier for the event.
     */
    suspend fun setRoomStateWithKey(roomId: String, eventType: String, stateKey: String, eventContent: Any): String

    /**
     * Looks up the contents of a state event in a room.
     * If the user is joined to the room then the state is taken from the current state of the room.
     * If the user has left the room then the state is taken from the state of the room when they left.
     *
     * **Rate-limited**: No.
     *
     * **Requires auth**: Yes.
     *
     * @param[roomId] The room to look up the state in.
     * @param[eventType] The type of state to look up.
     * @param[stateKey] The key of the state to look up. Defaults to an empty string. When an empty string, the trailing slash on this endpoint is optional.
     */
    suspend fun getRoomStateWithKey(roomId: String, eventType: String, stateKey: String): JsonObject

    /**
     * This endpoint is used to send a message event to a room.
     * Message events allow access to historical events and pagination,
     * making them suited for "once-off" activity in a room.
     *
     * The body of the request should be the content object of the event;
     * the fields in this object will vary depending on the type of event.
     * See Room Events for the m. event specification.
     *
     * **Rate-limited**: No.
     *
     * **Requires auth**: Yes.
     *
     * @param[roomId] The room to send the event to.
     * @param[eventType] The type of event to send.
     * @param[txnId] The transaction ID for this event.
     * Clients should generate an ID unique across requests with the same access token;
     * it will be used by the server to ensure idempotency of requests.
     */
    suspend fun sendMessage(roomId: String, eventType: String, txnId: String, content: JsonObject): String

    /**
     * Strips all information out of an event which isn't critical to the
     * integrity of the server-side representation of the room.
     *
     * This cannot be undone.
     *
     * Users may redact their own events, and any user with a power level greater than
     * or equal to the redact power level of the room may redact events there.
     *
     * **Rate-limited**: No.
     *
     * **Requires auth**: Yes.
     *
     * @param[roomId] The room from which to redact the event.
     * @param[eventId] The ID of the event to redact.
     * @param[txnId] The transaction ID for this event.
     * Clients should generate a unique ID; it will be used by the server to ensure idempotency of requests.
     * @param[reason] The reason for the event being redacted.
     * @return A unique identifier for the event.
     */
    suspend fun redactEvent(roomId: String, eventId: String, txnId: String, reason: String? = null): String

    /**
     * Get a single event based on `roomId/eventId`.
     * You must have permission to retrieve this event e.g. by being a member in the room for this event.
     *
     * **Rate-limited**: No.
     *
     * **Requires auth**: Yes.
     *
     * @param[roomId] The ID of the room the event is in.
     * @param[eventId] The event ID to get.
     */
    suspend fun getOneRoomEvent(roomId: String, eventId: String): MatrixEvent

    /**
     * Get the state events for the current state of a room.
     *
     * **Rate-limited**: No.
     *
     * **Requires auth**: Yes.
     *
     * @param[roomId] The room to look up the state for.
     */
    suspend fun getRoomState(roomId: String): List<MatrixEvent>


    /**
     * Gets the visibility of a given room on the server's public room directory.
     *
     * **Rate-limited**: No.
     *
     * **Requires auth**: No.
     *
     * @param[roomId] The room ID.
     * @return The visibility of the room in the directory. One of: ["private", "public"]
     */
    suspend fun getVisibility(roomId: String): RoomVisibility

    /**
     * Sets the visibility of a given room in the server's public room directory.
     *
     * Servers may choose to implement additional access control checks here,
     * for instance that room visibility can only be changed by the room creator or a server administrator.
     *
     * **Rate-limited**: No.
     *
     * **Requires auth**: Yes.
     *
     * @param[roomId] The room ID.
     * @param[visibility] The new visibility setting for the room. Defaults to 'public'. One of: ["private", "public"]
     */
    suspend fun setVisibility(roomId: String, visibility: RoomVisibility)
}

// GET /_matrix/client/r0/rooms/{roomId}/context/{eventId} -> getEventContext
// POST /_matrix/client/r0/rooms/{roomId}/invite -> inviteBy3PID
// POST /_matrix/client/r0/rooms/{roomId}/report/{eventId} -> reportContent
// GET /_matrix/client/r0/rooms/{roomId}/initialSync -> roomInitialSync


// PUT /_matrix/client/r0/directory/list/appservice/{networkId}/{roomId} -> updateAppserviceRoomDirectoryVsibility



// GET /_matrix/client/r0/notifications -> getNotifications
// GET /_matrix/client/r0/admin/whois/{userId} -> getWhoIs

// POST /_matrix/client/r0/search -> search
