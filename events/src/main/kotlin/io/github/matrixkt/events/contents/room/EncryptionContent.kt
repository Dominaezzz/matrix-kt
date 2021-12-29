package io.github.matrixkt.events.contents.room

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Defines how messages sent in this room should be encrypted.
 */
@SerialName("m.room.encryption")
@Serializable
public data class EncryptionContent(
    /**
     * The encryption algorithm to be used to encrypt messages sent in this room. Must be 'm.megolm.v1.aes-sha2'.
     */
    val algorithm: String,

    /**
     * How long the session should be used before changing it. 604800000 (a week) is the recommended default.
     */
    @SerialName("rotation_period_ms")
    val rotationPeriodMs: Long? = null,

    /**
     * How many messages should be sent before changing the session. 100 is the recommended default.
     */
    @SerialName("rotation_period_msgs")
    val rotationPeriodMsgs: Long? = null
)
