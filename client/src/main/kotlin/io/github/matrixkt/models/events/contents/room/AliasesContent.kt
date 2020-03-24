package io.github.matrixkt.models.events.contents.room

import io.github.matrixkt.models.events.contents.Content
import kotlinx.serialization.Serializable

@Serializable
data class AliasesContent(
    /**
     * A list of room aliases.
     */
    val aliases: List<String>
) : Content()
