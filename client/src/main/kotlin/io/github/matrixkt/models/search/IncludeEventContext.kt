package io.github.matrixkt.models.search

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class IncludeEventContext(
    /**
     * How many events before the result are returned.
     * By default, this is 5.
     */
    @SerialName("before_limit")
    val beforeLimit: Int? = null,

    /**
     * How many events after the result are returned.
     * By default, this is 5.
     */
    @SerialName("after_limit")
    val afterLimit: Int? = null,

    /**
     * Requests that the server returns the historic profile information for the users that sent the events that were returned.
     * By default, this is false.
     */
    @SerialName("include_profile")
    val includeProfile: Boolean? = null
)
