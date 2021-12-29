package io.github.matrixkt.api

import io.github.matrixkt.models.thirdparty.User
import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Retrieve a Matrix User ID linked to a user on the third party service, given
 * a set of user parameters.
 */
public class QueryUserByProtocol(
    public override val url: Url
) : MatrixRpc.WithAuth<RpcMethod.Get, QueryUserByProtocol.Url, Nothing, List<User>> {
    public override val body: Nothing
        get() = TODO()

    @Resource("_matrix/client/r0/thirdparty/user/{protocol}")
    @Serializable
    public class Url(
        /**
         * The name of the protocol.
         */
        public val protocol: String,
        /**
         * One or more custom fields that are passed to the AS to help identify the user.
         */
        @SerialName("fields...")
        public val fields: String? = null
    )
}
