package io.github.matrixkt.models.search

import io.github.matrixkt.models.events.MatrixEvent
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class ResultRoomEvents(
    /**
     * An approximate count of the total number of results found.
     */
    val count: Int? = null,

    /**
     * List of words which should be highlighted, useful for stemming which may change the query terms.
     */
    val highlights: List<String> = emptyList(),

    /**
     * 	List of results in the requested order.
     */
    val results: List<Result> = emptyList(),

    /**
     * The current state for every room in the results.
     * This is included if the request had the [RoomEventsCriteria.includeState] key set with a value of true.
     *
     * The string key is the room ID for which the State Event array belongs to.
     */
    val state: Map<String, MatrixEvent> = emptyMap(),

    /**
     * Any groups that were requested.
     *
     * The outer string key is the group key requested (eg: room_id or sender).
     * The inner string key is the grouped value (eg: a room's ID or a user's ID).
     */
    val groups: Map<String, Map<String, GroupValue>>,

    /**
     * Token that can be used to get the next batch of results, by passing as the `next_batch` parameter to the next call.
     * If this field is absent, there are no more results.
     */
    @SerialName("next_batch")
    val nextBatch: String? = null
)
