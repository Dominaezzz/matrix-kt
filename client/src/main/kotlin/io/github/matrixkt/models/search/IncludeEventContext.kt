package io.github.matrixkt.models.search

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class IncludeEventContext(
    /**
     * How many events before the result are returned.
     */
    @SerialName("before_limit")
    val beforeLimit: Int = 5,

    /**
     * How many events after the result are returned.
     */
    @SerialName("after_limit")
    val afterLimit: Int = 5,

    /**
     * Requests that the server returns the historic profile information for the users that sent the events that were returned.
     */
    @SerialName("include_profile")
    val includeProfile: Boolean = false
)
