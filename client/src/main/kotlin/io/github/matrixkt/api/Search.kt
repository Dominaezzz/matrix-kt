package io.github.matrixkt.api

import io.github.matrixkt.models.search.Categories
import io.github.matrixkt.models.search.Results
import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Performs a full text search across different categories.
 */
public class Search(
    public override val url: Url,
    public override val body: Body
) : MatrixRpc.WithAuth<RpcMethod.Post, Search.Url, Search.Body, Results> {
    @Resource("_matrix/client/r0/search")
    @Serializable
    public class Url(
        /**
         * The point to return events from. If given, this should be a
         * ``next_batch`` result from a previous call to this endpoint.
         */
        @SerialName("next_batch")
        public val nextBatch: String? = null
    )

    @Serializable
    public class Body(
        /**
         * Describes which categories to search in and their criteria.
         */
        @SerialName("search_categories")
        public val searchCategories: Categories
    )
}
