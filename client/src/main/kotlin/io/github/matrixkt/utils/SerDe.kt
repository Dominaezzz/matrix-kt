package io.github.matrixkt.utils

import io.github.matrixkt.models.AuthenticationData
import io.github.matrixkt.models.UserIdentifier
import io.github.matrixkt.models.events.*
import io.github.matrixkt.models.events.contents.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual

val MatrixJsonConfig = JsonConfiguration.Stable.copy(
    encodeDefaults = false,
    strictMode = false,
    classDiscriminator = "type"
)

val MatrixSerialModule = SerializersModule {
    contextual(RoomAliasesContent.serializer())
    contextual(RoomCanonicalAliasContent.serializer())
    contextual(RoomCreateContent.serializer())
    contextual(RoomJoinRulesContent.serializer())
    contextual(RoomMemberContent.serializer())
    contextual(RoomPowerLevelsContent.serializer())
    contextual(RoomHistoryVisibilityContent.serializer())
    contextual(RoomRedactionContent.serializer())
    contextual(RoomMessageContent.serializer())
    contextual(RoomNameContent.serializer())
    contextual(RoomTopicContent.serializer())
    contextual(RoomAvatarContent.serializer())
    contextual(RoomPinnedEventsContent.serializer())
    contextual(RoomGuestAccessContent.serializer())

    polymorphic(Content::class) {
        addSubclass(RoomAliasesContent.serializer())
        addSubclass(RoomCanonicalAliasContent.serializer())
        addSubclass(RoomCreateContent.serializer())
        addSubclass(RoomJoinRulesContent.serializer())
        addSubclass(RoomMemberContent.serializer())
        addSubclass(RoomPowerLevelsContent.serializer())
        addSubclass(RoomHistoryVisibilityContent.serializer())
        addSubclass(RoomRedactionContent.serializer())
        addSubclass(RoomMessageContent.serializer())
        addSubclass(RoomNameContent.serializer())
        addSubclass(RoomTopicContent.serializer())
        addSubclass(RoomAvatarContent.serializer())
        addSubclass(RoomPinnedEventsContent.serializer())
        addSubclass(RoomGuestAccessContent.serializer())
    }

    polymorphic(RoomMessageContent.serializer()) {
        addSubclass(RoomMessageContent.Text.serializer())
        addSubclass(RoomMessageContent.Emote.serializer())
        addSubclass(RoomMessageContent.Notice.serializer())
        addSubclass(RoomMessageContent.Image.serializer())
        addSubclass(RoomMessageContent.File.serializer())
        addSubclass(RoomMessageContent.Audio.serializer())
        addSubclass(RoomMessageContent.Location.serializer())
        addSubclass(RoomMessageContent.Video.serializer())
    }

    polymorphic(UserIdentifier.serializer()) {
        addSubclass(UserIdentifier.Matrix.serializer())
        addSubclass(UserIdentifier.PhoneNumber.serializer())
        addSubclass(UserIdentifier.ThirdParty.serializer())
    }

    polymorphic(AuthenticationData.serializer()) {
        addSubclass(AuthenticationData.Password.serializer())
        addSubclass(AuthenticationData.GoogleRecaptcha.serializer())
        addSubclass(AuthenticationData.Token.serializer())
        addSubclass(AuthenticationData.Email.serializer())
        addSubclass(AuthenticationData.MSISDN.serializer())
        addSubclass(AuthenticationData.Dummy.serializer())
    }
}

val MatrixJson = Json(
    MatrixJsonConfig,
    MatrixSerialModule
)
