package io.github.matrixkt.models.search

import kotlinx.serialization.Serializable

@Serializable
class Group(
    /**
     * Key that defines the group.
     * One of: ["room_id", "sender"]
     */
    val key: GroupKey? = null
)
