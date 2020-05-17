package io.github.matrixkt.models.search

import io.github.matrixkt.models.events.MatrixEvent
import kotlinx.serialization.Serializable

@Serializable
class Result(
    /**
     * A number that describes how closely this result matches the search.
     * Higher is closer.
     */
    val rank: Double,

    /**
     * The event that matched.
     */
    val result: MatrixEvent? = null,

    /**
     * Context for result, if requested.
     */
    val context: EventContext? = null
)
