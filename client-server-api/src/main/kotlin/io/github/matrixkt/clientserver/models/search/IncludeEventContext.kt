package io.github.matrixkt.clientserver.models.search

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public class IncludeEventContext(
    /**
     * How many events before the result are returned.
     */
    @SerialName("before_limit")
    public val beforeLimit: Int = 5,

    /**
     * How many events after the result are returned.
     */
    @SerialName("after_limit")
    public val afterLimit: Int = 5,

    /**
     * Requests that the server returns the historic profile information for the users that sent the events that were returned.
     */
    @SerialName("include_profile")
    public val includeProfile: Boolean = false
)
