package io.github.matrixkt.clientserver.models.search

import kotlinx.serialization.Serializable

@Serializable
public class SearchRequest(
    /**
     * Describes which categories to search in and their criteria.
     */
    @Serializable
    public val searchCategories: Categories
)
