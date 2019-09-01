package io.github.matrixkt.models.events.contents

import kotlinx.serialization.Serializable

@Serializable
data class RoomNameContent(
    /**
     * The name of the room. This MUST NOT exceed 255 bytes.
     */
    val name: String
) : Content()
