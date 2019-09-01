package io.github.matrixkt.impls

import io.github.matrixkt.apis.KeysApi
import io.github.matrixkt.models.*
import io.ktor.client.HttpClient
import io.ktor.client.call.receive
import io.ktor.client.request.*
import io.ktor.client.response.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.io.core.use
import kotlin.reflect.KProperty0

internal class KeysApiImpl(private val client: HttpClient, private val accessTokenProp: KProperty0<String>) : KeysApi {
    private inline val accessToken: String get() = accessTokenProp.get()

    override suspend fun uploadKeys(keys: UploadKeysRequest): UploadKeysResponse {
        val response = client.post<HttpResponse>(path = "_matrix/client/r0/keys/upload") {
            header("Authorization", "Bearer $accessToken")
            contentType(ContentType.Application.Json)
            body = keys
        }

        response.use {
            when (it.status) {
                HttpStatusCode.OK -> return it.receive()
                else -> throw it.receive<MatrixError>()
            }
        }
    }

    override suspend fun queryKeys(query: QueryKeysRequest): QueryKeysResponse {
        val response = client.post<HttpResponse>(path = "_matrix/client/r0/keys/query") {
            header("Authorization", "Bearer $accessToken")
            contentType(ContentType.Application.Json)
            body = query
        }

        response.use {
            when (it.status) {
                HttpStatusCode.OK -> return it.receive()
                else -> throw it.receive<MatrixError>()
            }
        }
    }

    override suspend fun claimKeys(query: ClaimKeysRequest): ClaimKeysResponse {
        val response = client.post<HttpResponse>(path = "_matrix/client/r0/keys/claim") {
            header("Authorization", "Bearer $accessToken")
            contentType(ContentType.Application.Json)
            body = query
        }

        response.use {
            when (it.status) {
                HttpStatusCode.OK -> return it.receive()
                else -> throw it.receive<MatrixError>()
            }
        }
    }

    override suspend fun getKeysChanges(from: String, to: String): KeyChangesResponse {
        val response = client.get<HttpResponse> {
            url {
                encodedPath = "_matrix/client/r0/keys/changes"
                parameter("from", from)
                parameter("to", to)
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
