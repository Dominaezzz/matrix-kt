package io.github.matrixkt.clientserver.api

import io.github.matrixkt.clientserver.models.PublicRoomsChunk
import io.github.matrixkt.clientserver.MatrixRpc
import io.github.matrixkt.clientserver.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Lists the public rooms on the server.
 *
 * This API returns paginated responses. The rooms are ordered by the number
 * of joined members, with the largest rooms first.
 */
public class GetPublicRooms(
    public override val url: Url
) : MatrixRpc<RpcMethod.Get, GetPublicRooms.Url, Nothing, GetPublicRooms.Response> {
    public override val body: Nothing
        get() = TODO()

    @Resource("_matrix/client/r0/publicRooms")
    @Serializable
    public class Url(
        /**
         * Limit the number of results returned.
         */
        public val limit: Long? = null,
        /**
         * A pagination token from a previous request, allowing clients to
         * get the next (or previous) batch of rooms.
         * The direction of pagination is specified solely by which token
         * is supplied, rather than via an explicit flag.
         */
        public val since: String? = null,
        /**
         * The server to fetch the public room lists from. Defaults to the
         * local server.
         */
        public val server: String? = null
    )

    @Serializable
    public class Response(
        /**
         * A paginated chunk of public rooms.
         */
        public val chunk: List<PublicRoomsChunk>,
        /**
         * A pagination token for the response. The absence of this token
         * means there are no more results to fetch and the client should
         * stop paginating.
         */
        @SerialName("next_batch")
        public val nextBatch: String? = null,
        /**
         * A pagination token that allows fetching previous results. The
         * absence of this token means there are no results before this
         * batch, i.e. this is the first batch.
         */
        @SerialName("prev_batch")
        public val prevBatch: String? = null,
        /**
         * An estimate on the total number of public rooms, if the
         * server has an estimate.
         */
        @SerialName("total_room_count_estimate")
        public val totalRoomCountEstimate: Long? = null
    )
}
