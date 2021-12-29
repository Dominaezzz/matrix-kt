package io.github.matrixkt.api

import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * *Note that there are two forms of this API, which are documented separately.
 * This version of the API requires that the inviter knows the Matrix
 * identifier of the invitee. The other is documented in the*
 * [third party invites section](/client-server-api/#post_matrixclientv3roomsroomidinvite-1).
 *
 * This API invites a user to participate in a particular room.
 * They do not start participating in the room until they actually join the
 * room.
 *
 * Only users currently in a particular room can invite other users to
 * join that room.
 *
 * If the user was invited to the room, the homeserver will append a
 * `m.room.member` event to the room.
 */
public class InviteUser(
    public override val url: Url,
    public override val body: Body
) : MatrixRpc.WithAuth<RpcMethod.Post, InviteUser.Url, InviteUser.Body, Unit> {
    @Resource("_matrix/client/r0/rooms/{roomId}/invite ")
    @Serializable
    public class Url(
        /**
         * The room identifier (not alias) to which to invite the user.
         */
        public val roomId: String
    )

    @Serializable
    public class Body(
        /**
         * Optional reason to be included as the `reason` on the subsequent
         * membership event.
         */
        public val reason: String? = null,
        /**
         * The fully qualified user ID of the invitee.
         */
        @SerialName("user_id")
        public val userId: String
    )
}
