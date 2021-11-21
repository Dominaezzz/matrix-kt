package io.github.matrixkt.models.search

import io.github.matrixkt.models.events.StateEvent
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
public class ResultRoomEvents(
    /**
     * An approximate count of the total number of results found.
     */
    public val count: Int? = null,

    /**
     * List of words which should be highlighted, useful for stemming which may change the query terms.
     */
    public val highlights: List<String> = emptyList(),

    /**
     * 	List of results in the requested order.
     */
    public val results: List<Result> = emptyList(),

    /**
     * The current state for every room in the results.
     * This is included if the request had the [RoomEventsCriteria.includeState] key set with a value of true.
     *
     * The string key is the room ID for which the State Event array belongs to.
     */
    public val state: Map<String, StateEvent<JsonObject, JsonObject>> = emptyMap(),

    /**
     * Any groups that were requested.
     *
     * The outer string key is the group key requested (eg: room_id or sender).
     * The inner string key is the grouped value (eg: a room's ID or a user's ID).
     */
    public val groups: Map<String, Map<String, GroupValue>>,

    /**
     * Token that can be used to get the next batch of results, by passing as the `next_batch` parameter to the next call.
     * If this field is absent, there are no more results.
     */
    @SerialName("next_batch")
    public val nextBatch: String? = null
)
