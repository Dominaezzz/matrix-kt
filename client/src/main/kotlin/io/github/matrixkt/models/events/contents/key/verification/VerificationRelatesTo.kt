package io.github.matrixkt.models.events.contents.key.verification

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class VerificationRelatesTo(
    /**
     * The event ID of the `m.key.verification.request` that this message is related to.
     */
    @SerialName("event_id")
    val eventId: String,

    /**
     * The relationship type.
     * One of: ["m.reference"].
     */
    @SerialName("rel_type")
    val relType: String
)
