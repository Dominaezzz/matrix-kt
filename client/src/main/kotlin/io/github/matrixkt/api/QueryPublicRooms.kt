package io.github.matrixkt.api

import io.github.matrixkt.models.PublicRoomsChunk
import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Lists the public rooms on the server, with optional filter.
 *
 * This API returns paginated responses. The rooms are ordered by the number
 * of joined members, with the largest rooms first.
 */
public class QueryPublicRooms(
    public override val url: Url,
    /**
     * Options for which rooms to return.
     */
    public override val body: Body
) : MatrixRpc.WithAuth<RpcMethod.Post, QueryPublicRooms.Url, QueryPublicRooms.Body, QueryPublicRooms.Response> {
    @Resource("_matrix/client/r0/publicRooms")
    @Serializable
    public class Url(
        /**
         * The server to fetch the public room lists from. Defaults to the
         * local server.
         */
        public val server: String? = null
    )

    @Serializable
    public class Filter(
        /**
         * A string to search for in the room metadata, e.g. name,
         * topic, canonical alias etc. (Optional).
         */
        @SerialName("generic_search_term")
        public val genericSearchTerm: String? = null
    )

    @Serializable
    public class Body(
        /**
         * Filter to apply to the results.
         */
        public val filter: Filter? = null,
        /**
         * Whether or not to include all known networks/protocols from
         * application services on the homeserver. Defaults to false.
         */
        @SerialName("include_all_networks")
        public val includeAllNetworks: Boolean? = null,
        /**
         * Limit the number of results returned.
         */
        public val limit: Long? = null,
        /**
         * A pagination token from a previous request, allowing clients
         * to get the next (or previous) batch of rooms.  The direction
         * of pagination is specified solely by which token is supplied,
         * rather than via an explicit flag.
         */
        public val since: String? = null,
        /**
         * The specific third party network/protocol to request from the
         * homeserver. Can only be used if ``include_all_networks`` is false.
         */
        @SerialName("third_party_instance_id")
        public val thirdPartyInstanceId: String? = null
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
