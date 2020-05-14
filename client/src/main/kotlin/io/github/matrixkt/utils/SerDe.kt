package io.github.matrixkt.utils

import io.github.matrixkt.models.events.contents.*
import io.github.matrixkt.models.events.contents.call.AnswerContent
import io.github.matrixkt.models.events.contents.call.CandidatesContent
import io.github.matrixkt.models.events.contents.call.HangupContent
import io.github.matrixkt.models.events.contents.call.InviteContent
import io.github.matrixkt.models.events.contents.key.verification.*
import io.github.matrixkt.models.events.contents.policy.rule.RoomContent
import io.github.matrixkt.models.events.contents.policy.rule.ServerContent
import io.github.matrixkt.models.events.contents.policy.rule.UserContent
import io.github.matrixkt.models.events.contents.room.*
import io.github.matrixkt.models.events.contents.room.message.FeedbackContent
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
        subclass(EncryptionContent.serializer())
        subclass(EncryptedContent.serializer())
        subclass(ServerAclContent.serializer())
        subclass(ThirdPartyInviteContent.serializer())
        subclass(TombstoneContent.serializer())
        subclass(StickerContent.serializer())
        subclass(InviteContent.serializer())
        subclass(CandidatesContent.serializer())
        subclass(AnswerContent.serializer())
        subclass(HangupContent.serializer())
        subclass(TypingContent.serializer())
        subclass(ReceiptContent.serializer())
        subclass(FullyReadContent.serializer())
        subclass(PresenceContent.serializer())
        subclass(RoomKeyContent.serializer())
        subclass(RoomKeyRequestContent.serializer())
        subclass(ForwardedRoomKeyContent.serializer())
        subclass(DummyContent.serializer())
        subclass(TagContent.serializer())
        subclass(DirectContent.serializer())
        subclass(AcceptedTermsContent.serializer())
        subclass(RequestContent.serializer())
        subclass(StartContent.serializer())
        subclass(CancelContent.serializer())
        subclass(AcceptContent.serializer())
        subclass(KeyContent.serializer())
        subclass(MacContent.serializer())
        subclass(IdentityServerContent.serializer())
        subclass(IgnoredUserListContent.serializer())
        subclass(PushRulesContent.serializer())
        subclass(RoomContent.serializer())
        subclass(ServerContent.serializer())
        subclass(UserContent.serializer())
        // subclass(FeedbackContent.serializer())
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
        subclass(MessageContent.ServerNotice.serializer())
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
