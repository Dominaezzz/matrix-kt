package io.github.matrixkt

import io.github.matrixkt.apis.*
import io.github.matrixkt.models.*
import io.github.matrixkt.utils.MatrixJson
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.*
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.features.*
import io.ktor.client.features.json.Json
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.http.*
import io.ktor.utils.io.core.Closeable

class MatrixClient(engine: HttpClientEngine,
                   baseUrl: Url,
                   block: HttpClientConfig<*>.() -> Unit = {}) : Closeable {
    private val client = HttpClient(engine) {
        defaultRequest {
            val builder = URLBuilder(baseUrl)
            builder.encodedPath += url.encodedPath
            this.url.takeFrom(builder)
        }

        Json {
            serializer = KotlinxSerializer(MatrixJson)
        }

        expectSuccess = true

        HttpResponseValidator {
            handleResponseException {
                if (it !is ResponseException) return@handleResponseException
                throw it.response.receive<MatrixError>()
            }
        }

        block()
    }

    var accessToken: String = ""

    val roomApi: RoomApi = RoomApi(client, ::accessToken)
    val eventApi: EventApi = EventApi(client, ::accessToken)
    val userApi: UserApi = UserApi(client, ::accessToken)
    val contentApi: ContentApi = ContentApi(client, ::accessToken)
    val authApi: AuthApi = AuthApi(client, ::accessToken)
    val filterApi: FilterApi = FilterApi(client, ::accessToken)
    val deviceApi: DeviceApi = DeviceApi(client, ::accessToken)
    val voIPApi: VoIPApi = VoIPApi(client, ::accessToken)
    val accountApi: AccountApi = AccountApi(client, ::accessToken)
    val miscApi: MiscApi = MiscApi(client, ::accessToken)
    val pushApi: PushApi = PushApi(client, ::accessToken)
    val keysApi: KeysApi = KeysApi(client, ::accessToken)
    val adminApi: AdminApi = AdminApi(client, ::accessToken)
    val thirdPartyApi: ThirdPartyApi = ThirdPartyApi(client, ::accessToken)

    override fun close() {
        client.close()
    }
}
