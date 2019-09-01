package io.github.matrixkt.impls

import io.github.matrixkt.apis.MiscApi
import io.github.matrixkt.models.wellknown.DiscoveryInformation
import io.github.matrixkt.models.GetCapabilitiesResponse
import io.github.matrixkt.models.MatrixError
import io.github.matrixkt.models.Versions
import io.ktor.client.HttpClient
import io.ktor.client.call.receive
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.response.HttpResponse
import io.ktor.http.HttpStatusCode
import kotlinx.io.core.use
import kotlin.reflect.KProperty0

internal class MiscApiImpl(private val client: HttpClient, private val accessTokenProp: KProperty0<String>) : MiscApi {
    private inline val accessToken: String get() = accessTokenProp.get()

    override suspend fun getVersions(): Versions {
        val response = client.get<HttpResponse>("/_matrix/client/versions")

        response.use {
            when (it.status) {
                HttpStatusCode.OK -> return it.receive()
                else -> throw it.receive<MatrixError>()
            }
        }
    }

    override suspend fun getWellKnown(): DiscoveryInformation {
        val response = client.get<HttpResponse>("/.well-known/matrix/client")

        response.use {
            when (it.status) {
                HttpStatusCode.OK -> return it.receive()
                else -> throw it.receive<MatrixError>()
            }
        }
    }

    override suspend fun getCapabilities(): GetCapabilitiesResponse {
        val response = client.get<HttpResponse>("/_matrix/client/r0/capabilities") {
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
