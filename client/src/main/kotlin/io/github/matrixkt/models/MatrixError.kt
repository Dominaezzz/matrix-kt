package io.github.matrixkt.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
sealed class MatrixError : Exception() {
    override var message: String?
        get() = error
        set(value) {}

    abstract val error: String?

    @Serializable
    @SerialName("M_NOT_FOUND")
    data class NotFound(override val error: String? = null) : MatrixError()

    @Serializable
    @SerialName("M_TOO_LARGE")
    data class TooLarge(override val error: String? = null) : MatrixError()

    @Serializable
    @SerialName("M_FORBIDDEN")
    data class Forbidden(override val error: String? = null) : MatrixError()

    @Serializable
    @SerialName("M_INVALID_USERNAME")
    data class InvalidUsername(override val error: String? = null) : MatrixError()

    @Serializable
    @SerialName("M_USER_IN_USE")
    data class UserInUse(override val error: String? = null) : MatrixError()

    @Serializable
    @SerialName("M_THREEPID_NOT_FOUND")
    data class ThreePIdNotFound(override val error: String? = null) : MatrixError()

    @Serializable
    @SerialName("M_THREEPID_IN_USE")
    data class ThreePIdInUse(override val error: String? = null) : MatrixError()

    @Serializable
    @SerialName("M_THREEPID_DENIED")
    data class ThreePIdDenied(override val error: String? = null) : MatrixError()

    @Serializable
    @SerialName("M_THREEPID_AUTH_FAILED")
    data class ThreePIdAuthFailed(override val error: String? = null) : MatrixError()

    @Serializable
    @SerialName("M_EXCLUSIVE")
    data class Exclusive(override val error: String? = null) : MatrixError()

    @Serializable
    @SerialName("M_UNSUPPORTED_ROOM_VERSION")
    data class UnsupportedRoomVersion(override val error: String? = null) : MatrixError()

    @Serializable
    @SerialName("M_UNKNOWN_TOKEN")
    data class UnknownToken(override val error: String? = null) : MatrixError()

    @Serializable
    @SerialName("M_MISSING_PARAM")
    data class MissingParam(override val error: String? = null) : MatrixError()

    @Serializable
    @SerialName("M_LIMIT_EXCEEDED")
    data class LimitExceeded(
        override val error: String? = null,

        @SerialName("retry_after_ms")
        val retryAfterMillis: Long
    ) : MatrixError()

    @Serializable
    @SerialName("M_UNKNOWN")
    data class Unknown(override val error: String? = null) : MatrixError()
}
