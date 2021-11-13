package io.github.matrixkt.models.keybackup

import kotlinx.serialization.Serializable

@Serializable
public data class RoomKeysUpdateResponse(
    /**
     * The number of keys stored in the backup
     */
    public val count: Long,
    /**
     * The new etag value representing stored keys in the backup.
     * See `GET /room_keys/version/{version}` for more details.
     */
    public val etag: String
)
