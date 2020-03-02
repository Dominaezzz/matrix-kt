package io.github.matrixkt

import io.github.matrixkt.apis.*
import io.github.matrixkt.impls.*
import io.github.matrixkt.models.*
import io.github.matrixkt.models.events.MatrixEvent
import io.github.matrixkt.models.events.contents.RoomRedactionContent
import io.github.matrixkt.models.filter.Filter
import io.github.matrixkt.models.push.PushRule
import io.github.matrixkt.models.wellknown.DiscoveryInformation
import io.github.matrixkt.utils.MatrixJson
import io.github.matrixkt.utils.MatrixJsonConfig
import io.github.matrixkt.utils.MatrixSerialModule
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.features.HttpCallValidator
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.host
import io.ktor.client.statement.readBytes
import io.ktor.http.HttpStatusCode
import io.ktor.utils.io.core.Closeable
import io.ktor.utils.io.core.String
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.modules.SerializersModule

class MatrixClient(engine: HttpClientEngine, host: String = "matrix.org") : Closeable {
    private val client = HttpClient(engine) {
        val json = Json(
            MatrixJsonConfig.copy(classDiscriminator = "errcode"),
            SerializersModule {
                include(MatrixSerialModule)

                polymorphic<MatrixError> {
                    addSubclass(MatrixError.Unknown.serializer())
                    addSubclass(MatrixError.NotFound.serializer())
                    addSubclass(MatrixError.Forbidden.serializer())
                    addSubclass(MatrixError.UnsupportedRoomVersion.serializer())
                    addSubclass(MatrixError.LimitExceeded.serializer())
                    addSubclass(MatrixError.TooLarge.serializer())
                    addSubclass(MatrixError.UserInUse.serializer())
                    addSubclass(MatrixError.Exclusive.serializer())
                    addSubclass(MatrixError.InvalidUsername.serializer())
                    addSubclass(MatrixError.ThreePIdDenied.serializer())
                    addSubclass(MatrixError.ThreePIdInUse.serializer())
                    addSubclass(MatrixError.UnknownToken.serializer())
                    addSubclass(MatrixError.ThreePIdNotFound.serializer())
                    addSubclass(MatrixError.ThreePIdAuthFailed.serializer())
                    addSubclass(MatrixError.MissingParam.serializer())
                }
            }
        )
        install(JsonFeature) {
            serializer = KotlinxSerializer(json)
        }
        install(HttpCallValidator) {
            validateResponse {
                if (it.status != HttpStatusCode.OK) {
                    val errorJson = String(it.readBytes())
                    throw json.parse(MatrixError.serializer(), errorJson) // it.receive<MatrixError>()
                }
            }
        }

        expectSuccess = false

        defaultRequest {
            this.host = host
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
