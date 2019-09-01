package io.github.matrixkt

import io.github.matrixkt.apis.*
import io.github.matrixkt.impls.*
import io.github.matrixkt.models.*
import io.github.matrixkt.models.events.MatrixEvent
import io.github.matrixkt.models.events.contents.RoomRedactionContent
import io.github.matrixkt.models.filter.Filter
import io.github.matrixkt.models.push.PushRule
import io.github.matrixkt.models.wellknown.DiscoveryInformation
import io.github.matrixkt.utils.MatrixJsonConfig
import io.github.matrixkt.utils.MatrixSerialModule
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.host
import kotlinx.io.core.Closeable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.modules.SerializersModule

class MatrixClient(engine: HttpClientEngine, host: String = "matrix.org") : Closeable {
    private val client = HttpClient(engine) {
        install(JsonFeature) {
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

            serializer = KotlinxSerializer(json).apply {
                register(MatrixError.serializer())
                register(JsonElement.serializer())
                register(GetMembersResponse.serializer())
                register(MessagesResponse.serializer())
                register(SendStateEventResponse.serializer())
                register(SendMessageEventResponse.serializer())
                register(CreateRoomResponse.serializer())
                register(ResolveRoomAliasResponse.serializer())
                register(GetJoinedRoomsResponse.serializer())
                register(JoinRoomRequest.serializer())
                register(JoinRoomResponse.serializer())
                register(KickRequest.serializer())
                register(UnBanRequest.serializer())
                register(VisibilityResponse.serializer())
                register(VisibilityRequest.serializer())
                register(PublicRoomsResponse.serializer())
                register(SearchPublicRoomsRequest.serializer())
                register(UpgradeRoomRequest.serializer())
                register(UpgradeRoomResponse.serializer())
                register(SyncResponse.serializer())
                register(SearchUsersRequest.serializer())
                register(SearchUsersResponse.serializer())
                register(SetDisplayNameRequest.serializer())
                register(GetDisplayNameResponse.serializer())
                register(GetAvatarUrlResponse.serializer())
                register(SetAvatarUrlRequest.serializer())
                register(GetUserProfileResponse.serializer())
                register(UploadResponse.serializer())
                register(UrlPreviewResponse.serializer())
                register(ConfigResponse.serializer())
                register(LoginFlowsResponse.serializer())
                register(LoginFlow.serializer())
                register(RegisterRequest.serializer())
                register(RegisterResponse.serializer())
                register(EmailValidationRequest.serializer())
                register(TokenValidationResponse.serializer())
                register(MSISDNValidationRequest.serializer())
                register(ChangePasswordRequest.serializer())
                register(DeactivateRequest.serializer())
                register(DeactivateResponse.serializer())
                register(Filter.serializer())
                register(TurnServerResponse.serializer())
                register(MatrixEvent.serializer())
                register(Remove3PidRequest.serializer())
                register(Remove3PidResponse.serializer())
                register(Get3PidsResponse.serializer())
                register(BanRequest.serializer())
                register(Capabilities.serializer())
                register(CreateRoomAliasRequest.serializer())
                register(GetDevicesResponse.serializer())
                register(Device.serializer())
                register(PushRule.serializer())
                register(PushRuleActions.serializer())
                register(Pushers.serializer())
                register(GetPresenceResponse.serializer())
                register(Versions.serializer())
                register(InviteRequest.serializer())
                register(PushRuleEnabled.serializer())
                register(KeyChangesResponse.serializer())
                register(UploadKeysRequest.serializer())
                register(UploadKeysResponse.serializer())
                register(LoginRequest.serializer())
                register(LoginResponse.serializer())
                register(ReadMarkersRequest.serializer())
                register(SetPresenceRequest.serializer())
                register(SyncResponse.serializer())
                register(TypingRequest.serializer())
                register(DiscoveryInformation.serializer())
                register(WhoAmIResponse.serializer())
                register(GetCapabilitiesResponse.serializer())
                register(JsonObject.serializer())
                register(QueryKeysRequest.serializer())
                register(QueryKeysResponse.serializer())
                register(GetPushRulesResponse.serializer())
                register(GetMembersResponse.serializer())
                register(DeviceKeys.serializer())
                register(SendStateEventResponse.serializer())
                register(Pusher.serializer())
                register(SetPushRuleRequest.serializer())
                register(JoinedMembersResponse.serializer())
                register(RoomRedactionContent.serializer())
                register(Add3PidRequest.serializer())
                register(SetPushRuleRequest.serializer())
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
