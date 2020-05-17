package io.github.matrixkt.models.search

import io.github.matrixkt.models.events.MatrixEvent
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class EventContext(
    /**
     * Pagination token for the start of the chunk.
     */
    val start: String? = null,

    /**
     * Pagination token for the end of the chunk
     */
    val end: String? = null,

    /**
     * The historic profile information of the users that sent the events returned.
     *
     * The string key is the user ID for which the profile belongs to.
     */
    @SerialName("profile_info")
    val profileInfo: Map<String, UserProfile> = emptyMap(),

    /**
     * Events just before the result.
     */
    @SerialName("events_before")
    val eventBefore: List<MatrixEvent> = emptyList(),

    /**
     * Events just after the result.
     */
    @SerialName("events_after")
    val eventAfter: List<MatrixEvent> = emptyList()
)
