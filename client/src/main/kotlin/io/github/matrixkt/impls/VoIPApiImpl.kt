package io.github.matrixkt.impls

import io.github.matrixkt.apis.VoIPApi
import io.github.matrixkt.models.TurnServerResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.post
import kotlin.reflect.KProperty0

internal class VoIPApiImpl(private val client: HttpClient, private val accessTokenProp: KProperty0<String>) : VoIPApi {
    private inline val accessToken: String get() = accessTokenProp.get()

    override suspend fun getTurnServer(): TurnServerResponse {
        return client.post("/_matrix/client/r0/voip/turnServer") {
            header("Authorization", "Bearer $accessToken")
        }
    }
}
