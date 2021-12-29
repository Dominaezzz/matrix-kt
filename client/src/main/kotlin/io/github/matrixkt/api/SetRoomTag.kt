package io.github.matrixkt.api

import io.github.matrixkt.models.events.contents.TagContent
import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.Serializable

/**
 * Add a tag to the room.
 */
public class SetRoomTag(
    public override val url: Url,
    /**
     * Extra data for the tag, e.g. ordering.
     */
    public override val body: TagContent.Tag
) : MatrixRpc.WithAuth<RpcMethod.Put, SetRoomTag.Url, TagContent.Tag, Unit> {
    @Resource("_matrix/client/r0/user/{userId}/rooms/{roomId}/tags/{tag}")
    @Serializable
    public class Url(
        /**
         * The id of the user to add a tag for. The access token must be
         * authorized to make requests for this user ID.
         */
        public val userId: String,
        /**
         * The ID of the room to add a tag to.
         */
        public val roomId: String,
        /**
         * The tag to add.
         */
        public val tag: String
    )
}
