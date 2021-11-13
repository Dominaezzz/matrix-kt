package io.github.matrixkt.models.keybackup

import kotlinx.serialization.Serializable

/**
 * The backed up keys for a room.
 */
@Serializable
public data class RoomKeyBackup(
    /**
     * A map of session IDs to key data.
     */
    public val sessions: Map<String, KeyBackupData>
)
