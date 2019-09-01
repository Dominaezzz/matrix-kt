package io.github.matrixkt.impls

import io.github.matrixkt.apis.UserApi
import io.github.matrixkt.models.*
import io.ktor.client.HttpClient
import io.ktor.client.call.receive
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.response.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.io.core.use
import kotlin.reflect.KProperty0

internal class UserApiImpl(private val client: HttpClient, private val accessTokenProp: KProperty0<String>) : UserApi {
    private inline val accessToken: String get() = accessTokenProp.get()

    override suspend fun searchUserDirectory(searchTerm: String, limit: Long?): SearchUsersResponse {
        val response = client.post<HttpResponse>("/_matrix/client/r0/user_directory/search") {
            header("Authorization", "Bearer $accessToken")
            contentType(ContentType.Application.Json)
            body = SearchUsersRequest(searchTerm, limit)
        }

        response.use {
            when (it.status) {
                HttpStatusCode.OK -> return it.receive()
                else -> throw it.receive<MatrixError>()
            }
        }
    }

    override suspend fun setDisplayName(userId: String, displayName: String?) {
        val response = client.put<HttpResponse> {
            url {
                path("_matrix", "client", "r0", "profile", userId, "displayname")
            }
            header("Authorization", "Bearer $accessToken")
            contentType(ContentType.Application.Json)
            body = SetDisplayNameRequest(displayName)
        }

        response.use {
            when (it.status) {
                HttpStatusCode.OK -> return it.receive()
                else -> throw it.receive<MatrixError>()
            }
        }
    }

    override suspend fun getDisplayName(userId: String): String? {
        val response = client.get<HttpResponse> {
            url {
                path("_matrix", "client", "r0", "profile", userId, "displayname")
            }
        }

        response.use {
            when (it.status) {
                HttpStatusCode.OK -> return it.receive<GetDisplayNameResponse>().displayName
                else -> throw it.receive<MatrixError>()
            }
        }
    }

    override suspend fun setAvatarUrl(userId: String, avatarUrl: String?) {
        val response = client.put<HttpResponse> {
            url {
                path("_matrix", "client", "r0", "profile", userId, "avatar_url")
            }
            header("Authorization", "Bearer $accessToken")
            contentType(ContentType.Application.Json)
            body = SetAvatarUrlRequest(avatarUrl)
        }

        response.use {
            when (it.status) {
                HttpStatusCode.OK -> return it.receive()
                else -> throw it.receive<MatrixError>()
            }
        }
    }

    override suspend fun getAvatarUrl(userId: String): String? {
        val response = client.get<HttpResponse> {
            url {
                path("_matrix", "client", "r0", "profile", userId, "avatar_url")
            }
        }

        response.use {
            when (it.status) {
                HttpStatusCode.OK -> return it.receive<GetAvatarUrlResponse>().avatarUrl
                else -> throw it.receive<MatrixError>()
            }
        }
    }

    override suspend fun getUserProfile(userId: String): GetUserProfileResponse {
        val response = client.get<HttpResponse> {
            url {
                path("_matrix", "client", "r0", "profile", userId)
            }
        }

        response.use {
            when (it.status) {
                HttpStatusCode.OK -> return it.receive()
                else -> throw it.receive<MatrixError>()
            }
        }
    }

    override suspend fun setPresence(userId: String, presence: Presence, statusMsg: String?) {
        val response = client.put<HttpResponse> {
            url {
                path("_matrix", "client", "r0", "presence", userId, "status")
            }
            header("Authorization", "Bearer $accessToken")
            contentType(ContentType.Application.Json)
            body = SetPresenceRequest(presence, statusMsg)
        }

        response.use {
            when (it.status) {
                HttpStatusCode.OK -> return it.receive()
                else -> throw it.receive<MatrixError>()
            }
        }
    }

    override suspend fun getPresence(userId: String): GetPresenceResponse {
        val response = client.get<HttpResponse> {
            url {
                path("_matrix", "client", "r0", "presence", userId, "status")
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
}
