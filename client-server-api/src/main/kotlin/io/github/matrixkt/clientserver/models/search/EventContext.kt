package io.github.matrixkt.clientserver.models.search

import io.github.matrixkt.clientserver.models.events.MatrixEvent
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public class EventContext(
    /**
     * Pagination token for the start of the chunk.
     */
    public val start: String? = null,

    /**
     * Pagination token for the end of the chunk
     */
    public val end: String? = null,

    /**
     * The historic profile information of the users that sent the events returned.
     *
     * The string key is the user ID for which the profile belongs to.
     */
    @SerialName("profile_info")
    public val profileInfo: Map<String, UserProfile> = emptyMap(),

    /**
     * Events just before the result.
     */
    @SerialName("events_before")
    public val eventBefore: List<MatrixEvent> = emptyList(),

    /**
     * Events just after the result.
     */
    @SerialName("events_after")
    public val eventAfter: List<MatrixEvent> = emptyList()
)
