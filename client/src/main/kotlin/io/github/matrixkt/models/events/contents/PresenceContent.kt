package io.github.matrixkt.models.events.contents

import io.github.matrixkt.models.Presence
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PresenceContent(
    /**
     * The current avatar URL for this user, if any.
     */
    @SerialName("avatar_url")
    val avatarUrl: String? = null,

    /**
     * The current display name for this user, if any.
     */
    @SerialName("displayname")
    val displayName: String? = null,

    /**
     * The last time since this used performed some action, in milliseconds.
     */
    @SerialName("last_active_ago")
    val lastActiveAgo: Long? = null,

    /**
     * The presence state for this user. One of: ["online", "offline", "unavailable"]
     */
    val presence: Presence,

    /**
     * Whether the user is currently active.
     */
    @SerialName("currently_active")
    val currentlyActive: Boolean? = null,

    /**
     * An optional description to accompany the presence.
     */
    @SerialName("status_msg")
    val statusMessage: String? = null
): Content()
