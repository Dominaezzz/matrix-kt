package io.github.matrixkt

import io.github.matrixkt.apis.*
import io.github.matrixkt.impls.*
import io.github.matrixkt.models.*
import io.github.matrixkt.utils.MatrixJson
import io.github.matrixkt.utils.MatrixJsonConfig
import io.github.matrixkt.utils.MatrixSerialModule
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.features.HttpCallValidator
import io.ktor.client.features.HttpResponseValidator
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.Json
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.host
import io.ktor.client.statement.readBytes
import io.ktor.http.HttpStatusCode
import io.ktor.utils.io.core.Closeable
import io.ktor.utils.io.core.String
import kotlinx.serialization.json.Json

class MatrixClient(engine: HttpClientEngine, host: String = "matrix.org") : Closeable {
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
    }

    var accessToken: String = ""

    val roomApi: RoomApi = RoomApiImpl(client, ::accessToken)
    val eventApi: EventApi = EventApiImpl(client, ::accessToken)
    val userApi: UserApi = UserApiImpl(client, ::accessToken)
    val contentApi: ContentApi = ContentApiImpl(client, ::accessToken)
    val authApi: AuthApi = AuthApiImpl(client, ::accessToken)
    val filterApi: FilterApi = FilterApiImpl(client, ::accessToken)
    val deviceApi: DeviceApi = DeviceApiImpl(client, ::accessToken)
    val voIPApi: VoIPApi = VoIPApiImpl(client, ::accessToken)
    val accountApi: AccountApi = AccountApiImpl(client, ::accessToken)
    val miscApi: MiscApi = MiscApiImpl(client, ::accessToken)
    val pushApi: PushApi = PushApiImpl(client, ::accessToken)
    val keysApi: KeysApi = KeysApiImpl(client, ::accessToken)

    override fun close() {
        client.close()
    }
}
