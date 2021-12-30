package io.github.matrixkt.clientserver.models.search

import io.github.matrixkt.clientserver.models.events.MatrixEvent
import kotlinx.serialization.Serializable

@Serializable
public class Result(
    /**
     * A number that describes how closely this result matches the search.
     * Higher is closer.
     */
    public val rank: Double,

    /**
     * The event that matched.
     */
    public val result: MatrixEvent? = null,

    /**
     * Context for result, if requested.
     */
    public val context: EventContext? = null
)
