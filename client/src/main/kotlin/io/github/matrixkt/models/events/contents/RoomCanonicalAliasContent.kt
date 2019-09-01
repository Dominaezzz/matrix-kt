package io.github.matrixkt.models.events.contents

import kotlinx.serialization.Serializable

@Serializable
data class RoomCanonicalAliasContent(
    /**
     * The canonical alias.
     */
    val alias: String
) : Content()
