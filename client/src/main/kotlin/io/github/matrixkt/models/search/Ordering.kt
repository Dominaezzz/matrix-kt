package io.github.matrixkt.models.search

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class Ordering {
    @SerialName("recent")
    RECENT,

    @SerialName("rank")
    RANK;
}
