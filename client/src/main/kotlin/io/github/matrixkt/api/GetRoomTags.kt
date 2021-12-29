package io.github.matrixkt.api

import io.github.matrixkt.models.events.contents.TagContent
import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.Serializable

/**
 * List the tags set by a user on a room.
 */
public class GetRoomTags(
    public override val url: Url
) : MatrixRpc.WithAuth<RpcMethod.Get, GetRoomTags.Url, Nothing, TagContent> {
    public override val body: Nothing
        get() = TODO()

    @Resource("_matrix/client/r0/user/{userId}/rooms/{roomId}/tags")
    @Serializable
    public class Url(
        /**
         * The id of the user to get tags for. The access token must be
         * authorized to make requests for this user ID.
         */
        public val userId: String,
        /**
         * The ID of the room to get tags for.
         */
        public val roomId: String
    )
}
