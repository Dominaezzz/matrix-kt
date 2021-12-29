package io.github.matrixkt.api

import io.github.matrixkt.models.thirdparty.User
import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.Serializable

/**
 * Retrieve an array of third party users from a Matrix User ID.
 */
public class QueryUserByID(
    public override val url: Url
) : MatrixRpc.WithAuth<RpcMethod.Get, QueryUserByID.Url, Nothing, List<User>> {
    public override val body: Nothing
        get() = TODO()

    @Resource("_matrix/client/r0/thirdparty/user")
    @Serializable
    public class Url(
        /**
         * The Matrix User ID to look up.
         */
        public val userid: String
    )
}
