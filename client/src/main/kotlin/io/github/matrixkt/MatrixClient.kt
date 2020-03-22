package io.github.matrixkt

import io.github.matrixkt.apis.*
import io.github.matrixkt.models.*
import io.github.matrixkt.utils.MatrixJson
import io.github.matrixkt.utils.MatrixJsonConfig
import io.github.matrixkt.utils.MatrixSerialModule
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.features.HttpResponseValidator
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.Json
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.host
import io.ktor.client.statement.readBytes
import io.ktor.http.HttpStatusCode
import io.ktor.utils.io.core.Closeable
import io.ktor.utils.io.core.String
import kotlinx.serialization.json.Json

class MatrixClient(engine: HttpClientEngine,
                   host: String = "matrix.org",
                   block: HttpClientConfig<*>.() -> Unit = {}) : Closeable {
    private val client = HttpClient(engine) {
        expectSuccess = false

        defaultRequest {
            this.host = host
        }

        Json {
            serializer = KotlinxSerializer(MatrixJson)
        }

        HttpResponseValidator {
            val json = Json(
                MatrixJsonConfig.copy(classDiscriminator = "errcode"),
                MatrixSerialModule
            )
            validateResponse {
                if (it.status != HttpStatusCode.OK) {
                    val errorJson = String(it.readBytes())
                    throw json.parse(MatrixError.serializer(), errorJson) // it.receive<MatrixError>()
                }
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

    override fun close() {
        client.close()
    }
}
