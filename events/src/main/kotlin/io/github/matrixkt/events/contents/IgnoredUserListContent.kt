package io.github.matrixkt.events.contents

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A map of users which are considered ignored is kept in ``account_data`` in an event type of ``m.ignored_user_list``.
 */
@SerialName("m.ignored_user_list")
@Serializable
public data class IgnoredUserListContent(
    /**
     * The map of users to ignore.
     */
    @SerialName("ignored_users")
    val ignoredUsers: Map<String, Unit>
)
