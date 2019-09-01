package io.github.matrixkt.models.events.contents

import kotlinx.serialization.Serializable

@Serializable
data class RoomRedactionContent(
    /**
     * The reason for the redaction, if any.
     */
    val reason: String? = null
) : Content()
