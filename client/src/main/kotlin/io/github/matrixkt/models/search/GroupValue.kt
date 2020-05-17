package io.github.matrixkt.models.search

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class GroupValue(
    /**
     * Token that can be used to get the next batch of results in the group, by passing as the next_batch parameter to the next call.
     * If this field is absent, there are no more results in this group.
     */
    @SerialName("next_batch")
    val nextBatch: String? = null,

    /**
     * Key that can be used to order different groups.
     */
    val order: Int? = null,

    /**
     * Which results are in this group.
     */
    val results: List<String> = emptyList()
)
