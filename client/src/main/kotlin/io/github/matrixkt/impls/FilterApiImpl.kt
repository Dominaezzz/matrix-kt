package io.github.matrixkt.impls

import io.github.matrixkt.apis.FilterApi
import io.github.matrixkt.models.filter.Filter
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.content
import kotlin.reflect.KProperty0

internal class FilterApiImpl(private val client: HttpClient, private val accessTokenProp: KProperty0<String>) : FilterApi {
    private inline val accessToken: String get() = accessTokenProp.get()

    override suspend fun defineFilter(userId: String, filter: Filter): String {
        val response = client.post<JsonObject>{
            url {
                path("_matrix", "client", "r0", "user", userId, "filter")
            }
            header("Authorization", "Bearer $accessToken")

            contentType(ContentType.Application.Json)
            body = filter
        }
        return response["filter_id"]!!.content
    }

    override suspend fun getFilter(userId: String, filterId: String): Filter {
        return client.get {
            url {
                path("_matrix", "client", "r0", "user", userId, "filter", filterId)
            }
            header("Authorization", "Bearer $accessToken")
        }
    }
}
