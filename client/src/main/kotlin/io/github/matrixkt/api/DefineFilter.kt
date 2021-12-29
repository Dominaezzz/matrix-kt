package io.github.matrixkt.api

import io.github.matrixkt.models.filter.Filter
import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Uploads a new filter definition to the homeserver.
 * Returns a filter ID that may be used in future requests to
 * restrict which events are returned to the client.
 */
public class DefineFilter(
    public override val url: Url,
    /**
     * The filter to upload.
     */
    public override val body: Filter
) : MatrixRpc.WithAuth<RpcMethod.Post, DefineFilter.Url, Filter, DefineFilter.Response> {
    @Resource("_matrix/client/r0/user/{userId}/filter")
    @Serializable
    public class Url(
        /**
         * The id of the user uploading the filter. The access token must be authorized to make
         * requests for this user id.
         */
        public val userId: String
    )

    @Serializable
    public class Response(
        /**
         * The ID of the filter that was created. Cannot start
         * with a ``{`` as this character is used to determine
         * if the filter provided is inline JSON or a previously
         * declared filter by homeservers on some APIs.
         */
        @SerialName("filter_id")
        public val filterId: String
    )
}
