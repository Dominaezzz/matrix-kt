package io.github.matrixkt.impls

import io.github.matrixkt.apis.VoIPApi
import io.github.matrixkt.models.MatrixError
import io.github.matrixkt.models.TurnServerResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.receive
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.response.HttpResponse
import io.ktor.http.HttpStatusCode
import kotlinx.io.core.use
import kotlin.reflect.KProperty0

internal class VoIPApiImpl(private val client: HttpClient, private val accessTokenProp: KProperty0<String>) : VoIPApi {
    private inline val accessToken: String get() = accessTokenProp.get()

    override suspend fun getTurnServer(): TurnServerResponse {
        val response = client.post<HttpResponse>("/_matrix/client/r0/voip/turnServer") {
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
