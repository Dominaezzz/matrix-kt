@file:JvmName("EventsSerDe")

package io.github.matrixkt.utils

import io.github.matrixkt.events.contents.*
import io.github.matrixkt.events.contents.call.AnswerContent
import io.github.matrixkt.events.contents.call.CandidatesContent
import io.github.matrixkt.events.contents.call.HangupContent
import io.github.matrixkt.events.contents.call.InviteContent
import io.github.matrixkt.events.contents.key.verification.*
import io.github.matrixkt.events.contents.policy.rule.RoomContent
import io.github.matrixkt.events.contents.policy.rule.ServerContent
import io.github.matrixkt.events.contents.policy.rule.UserContent
import io.github.matrixkt.events.contents.room.*
import io.github.matrixkt.events.contents.room.message.FeedbackContent
import io.github.matrixkt.events.contents.secret_storage.DefaultKeyContent
import io.github.matrixkt.events.contents.secret_storage.KeyDescription
import io.github.matrixkt.events.contents.secret_storage.Passphrase
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import kotlin.jvm.JvmName
import io.github.matrixkt.events.contents.secret.RequestContent as SecretRequestContent
import io.github.matrixkt.events.contents.secret.SendContent as SecretSendContent

public val EventSerialModule: SerializersModule = SerializersModule {
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
    contextual(EncryptionContent.serializer())
    contextual(EncryptedContent.serializer())
    contextual(ServerAclContent.serializer())
    contextual(ThirdPartyInviteContent.serializer())
    contextual(TombstoneContent.serializer())
    contextual(StickerContent.serializer())
    contextual(InviteContent.serializer())
    contextual(CandidatesContent.serializer())
    contextual(AnswerContent.serializer())
    contextual(HangupContent.serializer())
    contextual(TypingContent.serializer())
    contextual(ReceiptContent.serializer())
    contextual(FullyReadContent.serializer())
    contextual(PresenceContent.serializer())
    contextual(RoomKeyContent.serializer())
    contextual(RoomKeyRequestContent.serializer())
    contextual(ForwardedRoomKeyContent.serializer())
    contextual(DummyContent.serializer())
    contextual(TagContent.serializer())
    contextual(DirectContent.serializer())
    contextual(AcceptedTermsContent.serializer())
    contextual(RequestContent.ToDevice.serializer())
    contextual(RequestContent.InRoom.serializer())
    contextual(StartContent.ToDevice.serializer())
    contextual(StartContent.InRoom.serializer())
    contextual(DoneContent.ToDevice.serializer())
    contextual(DoneContent.InRoom.serializer())
    contextual(CancelContent.ToDevice.serializer())
    contextual(CancelContent.InRoom.serializer())
    contextual(AcceptContent.ToDevice.serializer())
    contextual(AcceptContent.InRoom.serializer())
    contextual(KeyContent.ToDevice.serializer())
    contextual(KeyContent.InRoom.serializer())
    contextual(MacContent.ToDevice.serializer())
    contextual(MacContent.InRoom.serializer())
    contextual(IdentityServerContent.serializer())
    contextual(IgnoredUserListContent.serializer())
    contextual(PushRulesContent.serializer())
    contextual(RoomContent.serializer())
    contextual(ServerContent.serializer())
    contextual(UserContent.serializer())
    contextual(FeedbackContent.serializer())
    contextual(DefaultKeyContent.serializer())
    contextual(SecretRequestContent.serializer())
    contextual(SecretSendContent.serializer())

    polymorphic(MessageContent::class) {
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

    polymorphic(EncryptedContent::class) {
        subclass(EncryptedContent.OlmV1.serializer())
        subclass(EncryptedContent.MegolmV1.serializer())
    }

    polymorphic(StartContent.ToDevice::class) {
        subclass(StartContent.SasV1.ToDevice.serializer())
        subclass(StartContent.ReciprocateV1.ToDevice.serializer())
    }

    polymorphic(StartContent.InRoom::class) {
        subclass(StartContent.SasV1.InRoom.serializer())
        subclass(StartContent.ReciprocateV1.InRoom.serializer())
    }

    polymorphic(AcceptContent.ToDevice::class) {
        subclass(AcceptContent.SasV1.ToDevice.serializer())
    }

    polymorphic(AcceptContent.InRoom::class) {
        subclass(AcceptContent.SasV1.InRoom.serializer())
    }

    polymorphic(KeyDescription::class) {
        subclass(KeyDescription.AesHmacSha2.serializer())
    }

    polymorphic(Passphrase::class) {
        subclass(Passphrase.PBKDF2.serializer())
    }
}
