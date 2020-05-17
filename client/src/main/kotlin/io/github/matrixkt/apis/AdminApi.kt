package io.github.matrixkt.apis

import io.github.matrixkt.models.admin.WhoIsResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import kotlin.reflect.KProperty0

class AdminApi internal constructor(private val client: HttpClient, private val accessTokenProp: KProperty0<String>) {
    private inline val accessToken: String get() = accessTokenProp.get()

    /**
     * Gets information about a particular user.
     *
     * This API may be restricted to only be called by the user being looked up, or by a server admin.
     * Server-local administrator privileges are not specified in this document.
     *
     * **Rate-limited**: No.
     *
     * **Requires auth**: Yes.
     *
     * @param[userId#] The user to look up.
     */
    suspend fun getWhoIs(userId: String): WhoIsResponse {
        return client.get {
            url {
                path("/_matrix", "client", "r0", "admin", "whois", userId)
            }

            header("Authorization", "Bearer $accessToken")
        }
    }
}
