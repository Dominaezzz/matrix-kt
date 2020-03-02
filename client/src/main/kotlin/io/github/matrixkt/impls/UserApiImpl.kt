package io.github.matrixkt.impls

import io.github.matrixkt.apis.UserApi
import io.github.matrixkt.models.*
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlin.reflect.KProperty0

internal class UserApiImpl(private val client: HttpClient, private val accessTokenProp: KProperty0<String>) : UserApi {
    private inline val accessToken: String get() = accessTokenProp.get()

    override suspend fun searchUserDirectory(searchTerm: String, limit: Long?): SearchUsersResponse {
        return client.post("/_matrix/client/r0/user_directory/search") {
            header("Authorization", "Bearer $accessToken")
            contentType(ContentType.Application.Json)
            body = SearchUsersRequest(searchTerm, limit)
        }
    }

    override suspend fun setDisplayName(userId: String, displayName: String?) {
        return client.put {
            url {
                path("_matrix", "client", "r0", "profile", userId, "displayname")
            }
            header("Authorization", "Bearer $accessToken")
            contentType(ContentType.Application.Json)
            body = SetDisplayNameRequest(displayName)
        }
    }

    override suspend fun getDisplayName(userId: String): String? {
        val response = client.get<GetDisplayNameResponse> {
            url {
                path("_matrix", "client", "r0", "profile", userId, "displayname")
            }
        }
        return response.displayName
    }

    override suspend fun setAvatarUrl(userId: String, avatarUrl: String?) {
        return client.put {
            url {
                path("_matrix", "client", "r0", "profile", userId, "avatar_url")
            }
            header("Authorization", "Bearer $accessToken")
            contentType(ContentType.Application.Json)
            body = SetAvatarUrlRequest(avatarUrl)
        }
    }

    override suspend fun getAvatarUrl(userId: String): String? {
        val response = client.get<GetAvatarUrlResponse> {
            url {
                path("_matrix", "client", "r0", "profile", userId, "avatar_url")
            }
        }
        return response.avatarUrl
    }

    override suspend fun getUserProfile(userId: String): GetUserProfileResponse {
        return client.get {
            url {
                path("_matrix", "client", "r0", "profile", userId)
            }
        }
    }

    override suspend fun setPresence(userId: String, presence: Presence, statusMsg: String?) {
        return client.put {
            url {
                path("_matrix", "client", "r0", "presence", userId, "status")
            }
            header("Authorization", "Bearer $accessToken")
            contentType(ContentType.Application.Json)
            body = SetPresenceRequest(presence, statusMsg)
        }
    }

    override suspend fun getPresence(userId: String): GetPresenceResponse {
        return client.get {
            url {
                path("_matrix", "client", "r0", "presence", userId, "status")
            }
            header("Authorization", "Bearer $accessToken")
        }
    }
}
