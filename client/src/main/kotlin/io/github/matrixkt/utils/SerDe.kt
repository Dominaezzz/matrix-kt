@file:JvmName("ClientSerDe")

package io.github.matrixkt.utils

import io.github.matrixkt.api.Login
import io.github.matrixkt.models.AuthenticationData
import io.github.matrixkt.models.MatrixError
import io.github.matrixkt.models.UserIdentifier
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import kotlin.jvm.JvmName

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
