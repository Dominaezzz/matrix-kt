package io.github.matrixkt.api

import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This API returns a list of the user's current rooms.
 */
public class GetJoinedRooms(
    public override val url: Url
) : MatrixRpc.WithAuth<RpcMethod.Get, GetJoinedRooms.Url, Nothing, GetJoinedRooms.Response> {
    public override val body: Nothing
        get() = TODO()

    @Resource("_matrix/client/r0/joined_rooms")
    @Serializable
    public class Url

    @Serializable
    public class Response(
        /**
         * The ID of each room in which the user has ``joined`` membership.
         */
        @SerialName("joined_rooms")
        public val joinedRooms: List<String>
    )
}
