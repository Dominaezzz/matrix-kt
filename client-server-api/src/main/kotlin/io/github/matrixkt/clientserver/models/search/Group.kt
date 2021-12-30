package io.github.matrixkt.clientserver.models.search

import kotlinx.serialization.Serializable

@Serializable
public class Group(
    /**
     * Key that defines the group.
     * One of: ["room_id", "sender"]
     */
    public val key: GroupKey? = null
)
