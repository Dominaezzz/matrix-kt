package io.github.matrixkt.clientserver.models.search

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public class Groupings(
    /**
     * List of groups to request.
     */
    @SerialName("group_by")
    public val groupBy: List<Group> = emptyList()
)
