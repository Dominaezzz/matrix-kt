package io.github.matrixkt.models.search

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class ResultCategories(
    /**
     * Mapping of category name to search criteria.
     */
    @SerialName("room_events")
    val roomEvents: ResultRoomEvents? = null
)
