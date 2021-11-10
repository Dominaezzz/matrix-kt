package io.github.matrixkt.models.events.contents.secret_storage

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SerialName("m.secret_storage.default_key")
@Serializable
public data class DefaultKeyContent(
    /**
     * The ID of the default key.
     */
    val key: String
)
