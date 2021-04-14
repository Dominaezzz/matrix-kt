package io.github.matrixkt.models.search

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public class Results(
    /**
     * Describes which categories to search in and their criteria.
     */
    @SerialName("search_categories")
    public val searchCategories: ResultCategories
)
