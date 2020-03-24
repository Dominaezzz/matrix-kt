package io.github.matrixkt.utils

import io.github.matrixkt.models.events.contents.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual

val MatrixJsonConfig = JsonConfiguration.Stable.copy(
    encodeDefaults = false,
    isLenient = true,
    ignoreUnknownKeys = true,
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
        subclass(RoomAliasesContent.serializer())
        subclass(RoomCanonicalAliasContent.serializer())
        subclass(RoomCreateContent.serializer())
        subclass(RoomJoinRulesContent.serializer())
        subclass(RoomMemberContent.serializer())
        subclass(RoomPowerLevelsContent.serializer())
        subclass(RoomHistoryVisibilityContent.serializer())
        subclass(RoomRedactionContent.serializer())
        subclass(RoomMessageContent.serializer())
        subclass(RoomNameContent.serializer())
        subclass(RoomTopicContent.serializer())
        subclass(RoomAvatarContent.serializer())
        subclass(RoomPinnedEventsContent.serializer())
        subclass(RoomGuestAccessContent.serializer())
    }

    polymorphic(RoomMessageContent.serializer()) {
        subclass(RoomMessageContent.Text.serializer())
        subclass(RoomMessageContent.Emote.serializer())
        subclass(RoomMessageContent.Notice.serializer())
        subclass(RoomMessageContent.Image.serializer())
        subclass(RoomMessageContent.File.serializer())
        subclass(RoomMessageContent.Audio.serializer())
        subclass(RoomMessageContent.Location.serializer())
        subclass(RoomMessageContent.Video.serializer())
    }

    polymorphic(RoomEncryptedContent.serializer()) {
        subclass(RoomEncryptedContent.OlmV1.serializer())
        subclass(RoomEncryptedContent.MegolmV1.serializer())
    }
}

val MatrixJson = Json(
    MatrixJsonConfig,
    MatrixSerialModule
)
