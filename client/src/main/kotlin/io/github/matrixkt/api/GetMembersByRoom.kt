package io.github.matrixkt.api

import io.github.matrixkt.models.events.StateEvent
import io.github.matrixkt.models.events.contents.room.MemberContent
import io.github.matrixkt.models.events.contents.room.Membership
import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

/**
 * Get the list of members for this room.
 */
public class GetMembersByRoom(
    public override val url: Url
) : MatrixRpc.WithAuth<RpcMethod.Get, GetMembersByRoom.Url, Nothing, GetMembersByRoom.Response> {
    public override val body: Nothing
        get() = TODO()

    @Resource("_matrix/client/r0/rooms/{roomId}/members")
    @Serializable
    public class Url(
        /**
         * The room to get the member events for.
         */
        public val roomId: String,
        /**
         * The point in time (pagination token) to return members for in the room.
         * This token can be obtained from a ``prev_batch`` token returned for
         * each room by the sync API. Defaults to the current state of the room,
         * as determined by the server.
         */
        public val at: String? = null,
        /**
         * The kind of membership to filter for. Defaults to no filtering if
         * unspecified. When specified alongside ``not_membership``, the two
         * parameters create an 'or' condition: either the membership *is*
         * the same as ``membership`` **or** *is not* the same as ``not_membership``.
         */
        public val membership: Membership? = null,
        /**
         * The kind of membership to exclude from the results. Defaults to no
         * filtering if unspecified.
         */
        @SerialName("not_membership")
        public val notMembership: Membership? = null
    )

    @Serializable
    public class Response(
        public val chunk: List<StateEvent<MemberContent, JsonObject>>? = null
    )
}