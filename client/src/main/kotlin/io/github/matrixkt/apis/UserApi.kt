package io.github.matrixkt.apis

import io.github.matrixkt.models.*
import io.github.matrixkt.models.events.contents.TagContent
import io.ktor.client.HttpClient
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlin.reflect.KProperty0

class UserApi internal constructor(private val client: HttpClient, private val accessTokenProp: KProperty0<String>) {
    private inline val accessToken: String get() = accessTokenProp.get()

    /**
     * Performs a search for users on the homeserver.
     * The homeserver may determine which subset of users are searched,
     * however the homeserver MUST at a minimum consider the users the
     * requesting user shares a room with and those who reside in public rooms (known to the homeserver).
     * The search MUST consider local users to the homeserver, and SHOULD query remote users as part of the search.
     *
     * The search is performed case-insensitively on user IDs and display names preferably using a collation
     * determined based upon the Accept-Language header provided in the request, if present.
     *
     * **Rate-limited**: Yes.
     *
     * **Requires auth**: Yes.
     *
     * @param[searchTerm] The term to search for.
     * @param[limit] The maximum number of results to return. Defaults to 10.
     */
    suspend fun searchUserDirectory(searchTerm: String, limit: Long? = null): SearchUsersResponse {
        return client.post("_matrix/client/r0/user_directory/search") {
            header("Authorization", "Bearer $accessToken")
            contentType(ContentType.Application.Json)
            body = SearchUsersRequest(searchTerm, limit)
        }
    }

    /**
     * This API sets the given user's display name.
     * You must have permission to set this user's display name,
     * e.g. you need to have their `access_token`.
     *
     * **Rate-limited**: Yes.
     *
     * **Requires auth**: Yes.
     *
     * @param[userId] The user whose display name to set.
     * @param[displayName] The new display name for this user.
     */
    suspend fun setDisplayName(userId: String, displayName: String?) {
        return client.put {
            url {
                path("_matrix", "client", "r0", "profile", userId, "displayname")
            }
            header("Authorization", "Bearer $accessToken")
            contentType(ContentType.Application.Json)
            body = SetDisplayNameRequest(displayName)
        }
    }

    /**
     * Get the user's display name.
     * This API may be used to fetch the user's own displayname or to query the name of other users;
     * either locally or on remote homeservers.
     *
     * **Rate-limited**: No.
     *
     * **Requires auth**: No.
     *
     * @param[userId] The user whose display name to get.
     * @return The user's display name if they have set one, otherwise not present.
     */
    suspend fun getDisplayName(userId: String): String? {
        val response = client.get<GetDisplayNameResponse> {
            url {
                path("_matrix", "client", "r0", "profile", userId, "displayname")
            }
        }
        return response.displayName
    }

    /**
     * This API sets the given user's avatar URL.
     * You must have permission to set this user's avatar URL,
     * e.g. you need to have their `access_token`.
     *
     * **Rate-limited**: Yes.
     *
     * **Requires auth**: Yes.
     *
     * @param[userId] The user whose avatar URL to set.
     * @param[avatarUrl] The new avatar URL for this user.
     */
    suspend fun setAvatarUrl(userId: String, avatarUrl: String?) {
        return client.put {
            url {
                path("_matrix", "client", "r0", "profile", userId, "avatar_url")
            }
            header("Authorization", "Bearer $accessToken")
            contentType(ContentType.Application.Json)
            body = SetAvatarUrlRequest(avatarUrl)
        }
    }

    /**
     * Get the user's avatar URL.
     * This API may be used to fetch the user's own avatar URL or to query the URL of other users;
     * either locally or on remote homeservers.
     *
     * **Rate-limited**: No.
     *
     * **Requires auth**: No.
     *
     * @param[userId] The user whose avatar URL to get.
     * @return The user's avatar URL if they have set one, otherwise not present.
     */
    suspend fun getAvatarUrl(userId: String): String? {
        val response = client.get<GetAvatarUrlResponse> {
            url {
                path("_matrix", "client", "r0", "profile", userId, "avatar_url")
            }
        }
        return response.avatarUrl
    }

    /**
     * Get the combined profile information for this user.
     * This API may be used to fetch the user's own profile information or other users;
     * either locally or on remote homeservers.
     * This API may return keys which are not limited to `displayname` or `avatar_url`.
     *
     * **Rate-limited**: No.
     *
     * **Requires auth**: No.
     *
     * @param[userId] The user whose profile information to get.
     */
    suspend fun getUserProfile(userId: String): GetUserProfileResponse {
        return client.get {
            url {
                path("_matrix", "client", "r0", "profile", userId)
            }
        }
    }

    /**
     * This API sets the given user's presence state.
     * When setting the status, the activity time is updated to reflect that activity;
     * the client does not need to specify the `last_active_ago` field.
     * You cannot set the presence state of another user.
     *
     * **Rate-limited**: Yes.
     *
     * **Requires auth**: Yes.
     *
     * @param[userId] The user whose presence state to update.
     * @param[presence] The new presence state. One of: ["online", "offline", "unavailable"].
     * @param[statusMsg] The status message to attach to this state.
     */
    suspend fun setPresence(userId: String, presence: Presence, statusMsg: String? = null) {
        return client.put {
            url {
                path("_matrix", "client", "r0", "presence", userId, "status")
            }
            header("Authorization", "Bearer $accessToken")
            contentType(ContentType.Application.Json)
            body = SetPresenceRequest(presence, statusMsg)
        }
    }

    /**
     * Get the given user's presence state.
     *
     * **Rate-limited**: No.
     *
     * **Requires auth**: Yes.
     *
     * @param[userId] The user whose presence state to get.
     */
    suspend fun getPresence(userId: String): GetPresenceResponse {
        return client.get {
            url {
                path("_matrix", "client", "r0", "presence", userId, "status")
            }
            header("Authorization", "Bearer $accessToken")
        }
    }

    /**
     * Set some account_data for the client.
     * This config is only visible to the user that set the account_data.
     * The config will be synced to clients in the top-level account_data.
     *
     * **Rate-limited**: No.
     *
     * **Requires auth**: Yes.
     *
     * @param[userId] The ID of the user to set account_data for. The access token must be authorized to make requests for this user ID.
     * @param[type]	The event type of the account_data to set. Custom types should be namespaced to avoid clashes.
     */
    suspend fun setAccountData(userId: String, type: String, content: JsonElement) {
        return client.put {
            url {
                path("_matrix", "client", "r0", "user", userId, "account_data", type)
            }
            header("Authorization", "Bearer $accessToken")

            contentType(ContentType.Application.Json)
            body = content
        }
    }

    /**
     * Get some account_data for the client. This config is only visible to the user that set the account_data.
     *
     * **Rate-limited**: No.
     *
     * **Requires auth**: Yes.
     *
     * @param[userId] The ID of the user to get account_data for. The access token must be authorized to make requests for this user ID.
     * @param[type] The event type of the account_data to get. Custom types should be namespaced to avoid clashes.
     */
    suspend fun getAccountData(userId: String, type: String): JsonElement {
        return client.get {
            url {
                path("_matrix", "client", "r0", "user", userId, "account_data", type)
            }
            header("Authorization", "Bearer $accessToken")
        }
    }

    /**
     * Set some account_data for the client on a given room.
     * This config is only visible to the user that set the account_data.
     * The config will be synced to clients in the per-room account_data.
     *
     * **Rate-limited**: No.
     *
     * **Requires auth**: Yes.
     *
     * @param[userId] The ID of the user to set account_data for. The access token must be authorized to make requests for this user ID.
     * @param[roomId] The ID of the room to set account_data on.
     * @param[type] The event type of the account_data to set. Custom types should be namespaced to avoid clashes.
     */
    suspend fun setAccountDataPerRoom(userId: String, roomId: String, type: String, content: JsonElement) {
        return client.put {
            url {
                path("_matrix", "client", "r0", "user", userId, "rooms", roomId, "account_data", type)
            }
            header("Authorization", "Bearer $accessToken")

            contentType(ContentType.Application.Json)
            body = content
        }
    }

    /**
     * Get some account_data for the client on a given room. This config is only visible to the user that set the account_data.
     *
     * **Rate-limited**: No.
     *
     * **Requires auth**: Yes.
     *
     * @param[userId] The ID of the user to set account_data for. The access token must be authorized to make requests for this user ID.
     * @param[roomId] The ID of the room to get account_data for.
     * @param[type] The event type of the account_data to get. Custom types should be namespaced to avoid clashes.
     */
    suspend fun getAccountDataPerRoom(userId: String, roomId: String, type: String): JsonElement {
        return client.get {
            url {
                path("_matrix", "client", "r0", "user", userId, "rooms", roomId, "account_data", type)
            }
            header("Authorization", "Bearer $accessToken")
        }
    }

    /**
     * List the tags set by a user on a room.
     *
     * **Rate-limited**: No.
     *
     * **Requires auth**: Yes.
     *
     * @param[userId] The id of the user to get tags for. The access token must be authorized to make requests for this user ID.
     * @param[roomId] The ID of the room to get tags for.
     */
    suspend fun getRoomTags(userId: String, roomId: String): TagContent {
        return client.get {
            url {
                path("_matrix", "client", "r0", "user", userId, "rooms", roomId, "tags")
            }
            header("Authorization", "Bearer $accessToken")
        }
    }

    /**
     * Add a tag to the room.
     *
     * **Rate-limited**: No.
     *
     * **Requires auth**: Yes.
     *
     * @param[userId] The id of the user to add a tag for. The access token must be authorized to make requests for this user ID.
     * @param[roomId] The ID of the room to add a tag to.
     * @param[tag] The tag to add.
     * @param[order] A number in a range [0,1] describing a relative position of the room under the given tag.
     */
    suspend fun setRoomTag(userId: String, roomId: String, tag: String, order: Double? = null) {
        return client.put {
            url {
                path("_matrix", "client", "r0", "user", userId, "rooms", roomId, "tags", tag)
            }
            header("Authorization", "Bearer $accessToken")

            contentType(ContentType.Application.Json)
            body = TagContent.Tag(order)
        }
    }

    /**
     * Remove a tag from the room.
     *
     * **Rate-limited**: No.
     *
     * **Requires auth**: Yes.
     *
     * @param[userId] The id of the user to remove a tag for. The access token must be authorized to make requests for this user ID.
     * @param[roomId] The ID of the room to remove a tag from.
     * @param[tag] The tag to remove.
     */
    suspend fun deleteRoomTag(userId: String, roomId: String, tag: String) {
        return client.delete {
            url {
                path("_matrix", "client", "r0", "user", userId, "rooms", roomId, "tags", tag)
            }
            header("Authorization", "Bearer $accessToken")
        }
    }

    /**
     * Gets an OpenID token object that the requester may supply to another service to verify their identity in Matrix.
     * The generated token is only valid for exchanging for user information from the federation API for OpenID.
     *
     * The access token generated is only valid for the OpenID API. It cannot be used to request another OpenID access token or call /sync, for example.
     *
     * **Rate-limited**: Yes.
     *
     * **Requires auth**: Yes.
     *
     * @param[userId] The user to request and OpenID token for. Should be the user who is authenticated for the request.
     */
    suspend fun requestOpenIdToken(userId: String): GetOpenIdResponse {
        return client.post {
            url {
                path("_matrix", "client", "r0", "user", userId, "openid", "request_token")
            }
            header("Authorization", "Bearer $accessToken")
        }
    }
}
