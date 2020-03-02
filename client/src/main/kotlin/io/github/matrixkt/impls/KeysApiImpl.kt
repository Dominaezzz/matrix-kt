package io.github.matrixkt.impls

import io.github.matrixkt.apis.KeysApi
import io.github.matrixkt.models.*
import io.ktor.client.HttpClient
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlin.reflect.KProperty0

internal class KeysApiImpl(private val client: HttpClient, private val accessTokenProp: KProperty0<String>) : KeysApi {
    private inline val accessToken: String get() = accessTokenProp.get()

    override suspend fun uploadKeys(keys: UploadKeysRequest): UploadKeysResponse {
        return client.post(path = "_matrix/client/r0/keys/upload") {
            header("Authorization", "Bearer $accessToken")
            contentType(ContentType.Application.Json)
            body = keys
        }
    }

    override suspend fun queryKeys(query: QueryKeysRequest): QueryKeysResponse {
        return client.post(path = "_matrix/client/r0/keys/query") {
            header("Authorization", "Bearer $accessToken")
            contentType(ContentType.Application.Json)
            body = query
        }
    }

    override suspend fun claimKeys(query: ClaimKeysRequest): ClaimKeysResponse {
        return client.post(path = "_matrix/client/r0/keys/claim") {
            header("Authorization", "Bearer $accessToken")
            contentType(ContentType.Application.Json)
            body = query
        }
    }

    override suspend fun getKeysChanges(from: String, to: String): KeyChangesResponse {
        return client.get {
            url {
                encodedPath = "_matrix/client/r0/keys/changes"
                parameter("from", from)
                parameter("to", to)
            }
            header("Authorization", "Bearer $accessToken")
        }
    }
}
