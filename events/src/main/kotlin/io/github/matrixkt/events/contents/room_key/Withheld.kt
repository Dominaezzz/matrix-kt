package io.github.matrixkt.events.contents.room_key

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This event type is used to indicate that the sender is not sharing room keys with the recipient.
 * It is sent as a to-device event.
 *
 * Possible values for ``code`` include:
 *
 *  * ``m.blacklisted``: the user/device was blacklisted.
 *  * ``m.unverified``: the user/device was not verified, and the sender is only
 * sharing keys with verified users/devices.
 *  * ``m.unauthorised``: the user/device is not allowed to have the key. For
 * example, this could be sent in response to a key request if the user/device
 * was not in the room when the original message was sent.
 *  * ``m.unavailable``: sent in reply to a key request if the device that the
 * key is requested from does not have the requested key.
 *  * ``m.no_olm``: an olm session could not be established.
 *
 * In most cases, this event refers to a specific room key. The one exception to
 * this is when the sender is unable to establish an olm session with the
 * recipient. When this happens, multiple sessions will be affected.
 *
 * In order to avoid filling the recipient's device mailbox, the sender should only send
 * one ``m.room_key.withheld`` message with no [roomId] nor [sessionId]
 * set.
 *
 * If the sender retries and fails to create an olm session again in the
 * future, it should not send another ``m.room_key.withheld`` message with a
 * [code] of ``m.no_olm``, unless another olm session was previously
 * established successfully.
 *
 * In response to receiving an ``m.room_key.withheld`` message with a
 * [code] of ``m.no_olm``, the
 * recipient may start an olm session with the sender and send an ``m.dummy``
 * message to notify the sender of the new olm session.
 *
 * The recipient may assume that this ``m.room_key.withheld`` message applies
 * to all encrypted room messages sent before it receives the message.
 */
@SerialName("m.room_key.withheld")
@Serializable
public data class Withheld(
    /**
     * The encryption algorithm for the key that this event is about.
     */
    public val algorithm: String,

    /**
     * Required if [code] is not ``m.no_olm``.
     * The room for the key that this event is about.
     */
    @SerialName("room_id")
    public val roomId: String?,

    /**
     * Required of [code] is not ``m.no_olm``.
     * The session ID of the key that this event is for.
     */
    @SerialName("session_id")
    public val sessionId: String?,

    /**
     * The unpadded base64-encoded device curve25519 key of the event's sender.
     */
    @SerialName("sender_key")
    public val senderKey: String,

    /**
     * A machine-readable code for why the key was not sent.
     * Codes beginning with `m.` are reserved for codes defined in the Matrix specification.
     * Custom codes must use the Java package naming convention.
     */
    public val code: String,

    /**
     * A human-readable reason for why the key was not sent.
     * The receiving client should only use this string if it does not understand the [code].
     */
    public val reason: String
)
