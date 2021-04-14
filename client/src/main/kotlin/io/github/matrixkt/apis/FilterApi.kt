package io.github.matrixkt.apis

import io.github.matrixkt.models.filter.Filter
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlin.reflect.KProperty0

public class FilterApi internal constructor(private val client: HttpClient, private val accessTokenProp: KProperty0<String>) {
    private inline val accessToken: String get() = accessTokenProp.get()

    /**
     * Uploads a new filter definition to the homeserver.
     * Returns a filter ID that may be used in future requests to restrict which events are returned to the client.
     *
     * **Rate-limited**: No.
     *
     * **Requires auth**: Yes.
     *
     * @param[userId] The id of the user uploading the filter. The access token must be authorized to make requests for this user id.
     * @return The ID of the filter that was created.
     * Cannot start with a '{' as this character is used to determine if the filter
     * provided is inline JSON or a previously declared filter by homeservers on some APIs.
     */
    public suspend fun defineFilter(userId: String, filter: Filter): String {
        val response = client.post<JsonObject> {
            url {
                path("_matrix", "client", "r0", "user", userId, "filter")
            }
            header("Authorization", "Bearer $accessToken")

            contentType(ContentType.Application.Json)
            body = filter
        }
        return response["filter_id"]!!.jsonPrimitive.content
    }

    /**
     * Download a filter.
     *
     * **Rate-limited**: No.
     *
     * **Requires auth**: Yes.
     *
     * @param[userId] The user ID to download a filter for.
     * @param[filterId] The filter ID to download.
     */
    public suspend fun getFilter(userId: String, filterId: String): Filter {
        return client.get {
            url {
                path("_matrix", "client", "r0", "user", userId, "filter", filterId)
            }
            header("Authorization", "Bearer $accessToken")
        }
    }
}
