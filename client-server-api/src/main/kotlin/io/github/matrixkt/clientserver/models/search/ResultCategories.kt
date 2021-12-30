package io.github.matrixkt.clientserver.models.search

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public class ResultCategories(
    /**
     * Mapping of category name to search criteria.
     */
    @SerialName("room_events")
    public val roomEvents: ResultRoomEvents? = null
)
