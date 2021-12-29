package io.github.matrixkt.api

import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Performs a search for users. The homeserver may
 * determine which subset of users are searched, however the homeserver
 * MUST at a minimum consider the users the requesting user shares a
 * room with and those who reside in public rooms (known to the homeserver).
 * The search MUST consider local users to the homeserver, and SHOULD
 * query remote users as part of the search.
 *
 * The search is performed case-insensitively on user IDs and display
 * names preferably using a collation determined based upon the
 * ``Accept-Language`` header provided in the request, if present.
 */
public class SearchUserDirectory(
    public override val url: Url,
    public override val body: Body
) : MatrixRpc.WithAuth<RpcMethod.Post, SearchUserDirectory.Url, SearchUserDirectory.Body,
        SearchUserDirectory.Response> {
    @Resource("_matrix/client/r0/user_directory/search")
    @Serializable
    public class Url

    @Serializable
    public class Body(
        /**
         * The maximum number of results to return. Defaults to 10.
         */
        public val limit: Long? = null,
        /**
         * The term to search for
         */
        @SerialName("search_term")
        public val searchTerm: String
    )

    @Serializable
    public class User(
        /**
         * The avatar url, as an MXC, if one exists.
         */
        @SerialName("avatar_url")
        public val avatarUrl: String? = null,
        /**
         * The display name of the user, if one exists.
         */
        @SerialName("display_name")
        public val displayName: String? = null,
        /**
         * The user's matrix user ID.
         */
        @SerialName("user_id")
        public val userId: String
    )

    @Serializable
    public class Response(
        /**
         * Indicates if the result list has been truncated by the limit.
         */
        public val limited: Boolean,
        /**
         * Ordered by rank and then whether or not profile info is available.
         */
        public val results: List<User>
    )
}
