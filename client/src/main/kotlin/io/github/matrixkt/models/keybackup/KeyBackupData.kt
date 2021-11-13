package io.github.matrixkt.models.keybackup

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

/**
 * The key data.
 */
@Serializable
public data class KeyBackupData(
    /**
     * The index of the first message in the session that the key can decrypt.
     */
    @SerialName("first_message_index")
    val firstMessageIndex: Int,

    /**
     * The number of times this key has been forwarded via key-sharing between devices.
     */
    @SerialName("forwarded_count")
    val forwardedCount: Int,

    /**
     * Whether the device backing up the key verified the device that the key is from.
     */
    @SerialName("is_verified")
    val isVerified: Boolean,

    /**
     * Algorithm-dependent data.
     * See the documentation for the backup algorithms in [Server-side key backups](/client-server-api/#server-side-key-backups)
     * for more information on the expected format of the data.
     */
    @SerialName("session_data")
    val sessionData: JsonObject
)
