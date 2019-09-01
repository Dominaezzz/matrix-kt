package io.github.matrixkt.models.events.contents

import kotlinx.serialization.Serializable

@Serializable
data class RoomAliasesContent(
    /**
     * A list of room aliases.
     */
    val aliases: List<String>
) : Content()
