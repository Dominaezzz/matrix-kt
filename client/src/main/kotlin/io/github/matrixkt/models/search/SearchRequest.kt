package io.github.matrixkt.models.search

import kotlinx.serialization.Serializable

@Serializable
class SearchRequest(
    /**
     * Describes which categories to search in and their criteria.
     */
    @Serializable
    val searchCategories: Categories
)
