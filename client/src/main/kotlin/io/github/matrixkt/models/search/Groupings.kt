package io.github.matrixkt.models.search

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class Groupings(
    /**
     * List of groups to request.
     */
    @SerialName("group_by")
    val groupBy: List<Group> = emptyList()
)
