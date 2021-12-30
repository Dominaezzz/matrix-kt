package io.github.matrixkt.utils

import io.github.matrixkt.api.Login
import io.github.matrixkt.models.AuthenticationData
import io.github.matrixkt.models.MatrixError
import io.github.matrixkt.models.UserIdentifier
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
import io.github.matrixkt.models.events.contents.secret_storage.DefaultKeyContent
import io.github.matrixkt.models.events.contents.secret_storage.KeyDescription
import io.github.matrixkt.models.events.contents.secret_storage.Passphrase
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import io.github.matrixkt.models.events.contents.secret.RequestContent as SecretRequestContent
import io.github.matrixkt.models.events.contents.secret.SendContent as SecretSendContent

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

public val ClientServerSerialModule: SerializersModule = SerializersModule {
    polymorphic(UserIdentifier::class) {
        subclass(UserIdentifier.Matrix.serializer())
        subclass(UserIdentifier.PhoneNumber.serializer())
        subclass(UserIdentifier.ThirdParty.serializer())
    }

    polymorphic(AuthenticationData::class) {
        subclass(AuthenticationData.Dummy.serializer())
        subclass(AuthenticationData.Password.serializer())
        subclass(AuthenticationData.Email.serializer())
        subclass(AuthenticationData.MSISDN.serializer())
        subclass(AuthenticationData.Token.serializer())
        subclass(AuthenticationData.GoogleRecaptcha.serializer())
    }

    polymorphic(MatrixError::class) {
        subclass(MatrixError.NotFound.serializer())
        subclass(MatrixError.TooLarge.serializer())
        subclass(MatrixError.Forbidden.serializer())
        subclass(MatrixError.InvalidUsername.serializer())
        subclass(MatrixError.UserInUse.serializer())
        subclass(MatrixError.ThreePIdNotFound.serializer())
        subclass(MatrixError.ThreePIdInUse.serializer())
        subclass(MatrixError.ThreePIdDenied.serializer())
        subclass(MatrixError.ThreePIdAuthFailed.serializer())
        subclass(MatrixError.Exclusive.serializer())
        subclass(MatrixError.UnsupportedRoomVersion.serializer())
        subclass(MatrixError.UnknownToken.serializer())
        subclass(MatrixError.MissingParam.serializer())
        subclass(MatrixError.LimitExceeded.serializer())
        subclass(MatrixError.Unknown.serializer())
        subclass(MatrixError.MissingToken.serializer())
        subclass(MatrixError.BadJson.serializer())
        subclass(MatrixError.NotJson.serializer())
        subclass(MatrixError.Unrecognized.serializer())
        subclass(MatrixError.Unauthorized.serializer())
        subclass(MatrixError.UserDeactivated.serializer())
        subclass(MatrixError.InvalidRoomState.serializer())
        subclass(MatrixError.ServerNotTrusted.serializer())
        subclass(MatrixError.IncompatibleRoomVersion.serializer())
        subclass(MatrixError.BadState.serializer())
        subclass(MatrixError.GuestAccessForbidden.serializer())
        subclass(MatrixError.CaptchaNeeded.serializer())
        subclass(MatrixError.CaptchaInvalid.serializer())
        subclass(MatrixError.InvalidParam.serializer())
        subclass(MatrixError.ResourceLimitExceeded.serializer())
        subclass(MatrixError.CannotLeaveServerNoticeRoom.serializer())
    }

    polymorphic(Login.Body::class) {
        subclass(Login.Body.Password.serializer())
        subclass(Login.Body.Token.serializer())
    }
}

public val MatrixSerialModule: SerializersModule = SerializersModule {
    include(EventSerialModule)
    include(ClientServerSerialModule)
}

public val MatrixJson: Json = Json {
    serializersModule = MatrixSerialModule
    allowStructuredMapKeys = true

    encodeDefaults = false
    isLenient = true
    ignoreUnknownKeys = true
}
