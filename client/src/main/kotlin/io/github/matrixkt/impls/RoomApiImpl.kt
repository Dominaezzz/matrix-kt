package io.github.matrixkt.impls

import io.github.matrixkt.apis.RoomApi
import io.github.matrixkt.models.*
import io.github.matrixkt.models.events.MatrixEvent
import io.github.matrixkt.models.events.Membership
import io.github.matrixkt.models.events.contents.RoomRedactionContent
import io.github.matrixkt.utils.MatrixJson
import io.github.matrixkt.utils.MatrixJsonConfig
import io.github.matrixkt.utils.MatrixSerialModule
import io.ktor.client.HttpClient
import io.ktor.client.call.receive
import io.ktor.client.request.*
import io.ktor.client.response.HttpResponse
import io.ktor.client.response.readText
import io.ktor.http.*
import kotlinx.io.core.use
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.list
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty0

internal class RoomApiImpl(private val client: HttpClient, private val accessTokenProp: KProperty0<String>) : RoomApi {
    private inline val accessToken: String get() = accessTokenProp.get()

    private val eventJson = Json(
        MatrixJsonConfig.copy(classDiscriminator = "type"),
        MatrixSerialModule
    )

    override suspend fun createRoom(params: CreateRoomRequest): String {
        val response = client.post<HttpResponse>(path = "/_matrix/client/r0/createRoom") {
            header("Authorization", "Bearer $accessToken")
        }

        response.use {
            when (it.status) {
                HttpStatusCode.OK -> return it.receive<CreateRoomResponse>().roomId
                else -> throw it.receive<MatrixError>()
            }
        }
    }

    override suspend fun setRoomAlias(roomAlias: String, roomId: String) {
        val response = client.put<HttpResponse> {
            url {
                encodedPath = "/_matrix/client/r0/directory/room/${roomAlias.encodeURLQueryComponent(encodeFull = true)}"
                // path("_matrix", "client", "r0", "directory", "room", roomAlias)
            }

            header("Authorization", "Bearer $accessToken")

            contentType(ContentType.Application.Json)
            body = CreateRoomAliasRequest(roomId)
        }

        when (response.status) {
            HttpStatusCode.OK -> return
            else -> throw response.receive<MatrixError>()
        }
    }

    override suspend fun getRoomIdByAlias(roomAlias: String): ResolveRoomAliasResponse {
        val response = client.get<HttpResponse> {
            url("/_matrix/client/r0/directory/room/${roomAlias.encodeURLQueryComponent(encodeFull = true)}")
            // path("_matrix", "client", "r0", "directory", "room", roomAlias)

            header("Authorization", "Bearer $accessToken")
        }

        response.use {
            when (it.status) {
                HttpStatusCode.OK -> return it.receive()
                else -> throw it.receive<MatrixError>()
            }
        }
    }

    override suspend fun deleteRoomAlias(roomAlias: String) {
        val response = client.delete<HttpResponse> {
            url {
                encodedPath = "/_matrix/client/r0/directory/room/${roomAlias.encodeURLQueryComponent(encodeFull = true)}"
                // path("_matrix", "client", "r0", "directory", "room", roomAlias)
            }

            header("Authorization", "Bearer $accessToken")
        }

        response.use {
            when (it.status) {
                HttpStatusCode.OK -> return
                else -> throw it.receive<MatrixError>()
            }
        }
    }

    override suspend fun getJoinedRooms(): List<String> {
        val response = client.get<HttpResponse>("/_matrix/client/r0/joined_rooms") {
            header("Authorization", "Bearer $accessToken")
        }

        response.use {
            when (it.status) {
                HttpStatusCode.OK -> return it.receive<GetJoinedRoomsResponse>().joinedRooms
                else -> throw it.receive<MatrixError>()
            }
        }
    }

    override suspend fun inviteUser(roomId: String, userId: String) {
        val response = client.post<HttpResponse> {
            url {
                path("_matrix", "client", "r0", "rooms", roomId, "invite")
            }

            header("Authorization", "Bearer $accessToken")

            contentType(ContentType.Application.Json)
            body = InviteRequest(userId)
        }

        response.use {
            when (it.status) {
                HttpStatusCode.OK -> return it.receive()
                else -> throw it.receive<MatrixError>()
            }
        }
    }

    override suspend fun joinRoomById(roomId: String, params: JoinRoomRequest): String {
        val response = client.post<HttpResponse> {
            url {
                path("_matrix", "client", "r0", "rooms", roomId, "join")
            }

            header("Authorization", "Bearer $accessToken")

            contentType(ContentType.Application.Json)
            body = params
        }

        response.use {
            when (it.status) {
                HttpStatusCode.OK -> return it.receive<JoinRoomResponse>().roomId
                else -> throw it.receive<MatrixError>()
            }
        }
    }

    override suspend fun joinRoom(roomIdOrAlias: String, servers: List<String>, params: JoinRoomRequest): String {
        val response = client.post<HttpResponse> {
            url {
                path("_matrix", "client", "r0", "join", roomIdOrAlias)
            }
            parameter("server_name", servers)

            header("Authorization", "Bearer $accessToken")

            contentType(ContentType.Application.Json)
            body = params
        }

        response.use {
            when (it.status) {
                HttpStatusCode.OK -> return it.receive<JoinRoomResponse>().roomId
                else -> throw it.receive<MatrixError>()
            }
        }
    }

    override suspend fun leaveRoom(roomId: String) {
        val response = client.post<HttpResponse> {
            url {
                path("_matrix", "client", "r0", "rooms", roomId, "leave")
            }

            header("Authorization", "Bearer $accessToken")
        }

        response.use {
            when (it.status) {
                HttpStatusCode.OK -> return
                else -> throw it.receive<MatrixError>()
            }
        }
    }

    override suspend fun forgetRoom(roomId: String) {
        val response = client.post<HttpResponse> {
            url {
                path("_matrix", "client", "r0", "rooms", roomId, "forget")
            }

            header("Authorization", "Bearer $accessToken")
        }

        response.use {
            when (it.status) {
                HttpStatusCode.OK -> return
                else -> throw it.receive<MatrixError>()
            }
        }
    }

    override suspend fun kick(roomId: String, params: KickRequest) {
        val response = client.post<HttpResponse> {
            url {
                path("_matrix", "client", "r0", "rooms", roomId, "kick")
            }

            header("Authorization", "Bearer $accessToken")

            contentType(ContentType.Application.Json)
            body = params
        }

        response.use {
            when (it.status) {
                HttpStatusCode.OK -> return it.receive()
                else -> throw it.receive<MatrixError>()
            }
        }
    }

    override suspend fun ban(roomId: String, params: BanRequest) {
        val response = client.post<HttpResponse> {
            url {
                path("_matrix", "client", "r0", "rooms", roomId, "ban")
            }

            header("Authorization", "Bearer $accessToken")

            contentType(ContentType.Application.Json)
            body = params
        }

        response.use {
            when (it.status) {
                HttpStatusCode.OK -> return
                else -> throw it.receive<MatrixError>()
            }
        }
    }

    override suspend fun unban(roomId: String, userId: String) {
        val response = client.post<HttpResponse> {
            url {
                path("_matrix", "client", "r0", "rooms", roomId, "unban")
            }

            header("Authorization", "Bearer $accessToken")

            contentType(ContentType.Application.Json)
            body = UnBanRequest(userId)
        }

        response.use {
            when (it.status) {
                HttpStatusCode.OK -> return
                else -> throw it.receive<MatrixError>()
            }
        }
    }

    override suspend fun getMembersByRoom(
        roomId: String,
        at: String?,
        membership: Membership?,
        notMembership: Membership?
    ): GetMembersResponse {
        val response = client.get<HttpResponse>{
            url {
                path("_matrix", "client", "r0", "rooms", roomId, "members")
            }

            header("Authorization", "Bearer $accessToken")
        }

        response.use {
            when (it.status) {
                HttpStatusCode.OK -> return it.receive()
                else -> throw it.receive<MatrixError>()
            }
        }
    }

    override suspend fun getJoinedMembersByRoom(roomId: String): Map<String, RoomMember> {
        val response = client.get<HttpResponse>{
            url {
                path("_matrix", "client", "r0", "rooms", roomId, "joined_members")
            }

            header("Authorization", "Bearer $accessToken")
        }

        response.use {
            when (it.status) {
                HttpStatusCode.OK -> return it.receive<JoinedMembersResponse>().joined
                else -> throw it.receive<MatrixError>()
            }
        }
    }

    override suspend fun getRoomEvents(
        roomId: String,
        from: String,
        to: String?,
        dir: Direction,
        limit: Long?,
        filter: String?
    ): MessagesResponse {
        val response = client.get<HttpResponse>{
            url {
                path("_matrix", "client", "r0", "rooms", roomId, "messages")
            }

            header("Authorization", "Bearer $accessToken")
        }

        response.use {
            when (it.status) {
                HttpStatusCode.OK -> return eventJson.parse(MessagesResponse.serializer(), it.readText())
                else -> throw it.receive<MatrixError>()
            }
        }
    }

    override suspend fun setRoomStateWithKey(
        roomId: String,
        eventType: String,
        stateKey: String,
        eventContent: Any
    ): String {
        val response = client.put<HttpResponse> {
            url {
                path("_matrix", "client", "r0", "rooms", roomId, "state", eventType, stateKey)
            }

            header("Authorization", "Bearer $accessToken")

            contentType(ContentType.Application.Json)
            body = eventContent
        }

        response.use {
            when (it.status) {
                HttpStatusCode.OK -> return it.receive<SendStateEventResponse>().eventId
                else -> throw it.receive<MatrixError>()
            }
        }
    }

    override suspend fun sendMessage(roomId: String, eventType: String, txnId: String, content: JsonObject): String {
        val response = client.put<HttpResponse>{
            url {
                path("_matrix", "client", "r0", "rooms", roomId, "send", eventType, txnId)
            }

            header("Authorization", "Bearer $accessToken")

            contentType(ContentType.Application.Json)
            body = content
        }

        response.use {
            when (it.status) {
                HttpStatusCode.OK -> return it.receive<SendMessageEventResponse>().eventId
                else -> throw it.receive<MatrixError>()
            }
        }
    }

    override suspend fun redactEvent(roomId: String, eventId: String, txnId: String, reason: String?): String {
        val response = client.put<HttpResponse>{
            url {
                path("_matrix", "client", "r0", "rooms", roomId, "redact", eventId, txnId)
            }

            header("Authorization", "Bearer $accessToken")

            contentType(ContentType.Application.Json)
            body = RoomRedactionContent(reason)
        }

        response.use {
            when (it.status) {
                HttpStatusCode.OK -> return it.receive<SendMessageEventResponse>().eventId
                else -> throw it.receive<MatrixError>()
            }
        }
    }

    override suspend fun getPublicRooms(limit: Int?, since: String?, server: String?): PublicRoomsResponse {
        val response = client.get<HttpResponse>("/_matrix/client/r0/publicRooms") {
            if (limit != null) parameter("limit", limit)
            if (since != null) parameter("since", since)
            if (server != null) parameter("server", server)
        }

        response.use {
            when (it.status) {
                HttpStatusCode.OK -> return it.receive()
                else -> throw it.receive<MatrixError>()
            }
        }
    }

    override suspend fun queryPublicRooms(server: String?, params: SearchPublicRoomsRequest): PublicRoomsResponse {
        val response = client.post<HttpResponse>("/_matrix/client/r0/publicRooms") {
            if (server != null) parameter("server", server)

            header("Authorization", "Bearer $accessToken")

            contentType(ContentType.Application.Json)
            body = params
        }

        response.use {
            when (it.status) {
                HttpStatusCode.OK -> return it.receive()
                else -> throw it.receive<MatrixError>()
            }
        }
    }

    override suspend fun upgradeRoom(roomId: String, newVersion: String): String {
        val response = client.post<HttpResponse> {
            url {
                path("_matrix", "client", "r0", "rooms", roomId, "upgrade")
            }

            header("Authorization", "Bearer $accessToken")

            contentType(ContentType.Application.Json)
            body = UpgradeRoomRequest(newVersion)
        }

        response.use {
            when (it.status) {
                HttpStatusCode.OK -> return it.receive<UpgradeRoomResponse>().replacementRoom
                else -> throw it.receive<MatrixError>()
            }
        }
    }

    override suspend fun setTyping(userId: String, roomId: String, params: TypingRequest) {
        val response = client.put<HttpResponse> {
            url {
                path("_matrix", "client", "r0", "rooms", roomId, "typing", userId)
            }

            header("Authorization", "Bearer $accessToken")

            contentType(ContentType.Application.Json)
            body = params
        }

        response.use {
            when (it.status) {
                HttpStatusCode.OK -> return it.receive()
                else -> throw it.receive<MatrixError>()
            }
        }
    }

    override suspend fun postReceipt(roomId: String, receiptType: String, eventId: String) {
        val response = client.post<HttpResponse> {
            url {
                path("_matrix", "client", "r0", "rooms", roomId, "receipt", receiptType, eventId)
            }

            header("Authorization", "Bearer $accessToken")
        }

        response.use {
            when (it.status) {
                HttpStatusCode.OK -> return it.receive()
                else -> throw it.receive<MatrixError>()
            }
        }
    }

    override suspend fun setReadMarker(roomId: String, params: ReadMarkersRequest) {
        val response = client.post<HttpResponse> {
            url {
                path("_matrix", "client", "r0", "rooms", roomId, "read_markers")
            }

            header("Authorization", "Bearer $accessToken")

            contentType(ContentType.Application.Json)
            body = params
        }

        response.use {
            when (it.status) {
                HttpStatusCode.OK -> return it.receive()
                else -> throw it.receive<MatrixError>()
            }
        }
    }

    override suspend fun getOneRoomEvent(roomId: String, eventId: String): MatrixEvent {
        val response = client.get<HttpResponse>{
            url {
                path("_matrix", "client", "r0", "rooms", roomId, "event", eventId)
            }

            header("Authorization", "Bearer $accessToken")
        }

        response.use {
            when (it.status) {
                HttpStatusCode.OK -> return it.receive()
                else -> throw it.receive<MatrixError>()
            }
        }
    }

    override suspend fun getRoomStateWithKey(roomId: String, eventType: String, stateKey: String): JsonObject {
        val response = client.get<HttpResponse> {
            url {
                path("_matrix", "client", "r0", "rooms", roomId, "state", eventType, stateKey)
            }

            header("Authorization", "Bearer $accessToken")
        }

        response.use {
            @Suppress("UNCHECKED_CAST")
            when (it.status) {
                HttpStatusCode.OK -> return it.receive()
                else -> throw it.receive<MatrixError>()
            }
        }
    }

    override suspend fun getRoomState(roomId: String): List<MatrixEvent> {
        val response = client.get<HttpResponse>{
            url {
                path("_matrix", "client", "r0", "rooms", roomId, "state")
            }

            header("Authorization", "Bearer $accessToken")
        }

        response.use {
            when (it.status) {
                HttpStatusCode.OK -> return MatrixJson.parse(MatrixEvent.serializer().list, it.readText())
                else -> throw it.receive<MatrixError>()
            }
        }
    }


    override suspend fun getVisibility(roomId: String): RoomVisibility {
        val response = client.get<HttpResponse> {
            url {
                path("_matrix", "client", "r0", "directory", "list", "room", roomId)
            }
        }

        response.use {
            when (it.status) {
                HttpStatusCode.OK -> return it.receive<VisibilityResponse>().visibility
                else -> throw it.receive<MatrixError>()
            }
        }
    }

    override suspend fun setVisibility(roomId: String, visibility: RoomVisibility) {
        val response = client.put<HttpResponse> {
            url {
                path("_matrix", "client", "r0", "directory", "list", "room", roomId)
            }

            header("Authorization", "Bearer $accessToken")

            contentType(ContentType.Application.Json)
            body = VisibilityRequest(visibility)
        }

        response.use {
            when (it.status) {
                HttpStatusCode.OK -> return
                else -> throw it.receive<MatrixError>()
            }
        }
    }
}
