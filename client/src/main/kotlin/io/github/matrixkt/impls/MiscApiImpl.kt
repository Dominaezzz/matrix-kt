package io.github.matrixkt.impls

import io.github.matrixkt.apis.MiscApi
import io.github.matrixkt.models.wellknown.DiscoveryInformation
import io.github.matrixkt.models.GetCapabilitiesResponse
import io.github.matrixkt.models.Versions
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import kotlin.reflect.KProperty0

internal class MiscApiImpl(private val client: HttpClient, private val accessTokenProp: KProperty0<String>) : MiscApi {
    private inline val accessToken: String get() = accessTokenProp.get()

    override suspend fun getVersions(): Versions {
        return client.get("/_matrix/client/versions")
    }

    override suspend fun getWellKnown(): DiscoveryInformation {
        return client.get("/.well-known/matrix/client")
    }

    override suspend fun getCapabilities(): GetCapabilitiesResponse {
        return client.get("/_matrix/client/r0/capabilities") {
            header("Authorization", "Bearer $accessToken")
        }
    }
}
