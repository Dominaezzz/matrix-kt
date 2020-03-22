package io.github.matrixkt.apis

import io.github.matrixkt.models.TurnServerResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.post
import kotlin.reflect.KProperty0

class VoIPApi internal constructor(private val client: HttpClient, private val accessTokenProp: KProperty0<String>) {
    private inline val accessToken: String get() = accessTokenProp.get()

    /**
     * This API provides credentials for the client to use when initiating calls.
     *
     * **Rate-limited**: Yes.
     *
     * **Requires auth**: Yes.
     */
    suspend fun getTurnServer(): TurnServerResponse {
        return client.post("/_matrix/client/r0/voip/turnServer") {
            header("Authorization", "Bearer $accessToken")
        }
    }
}
