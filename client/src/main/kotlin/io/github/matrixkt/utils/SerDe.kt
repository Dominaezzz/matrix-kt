package io.github.matrixkt.utils

import io.github.matrixkt.models.AuthenticationData
import io.github.matrixkt.models.LoginRequest
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
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

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

    polymorphic(Content::class, null) {
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
        // subclass(RoomKeyRequestContent.serializer())
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

    polymorphic(MessageContent::class, MessageContent.serializer()) {
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

    polymorphic(EncryptedContent::class, EncryptedContent.serializer()) {
        subclass(EncryptedContent.OlmV1.serializer())
        subclass(EncryptedContent.MegolmV1.serializer())
    }

    polymorphic(StartContent::class, StartContent.serializer()) {
        subclass(StartContent.SasV1.serializer())
    }

    polymorphic(AcceptContent::class, AcceptContent.serializer()) {
        subclass(AcceptContent.SasV1.serializer())
    }

    polymorphic(RoomKeyRequestContent::class) {
        subclass(RoomKeyRequestContent.Request.serializer())
        subclass(RoomKeyRequestContent.Cancellation.serializer())
    }

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

    polymorphic(LoginRequest::class) {
        subclass(LoginRequest.Password.serializer())
        subclass(LoginRequest.Token.serializer())
    }
}

val MatrixJson = Json {
    serializersModule = MatrixSerialModule
    allowStructuredMapKeys = true

    encodeDefaults = false
    isLenient = true
    ignoreUnknownKeys = true
    classDiscriminator = "type"
}
