package io.github.matrixkt.events.contents

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This event type is used to forward keys for end-to-end encryption.
 * Typically it is encrypted as an `m.room.encrypted` event, then sent as a [to-device](https://matrix.org/docs/spec/client_server/r0.6.0#to-device) event.
 */
@SerialName("m.forwarded_room_key")
@Serializable
public data class ForwardedRoomKeyContent(
    /**
     * The encryption algorithm the key in this event is to be used with.
     */
    val algorithm: String,

    /**
     * The room where the key is used.
     */
    @SerialName("room_id")
    val roomId: String,

    /**
     * The Curve25519 key of the device which initiated the session originally.
     */
    @SerialName("sender_key")
    val senderKey: String,

    /**
     * The ID of the session that the key is for.
     */
    @SerialName("session_id")
    val sessionId: String,

    /**
     * The key to be exchanged.
     */
    @SerialName("session_key")
    val sessionKey: String,

    /**
     * The Ed25519 key of the device which initiated the session originally.
     * It is 'claimed' because the receiving device has no way to tell that the original `room_key` actually
     * came from a device which owns the private part of this key unless they have done device verification.
     */
    @SerialName("sender_claimed_ed25519_key")
    val senderClaimedEd25519Key: String,

    /**
     * Chain of Curve25519 keys.
     * It starts out empty, but each time the key is forwarded to another device, the previous sender in the chain is added to the end of the list.
     * For example, if the key is forwarded from A to B to C, this field is empty between A and B, and contains A's Curve25519 key between B and C.
     */
    @SerialName("forwarding_curve25519_key_chain")
    val forwardingCurve25519KeyChain: List<String>
)
