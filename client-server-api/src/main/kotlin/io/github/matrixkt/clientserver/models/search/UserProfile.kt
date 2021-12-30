package io.github.matrixkt.clientserver.models.search

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public class UserProfile(
    @SerialName("displayname")
    public val displayName: String? = null,

    @SerialName("avatar_url")
    public val avatarUrl: String? = null
)
