package io.github.matrixkt.api

import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This API returns a map of MXIDs to member info objects for members of the room. The current user
 * must be in the room for it to work, unless it is an Application Service in which case any of the
 * AS's users must be in the room. This API is primarily for Application Services and should be faster
 * to respond than ``/members`` as it can be implemented more efficiently on the server.
 */
public class GetJoinedMembersByRoom(
    public override val url: Url
) : MatrixRpc.WithAuth<RpcMethod.Get, GetJoinedMembersByRoom.Url, Nothing, GetJoinedMembersByRoom.Response> {
    public override val body: Nothing
        get() = TODO()

    @Resource("_matrix/client/r0/rooms/{roomId}/joined_members")
    @Serializable
    public class Url(
        /**
         * The room to get the members of.
         */
        public val roomId: String
    )

    @Serializable
    public class RoomMember(
        /**
         * The mxc avatar url of the user this object is representing.
         */
        @SerialName("avatar_url")
        public val avatarUrl: String? = null,
        /**
         * The display name of the user this object is representing.
         */
        @SerialName("display_name")
        public val displayName: String? = null
    )

    @Serializable
    public class Response(
        /**
         * A map from user ID to a RoomMember object.
         */
        public val joined: Map<String, RoomMember> = emptyMap()
    )
}
