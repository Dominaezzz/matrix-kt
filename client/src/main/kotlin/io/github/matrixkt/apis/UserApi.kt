package io.github.matrixkt.apis

import io.github.matrixkt.models.*
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.http.ContentType
import io.ktor.http.contentType
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
        return client.post("/_matrix/client/r0/user_directory/search") {
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
}

// GET /_matrix/client/r0/user/{userId}/account_data/{type} -> getAccountData
// PUT /_matrix/client/r0/user/{userId}/account_data/{type} -> setAccountData
// POST /_matrix/client/r0/user/{userId}/openid/request_token -> requestOpenIdToken
// GET /_matrix/client/r0/user/{userId}/rooms/{roomId}/account_data/{type} -> getAccountDataPerRoom
// PUT /_matrix/client/r0/user/{userId}/rooms/{roomId}/account_data/{type} -> setAccountDataPerRoom
// GET /_matrix/client/r0/user/{userId}/rooms/{roomId}/tags -> getRoomTags
// PUT /_matrix/client/r0/user/{userId}/rooms/{roomId}/tags/{tag} -> setRoomTag
// DELETE /_matrix/client/r0/user/{userId}/rooms/{roomId}/tags/{tag} -> deleteRoomTag
