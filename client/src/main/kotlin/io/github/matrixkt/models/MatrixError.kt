package io.github.matrixkt.models

import kotlinx.serialization.*
import kotlinx.serialization.json.JsonClassDiscriminator


@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("errcode")
@Serializable
public abstract class MatrixError {
    public abstract val error: String?

    /**
     * No resource was found for this request.
     */
    @Serializable
    @SerialName("M_NOT_FOUND")
    public data class NotFound(override val error: String? = null) : MatrixError()

    /**
     * The request or entity was too large.
     */
    @Serializable
    @SerialName("M_TOO_LARGE")
    public data class TooLarge(override val error: String? = null) : MatrixError()

    /**
     * Forbidden access, e.g. joining a room without permission, failed login.
     */
    @Serializable
    @SerialName("M_FORBIDDEN")
    public data class Forbidden(override val error: String? = null) : MatrixError()

    /**
     * Encountered when trying to register a user ID which is not valid.
     */
    @Serializable
    @SerialName("M_INVALID_USERNAME")
    public data class InvalidUsername(override val error: String? = null) : MatrixError()

    /**
     * Encountered when trying to register a user ID which has been taken.
     */
    @Serializable
    @SerialName("M_USER_IN_USE")
    public data class UserInUse(override val error: String? = null) : MatrixError()

    /**
     * Sent when a threepid given to an API cannot be used because no record matching the threepid was found.
     */
    @Serializable
    @SerialName("M_THREEPID_NOT_FOUND")
    public data class ThreePIdNotFound(override val error: String? = null) : MatrixError()

    /**
     * Sent when a threepid given to an API cannot be used because the same threepid is already in use.
     */
    @Serializable
    @SerialName("M_THREEPID_IN_USE")
    public data class ThreePIdInUse(override val error: String? = null) : MatrixError()

    /**
     * The server does not permit this third party identifier.
     * This may happen if the server only permits, for example, email addresses from a particular domain.
     */
    @Serializable
    @SerialName("M_THREEPID_DENIED")
    public data class ThreePIdDenied(override val error: String? = null) : MatrixError()

    /**
     * Authentication could not be performed on the third party identifier.
     */
    @Serializable
    @SerialName("M_THREEPID_AUTH_FAILED")
    public data class ThreePIdAuthFailed(override val error: String? = null) : MatrixError()

    /**
     * The resource being requested is reserved by an application service,
     * or the application service making the request has not created the resource.
     */
    @Serializable
    @SerialName("M_EXCLUSIVE")
    public data class Exclusive(override val error: String? = null) : MatrixError()

    /**
     * The client's request to create a room used a room version that the server does not support.
     */
    @Serializable
    @SerialName("M_UNSUPPORTED_ROOM_VERSION")
    public data class UnsupportedRoomVersion(override val error: String? = null) : MatrixError()

    /**
     * The access token specified was not recognised.
     *
     * An additional response parameter, soft_logout, might be present on the response for 401 HTTP status codes.
     * See the [soft logout](https://matrix.org/docs/spec/client_server/latest#soft-logout) section for more information.
     */
    @Serializable
    @SerialName("M_UNKNOWN_TOKEN")
    public data class UnknownToken(
        override val error: String? = null,

        @SerialName("soft_logout")
        val softLogout: Boolean = false
    ) : MatrixError()

    /**
     * No access token was specified for the request.
     */
    @Serializable
    @SerialName("M_MISSING_TOKEN")
    public data class MissingToken(override val error: String? = null) : MatrixError()

    /**
     * Request contained valid JSON, but it was malformed in some way,
     * e.g. missing required keys, invalid values for keys.
     */
    @Serializable
    @SerialName("M_BAD_JSON")
    public data class BadJson(override val error: String? = null) : MatrixError()

    /**
     * Request did not contain valid JSON.
     */
    @Serializable
    @SerialName("M_NOT_JSON")
    public data class NotJson(override val error: String? = null) : MatrixError()

    /**
     * A required parameter was missing from the request.
     */
    @Serializable
    @SerialName("M_MISSING_PARAM")
    public data class MissingParam(override val error: String? = null) : MatrixError()

    /**
     * Too many requests have been sent in a short period of time. Wait a while then try again.
     */
    @Serializable
    @SerialName("M_LIMIT_EXCEEDED")
    public data class LimitExceeded(
        override val error: String? = null,

        @SerialName("retry_after_ms")
        val retryAfterMillis: Long
    ) : MatrixError()

    /**
     * An unknown error has occurred.
     */
    @Serializable
    @SerialName("M_UNKNOWN")
    public data class Unknown(override val error: String? = null) : MatrixError()

    /**
     * The server did not understand the request.
     */
    @Serializable
    @SerialName("M_UNRECOGNIZED")
    public data class Unrecognized(override val error: String? = null) : MatrixError()

    /**
     * The request was not correctly authorized. Usually due to login failures.
     */
    @Serializable
    @SerialName("M_UNAUTHORIZED")
    public data class Unauthorized(override val error: String? = null) : MatrixError()

    /**
     * The user ID associated with the request has been deactivated.
     * Typically for endpoints that prove authentication, such as `/login`.
     */
    @Serializable
    @SerialName("M_USER_DEACTIVATED")
    public data class UserDeactivated(override val error: String? = null) : MatrixError()

    /**
     * Sent when the initial state given to the createRoom API is invalid.
     */
    @Serializable
    @SerialName("M_INVALID_ROOM_STATE")
    public data class InvalidRoomState(override val error: String? = null) : MatrixError()

    /**
     * The client's request used a third party server, eg. identity server, that this server does not trust.
     */
    @Serializable
    @SerialName("M_SERVER_NOT_TRUSTED")
    public data class ServerNotTrusted(override val error: String? = null) : MatrixError()

    /**
     * The client attempted to join a room that has a version the server does not support.
     * Inspect the `room_version` property of the error response for the room's version.
     */
    @Serializable
    @SerialName("M_INCOMPATIBLE_ROOM_VERSION")
    public data class IncompatibleRoomVersion(
        override val error: String? = null,

        @SerialName("room_version")
        val roomVersion: String? = null
    ) : MatrixError()

    /**
     * The state change requested cannot be performed, such as attempting to unban a user who is not banned.
     */
    @Serializable
    @SerialName("M_BAD_STATE")
    public data class BadState(override val error: String? = null) : MatrixError()

    /**
     * The room or resource does not permit guests to access it.
     */
    @Serializable
    @SerialName("M_GUEST_ACCESS_FORBIDDEN")
    public data class GuestAccessForbidden(override val error: String? = null) : MatrixError()

    /**
     * A Captcha is required to complete the request.
     */
    @Serializable
    @SerialName("M_CAPTCHA_NEEDED")
    public data class CaptchaNeeded(override val error: String? = null) : MatrixError()

    /**
     * The Captcha provided did not match what was expected.
     */
    @Serializable
    @SerialName("M_CAPTCHA_INVALID")
    public data class CaptchaInvalid(override val error: String? = null) : MatrixError()

    /**
     * A parameter that was specified has the wrong value.
     * For example, the server expected an integer and instead received a string.
     */
    @Serializable
    @SerialName("M_INVALID_PARAM")
    public data class InvalidParam(override val error: String? = null) : MatrixError()

    /**
     * The request cannot be completed because the homeserver has reached a resource limit imposed on it.
     * For example, a homeserver held in a shared hosting environment may reach a resource limit if it starts using too much memory or disk space.
     * The error MUST have an `admin_contact` field to provide the user receiving the error a place to reach out to.
     * Typically, this error will appear on routes which attempt to modify state (eg: sending messages, account data, etc)
     * and not routes which only read state (eg: `/sync`, get account data, etc).
     */
    @Serializable
    @SerialName("M_RESOURCE_LIMIT_EXCEEDED")
    public data class ResourceLimitExceeded(override val error: String? = null) : MatrixError()

    /**
     * The user is unable to reject an invite to join the server notices room.
     * See the [Server Notices](https://matrix.org/docs/spec/client_server/latest#server-notices) module for more information.
     */
    @Serializable
    @SerialName("M_CANNOT_LEAVE_SERVER_NOTICE_ROOM")
    public data class CannotLeaveServerNoticeRoom(override val error: String? = null) : MatrixError()
}
