package io.github.matrixkt.api

import io.github.matrixkt.models.ThirdPartySigned
import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.github.matrixkt.utils.resource.Resource
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * *Note that this API requires a room ID, not alias.* ``/join/{roomIdOrAlias}`` *exists if you have
 * a room alias.*
 *
 * This API starts a user participating in a particular room, if that user
 * is allowed to participate in that room. After this call, the client is
 * allowed to see all current state events in the room, and all subsequent
 * events associated with the room until the user leaves the room.
 *
 * After a user has joined a room, the room will appear as an entry in the
 * response of the |/initialSync|_ and |/sync|_ APIs.
 *
 * If a ``third_party_signed`` was supplied, the homeserver must verify
 * that it matches a pending ``m.room.third_party_invite`` event in the
 * room, and perform key validity checking if required by the event.
 */
public class JoinRoomById(
    public override val url: Url,
    public override val body: Body? = null
) : MatrixRpc.WithAuth<RpcMethod.Post, JoinRoomById.Url, JoinRoomById.Body?, JoinRoomById.Response> {
    @Resource("/_matrix/client/r0/rooms/{roomId}/join")
    @Serializable
    public class Url(
        /**
         * The room identifier (not alias) to join.
         */
        public val roomId: String
    )

    @Serializable
    public class Body(
        /**
         * A signature of an ``m.third_party_invite`` token to prove that this user
         * owns a third party identity which has been invited to the room.
         */
        @SerialName("third_party_signed")
        public val thirdPartySigned: ThirdPartySigned? = null
    )

    @Serializable
    public class Response(
        /**
         * The joined room ID.
         */
        @SerialName("room_id")
        public val roomId: String
    )
}
