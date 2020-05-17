package io.github.matrixkt.models.search

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class UserProfile(
    @SerialName("displayname")
    val displayName: String? = null,

    @SerialName("avatar_url")
    val avatarUrl: String? = null
)
