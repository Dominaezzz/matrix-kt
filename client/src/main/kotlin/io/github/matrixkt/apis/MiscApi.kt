package io.github.matrixkt.apis

import io.github.matrixkt.models.wellknown.DiscoveryInformation
import io.github.matrixkt.models.GetCapabilitiesResponse
import io.github.matrixkt.models.Versions
import io.github.matrixkt.models.notifications.NotificationsResponse
import io.github.matrixkt.models.search.Results
import io.github.matrixkt.models.search.SearchRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlin.reflect.KProperty0

class MiscApi internal constructor(private val client: HttpClient, private val accessTokenProp: KProperty0<String>) {
    private inline val accessToken: String get() = accessTokenProp.get()

    /**
     * Gets the versions of the specification supported by the server.
     *
     * Values will take the form `rX.Y.Z`.
     *
     * Only the latest `Z` value will be reported for each supported `X.Y` value.
     * i.e. if the server implements `r0.0.0`, `r0.0.1`, and `r1.2.0`, it will report `r0.0.1` and `r1.2.0`.
     *
     * The server may additionally advertise experimental features it supports through `unstable_features`.
     * These features should be namespaced and may optionally include version information within their name if desired.
     * Features listed here are not for optionally toggling parts of the Matrix specification and should only be used to
     * advertise support for a feature which has not yet landed in the spec.
     * For example, a feature currently undergoing the proposal process may appear here and eventually be taken
     * off this list once the feature lands in the spec and the server deems it reasonable to do so.
     * Servers may wish to keep advertising features here after they've been released into the spec to
     * give clients a chance to upgrade appropriately.
     * Additionally, clients should avoid using unstable features in their stable releases.
     *
     * **Rate-limited**: No.
     *
     * **Requires auth**: No.
     */
    suspend fun getVersions(): Versions {
        return client.get("/_matrix/client/versions")
    }

    /**
     * Gets discovery information about the domain.
     * The file may include additional keys, which MUST follow the Java package naming convention, e.g. com.example.myapp.property.
     * This ensures property names are suitably namespaced for each application and reduces the risk of clashes.
     *
     * Note that this endpoint is not necessarily handled by the homeserver, but by another webserver, to be used for discovering the homeserver URL.
     *
     * **Rate-limited**: No.
     *
     * **Requires auth**: No.
     */
    suspend fun getWellKnown(): DiscoveryInformation {
        return client.get("/.well-known/matrix/client")
    }

    /**
     * Gets information about the server's supported feature set and other relevant capabilities.
     *
     * **Rate-limited**: Yes.
     *
     * **Requires auth**: Yes.
     */
    suspend fun getCapabilities(): GetCapabilitiesResponse {
        return client.get("/_matrix/client/r0/capabilities") {
            header("Authorization", "Bearer $accessToken")
        }
    }

    /**
     * Performs a full text search across different categories.
     *
     * **Rate-limited**: Yes.
     *
     * **Requires auth**: Yes.
     *
     * @param[nextBatch] The point to return events from.If given, this should be a next_batch result from a previous call to this endpoint.
     */
    suspend fun search(nextBatch: String? = null, body: SearchRequest): Results {
        return client.post("/_matrix/client/r0/search") {
            parameter("next_batch", nextBatch)

            header("Authorization", "Bearer $accessToken")

            contentType(ContentType.Application.Json)
            this.body = body
        }
    }

    /**
     * This API is used to paginate through the list of events that the user has been, or would have been notified about.
     *
     * **Rate-limited**: No.
     *
     * **Requires auth**: Yes.
     *
     * @param[from] Pagination token given to retrieve the next set of events.
     * @param[limit] Limit on the number of events to return in this request.
     * @param[only] Allows basic filtering of events returned. Supply `highlight` to return only events where the notification had the highlight tweak set.
     */
    suspend fun getNotifications(from: String? = null, limit: Int? = null, only: String? = null): NotificationsResponse {
        return client.get("/_matrix/client/r0/notifications") {
            parameter("from", from)
            parameter("limit", limit)
            parameter("only", only)

            header("Authorization", "Bearer $accessToken")
        }
    }
}
