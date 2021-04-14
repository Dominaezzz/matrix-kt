package io.github.matrixkt.models.search

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public class Categories(
    /**
     * Mapping of category name to search criteria.
     */
    @SerialName("room_events")
    public val roomEvents: RoomEventsCriteria? = null
)
