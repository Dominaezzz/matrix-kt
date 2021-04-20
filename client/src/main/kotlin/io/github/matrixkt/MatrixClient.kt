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

public class MatrixClient(engine: HttpClientEngine,
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
                throw MatrixException(it.response.receive())
            }
        }

        block()
    }

    public var accessToken: String = ""

    public val roomApi: RoomApi = RoomApi(client, ::accessToken)
    public val eventApi: EventApi = EventApi(client, ::accessToken)
    public val userApi: UserApi = UserApi(client, ::accessToken)
    public val contentApi: ContentApi = ContentApi(client, ::accessToken)
    public val authApi: AuthApi = AuthApi(client, ::accessToken)
    public val filterApi: FilterApi = FilterApi(client, ::accessToken)
    public val deviceApi: DeviceApi = DeviceApi(client, ::accessToken)
    public val voIPApi: VoIPApi = VoIPApi(client, ::accessToken)
    public val accountApi: AccountApi = AccountApi(client, ::accessToken)
    public val miscApi: MiscApi = MiscApi(client, ::accessToken)
    public val pushApi: PushApi = PushApi(client, ::accessToken)
    public val keysApi: KeysApi = KeysApi(client, ::accessToken)
    public val adminApi: AdminApi = AdminApi(client, ::accessToken)
    public val thirdPartyApi: ThirdPartyApi = ThirdPartyApi(client, ::accessToken)

    override fun close() {
        client.close()
    }
}
