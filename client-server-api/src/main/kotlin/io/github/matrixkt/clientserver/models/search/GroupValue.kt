package io.github.matrixkt.clientserver.models.search

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public class GroupValue(
    /**
     * Token that can be used to get the next batch of results in the group, by passing as the next_batch parameter to the next call.
     * If this field is absent, there are no more results in this group.
     */
    @SerialName("next_batch")
    public val nextBatch: String? = null,

    /**
     * Key that can be used to order different groups.
     */
    public val order: Int? = null,

    /**
     * Which results are in this group.
     */
    public val results: List<String> = emptyList()
)
