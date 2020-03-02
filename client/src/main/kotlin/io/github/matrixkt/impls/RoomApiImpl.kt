package io.github.matrixkt.impls

import io.github.matrixkt.apis.RoomApi
import io.github.matrixkt.models.*
import io.github.matrixkt.models.events.MatrixEvent
import io.github.matrixkt.models.events.Membership
import io.github.matrixkt.models.events.contents.RoomRedactionContent
import io.github.matrixkt.utils.MatrixJson
import io.ktor.client.HttpClient
import io.ktor.client.request.*
import io.ktor.client.statement.HttpStatement
import io.ktor.http.*
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.list
import kotlin.reflect.KProperty0

internal class RoomApiImpl(private val client: HttpClient, private val accessTokenProp: KProperty0<String>) : RoomApi {
    private inline val accessToken: String get() = accessTokenProp.get()

    override suspend fun createRoom(params: CreateRoomRequest): String {
        val response = client.post<CreateRoomResponse>(path = "/_matrix/client/r0/createRoom") {
            header("Authorization", "Bearer $accessToken")
        }
        return response.roomId
    }

    override suspend fun setRoomAlias(roomAlias: String, roomId: String) {
        return client.put {
            url {
                encodedPath = "/_matrix/client/r0/directory/room/${roomAlias.encodeURLQueryComponent(encodeFull = true)}"
                // path("_matrix", "client", "r0", "directory", "room", roomAlias)
            }

            header("Authorization", "Bearer $accessToken")

            contentType(ContentType.Application.Json)
            body = CreateRoomAliasRequest(roomId)
        }
    }

    override suspend fun getRoomIdByAlias(roomAlias: String): ResolveRoomAliasResponse {
        return client.get {
            url("/_matrix/client/r0/directory/room/${roomAlias.encodeURLQueryComponent(encodeFull = true)}")
            // path("_matrix", "client", "r0", "directory", "room", roomAlias)

            header("Authorization", "Bearer $accessToken")
        }
    }

    override suspend fun deleteRoomAlias(roomAlias: String) {
        return client.delete {
            url {
                encodedPath = "/_matrix/client/r0/directory/room/${roomAlias.encodeURLQueryComponent(encodeFull = true)}"
                // path("_matrix", "client", "r0", "directory", "room", roomAlias)
            }

            header("Authorization", "Bearer $accessToken")
        }
    }

    override suspend fun getJoinedRooms(): List<String> {
        val response = client.get<GetJoinedRoomsResponse>("/_matrix/client/r0/joined_rooms") {
            header("Authorization", "Bearer $accessToken")
        }
        return response.joinedRooms
    }

    override suspend fun inviteUser(roomId: String, userId: String) {
        return client.post {
            url {
                path("_matrix", "client", "r0", "rooms", roomId, "invite")
            }

            header("Authorization", "Bearer $accessToken")

            contentType(ContentType.Application.Json)
            body = InviteRequest(userId)
        }
    }

    override suspend fun joinRoomById(roomId: String, params: JoinRoomRequest): String {
        val response = client.post<JoinRoomResponse> {
            url {
                path("_matrix", "client", "r0", "rooms", roomId, "join")
            }

            header("Authorization", "Bearer $accessToken")

            contentType(ContentType.Application.Json)
            body = params
        }
        return response.roomId
    }

    override suspend fun joinRoom(roomIdOrAlias: String, servers: List<String>, params: JoinRoomRequest): String {
        val response = client.post<JoinRoomResponse> {
            url {
                path("_matrix", "client", "r0", "join", roomIdOrAlias)
            }
            parameter("server_name", servers)

            header("Authorization", "Bearer $accessToken")

            contentType(ContentType.Application.Json)
            body = params
        }
        return response.roomId
    }

    override suspend fun leaveRoom(roomId: String) {
        return client.post {
            url {
                path("_matrix", "client", "r0", "rooms", roomId, "leave")
            }

            header("Authorization", "Bearer $accessToken")
        }
    }

    override suspend fun forgetRoom(roomId: String) {
        return client.post {
            url {
                path("_matrix", "client", "r0", "rooms", roomId, "forget")
            }

            header("Authorization", "Bearer $accessToken")
        }
    }

    override suspend fun kick(roomId: String, params: KickRequest) {
        return client.post {
            url {
                path("_matrix", "client", "r0", "rooms", roomId, "kick")
            }

            header("Authorization", "Bearer $accessToken")

            contentType(ContentType.Application.Json)
            body = params
        }
    }

    override suspend fun ban(roomId: String, params: BanRequest) {
        return client.post {
            url {
                path("_matrix", "client", "r0", "rooms", roomId, "ban")
            }

            header("Authorization", "Bearer $accessToken")

            contentType(ContentType.Application.Json)
            body = params
        }
    }

    override suspend fun unban(roomId: String, userId: String) {
        return client.post {
            url {
                path("_matrix", "client", "r0", "rooms", roomId, "unban")
            }

            header("Authorization", "Bearer $accessToken")

            contentType(ContentType.Application.Json)
            body = UnBanRequest(userId)
        }
    }

    override suspend fun getMembersByRoom(
        roomId: String,
        at: String?,
        membership: Membership?,
        notMembership: Membership?
    ): GetMembersResponse {
        return client.get{
            url {
                path("_matrix", "client", "r0", "rooms", roomId, "members")
            }

            header("Authorization", "Bearer $accessToken")
        }
    }

    override suspend fun getJoinedMembersByRoom(roomId: String): Map<String, RoomMember> {
        val response = client.get<JoinedMembersResponse>{
            url {
                path("_matrix", "client", "r0", "rooms", roomId, "joined_members")
            }

            header("Authorization", "Bearer $accessToken")
        }
        return response.joined
    }

    override suspend fun getRoomEvents(
        roomId: String,
        from: String,
        to: String?,
        dir: Direction,
        limit: Long?,
        filter: String?
    ): MessagesResponse {
        return client.get{
            url {
                path("_matrix", "client", "r0", "rooms", roomId, "messages")
            }
            parameter("from", from)
            if (to != null) parameter("to", to)
            parameter("dir", when (dir) {
                Direction.F -> 'f'
                Direction.B -> 'b'
            })
            if (limit != null) parameter("limit", limit)
            if (filter != null) parameter("filter", filter)

            header("Authorization", "Bearer $accessToken")
        }
    }

    override suspend fun setRoomStateWithKey(
        roomId: String,
        eventType: String,
        stateKey: String,
        eventContent: Any
    ): String {
        val response = client.put<SendStateEventResponse> {
            url {
                path("_matrix", "client", "r0", "rooms", roomId, "state", eventType, stateKey)
            }

            header("Authorization", "Bearer $accessToken")

            contentType(ContentType.Application.Json)
            body = eventContent
        }
        return response.eventId
    }

    override suspend fun sendMessage(roomId: String, eventType: String, txnId: String, content: JsonObject): String {
        val response = client.put<SendMessageEventResponse>{
            url {
                path("_matrix", "client", "r0", "rooms", roomId, "send", eventType, txnId)
            }

            header("Authorization", "Bearer $accessToken")

            contentType(ContentType.Application.Json)
            body = content
        }
        return response.eventId
    }

    override suspend fun redactEvent(roomId: String, eventId: String, txnId: String, reason: String?): String {
        val response = client.put<SendMessageEventResponse>{
            url {
                path("_matrix", "client", "r0", "rooms", roomId, "redact", eventId, txnId)
            }

            header("Authorization", "Bearer $accessToken")

            contentType(ContentType.Application.Json)
            body = RoomRedactionContent(reason)
        }
        return response.eventId
    }

    override suspend fun getPublicRooms(limit: Int?, since: String?, server: String?): PublicRoomsResponse {
        return client.get("/_matrix/client/r0/publicRooms") {
            if (limit != null) parameter("limit", limit)
            if (since != null) parameter("since", since)
            if (server != null) parameter("server", server)
        }
    }

    override suspend fun queryPublicRooms(server: String?, params: SearchPublicRoomsRequest): PublicRoomsResponse {
        return client.post("/_matrix/client/r0/publicRooms") {
            if (server != null) parameter("server", server)

            header("Authorization", "Bearer $accessToken")

            contentType(ContentType.Application.Json)
            body = params
        }
    }

    override suspend fun upgradeRoom(roomId: String, newVersion: String): String {
        val response = client.post<UpgradeRoomResponse> {
            url {
                path("_matrix", "client", "r0", "rooms", roomId, "upgrade")
            }

            header("Authorization", "Bearer $accessToken")

            contentType(ContentType.Application.Json)
            body = UpgradeRoomRequest(newVersion)
        }
        return response.replacementRoom
    }

    override suspend fun setTyping(userId: String, roomId: String, params: TypingRequest) {
        return client.put {
            url {
                path("_matrix", "client", "r0", "rooms", roomId, "typing", userId)
            }

            header("Authorization", "Bearer $accessToken")

            contentType(ContentType.Application.Json)
            body = params
        }
    }

    override suspend fun postReceipt(roomId: String, receiptType: String, eventId: String) {
        return client.post {
            url {
                path("_matrix", "client", "r0", "rooms", roomId, "receipt", receiptType, eventId)
            }

            header("Authorization", "Bearer $accessToken")
        }
    }

    override suspend fun setReadMarker(roomId: String, params: ReadMarkersRequest) {
        return client.post {
            url {
                path("_matrix", "client", "r0", "rooms", roomId, "read_markers")
            }

            header("Authorization", "Bearer $accessToken")

            contentType(ContentType.Application.Json)
            body = params
        }
    }

    override suspend fun getOneRoomEvent(roomId: String, eventId: String): MatrixEvent {
        return client.get{
            url {
                path("_matrix", "client", "r0", "rooms", roomId, "event", eventId)
            }

            header("Authorization", "Bearer $accessToken")
        }
    }

    override suspend fun getRoomStateWithKey(roomId: String, eventType: String, stateKey: String): JsonObject {
        return client.get {
            url {
                path("_matrix", "client", "r0", "rooms", roomId, "state", eventType, stateKey)
            }

            header("Authorization", "Bearer $accessToken")
        }
    }

    override suspend fun getRoomState(roomId: String): List<MatrixEvent> {
        return client.get {
            url {
                path("_matrix", "client", "r0", "rooms", roomId, "state")
            }

            header("Authorization", "Bearer $accessToken")
        }
    }


    override suspend fun getVisibility(roomId: String): RoomVisibility {
        val response = client.get<VisibilityResponse> {
            url {
                path("_matrix", "client", "r0", "directory", "list", "room", roomId)
            }
        }
        return response.visibility
    }

    override suspend fun setVisibility(roomId: String, visibility: RoomVisibility) {
        return client.put {
            url {
                path("_matrix", "client", "r0", "directory", "list", "room", roomId)
            }

            header("Authorization", "Bearer $accessToken")

            contentType(ContentType.Application.Json)
            body = VisibilityRequest(visibility)
        }
    }
}
