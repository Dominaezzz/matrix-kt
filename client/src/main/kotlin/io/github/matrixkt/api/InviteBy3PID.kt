package io.github.matrixkt.api

import io.github.matrixkt.models.Invite3pid
import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.Serializable

/**
 * .. _invite-by-third-party-id-endpoint:
 *
 * *Note that there are two forms of this API, which are documented separately.
 * This version of the API does not require that the inviter know the Matrix
 * identifier of the invitee, and instead relies on third party identifiers.
 * The homeserver uses an identity server to perform the mapping from
 * third party identifier to a Matrix identifier. The other is documented in the*
 * `joining rooms section`_.
 *
 * This API invites a user to participate in a particular room.
 * They do not start participating in the room until they actually join the
 * room.
 *
 * Only users currently in a particular room can invite other users to
 * join that room.
 *
 * If the identity server did know the Matrix user identifier for the
 * third party identifier, the homeserver will append a ``m.room.member``
 * event to the room.
 *
 * If the identity server does not know a Matrix user identifier for the
 * passed third party identifier, the homeserver will issue an invitation
 * which can be accepted upon providing proof of ownership of the third
 * party identifier. This is achieved by the identity server generating a
 * token, which it gives to the inviting homeserver. The homeserver will
 * add an ``m.room.third_party_invite`` event into the graph for the room,
 * containing that token.
 *
 * When the invitee binds the invited third party identifier to a Matrix
 * user ID, the identity server will give the user a list of pending
 * invitations, each containing:
 *
 * - The room ID to which they were invited
 *
 * - The token given to the homeserver
 *
 * - A signature of the token, signed with the identity server's private key
 *
 * - The matrix user ID who invited them to the room
 *
 * If a token is requested from the identity server, the homeserver will
 * append a ``m.room.third_party_invite`` event to the room.
 *
 * .. _joining rooms section: `invite-by-user-id-endpoint`_
 */
public class InviteBy3PID(
    public override val url: Url,
    public override val body: Invite3pid
) : MatrixRpc.WithAuth<RpcMethod.Post, InviteBy3PID.Url, Invite3pid, Unit> {
    @Resource("/_matrix/client/r0/rooms/{roomId}/invite")
    @Serializable
    public class Url(
        /**
         * The room identifier (not alias) to which to invite the user.
         */
        public val roomId: String
    )
}
