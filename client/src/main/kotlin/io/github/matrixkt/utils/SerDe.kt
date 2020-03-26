package io.github.matrixkt.utils

import io.github.matrixkt.models.events.contents.*
import io.github.matrixkt.models.events.contents.key.verification.AcceptContent
import io.github.matrixkt.models.events.contents.key.verification.StartContent
import io.github.matrixkt.models.events.contents.room.*
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
    contextual(AliasesContent.serializer())
    contextual(CanonicalAliasContent.serializer())
    contextual(CreateContent.serializer())
    contextual(JoinRulesContent.serializer())
    contextual(MemberContent.serializer())
    contextual(PowerLevelsContent.serializer())
    contextual(HistoryVisibilityContent.serializer())
    contextual(RedactionContent.serializer())
    contextual(MessageContent.serializer())
    contextual(NameContent.serializer())
    contextual(TopicContent.serializer())
    contextual(AvatarContent.serializer())
    contextual(PinnedEventsContent.serializer())
    contextual(GuestAccessContent.serializer())

    polymorphic(Content::class) {
        subclass(AliasesContent.serializer())
        subclass(CanonicalAliasContent.serializer())
        subclass(CreateContent.serializer())
        subclass(JoinRulesContent.serializer())
        subclass(MemberContent.serializer())
        subclass(PowerLevelsContent.serializer())
        subclass(HistoryVisibilityContent.serializer())
        subclass(RedactionContent.serializer())
        subclass(MessageContent.serializer())
        subclass(NameContent.serializer())
        subclass(TopicContent.serializer())
        subclass(AvatarContent.serializer())
        subclass(PinnedEventsContent.serializer())
        subclass(GuestAccessContent.serializer())
    }

    polymorphic(MessageContent.serializer()) {
        subclass(MessageContent.Text.serializer())
        subclass(MessageContent.Emote.serializer())
        subclass(MessageContent.Notice.serializer())
        subclass(MessageContent.Image.serializer())
        subclass(MessageContent.File.serializer())
        subclass(MessageContent.Audio.serializer())
        subclass(MessageContent.Location.serializer())
        subclass(MessageContent.Video.serializer())
    }

    polymorphic(EncryptedContent.serializer()) {
        subclass(EncryptedContent.OlmV1.serializer())
        subclass(EncryptedContent.MegolmV1.serializer())
    }

    polymorphic(StartContent.serializer()) {
        subclass(StartContent.SasV1.serializer())
    }

    polymorphic(AcceptContent.serializer()) {
        subclass(AcceptContent.SasV1.serializer())
    }
}

val MatrixJson = Json(
    MatrixJsonConfig,
    MatrixSerialModule
)
