package io.github.matrixkt.api

import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.Serializable

/**
 * This API sets the given user's display name. You must have permission to
 * set this user's display name, e.g. you need to have their ``access_token``.
 */
public class SetDisplayName(
    public override val url: Url,
    /**
     * The display name info.
     */
    public override val body: Body
) : MatrixRpc.WithAuth<RpcMethod.Put, SetDisplayName.Url, SetDisplayName.Body, Unit> {
    @Resource("_matrix/client/r0/profile/{userId}/displayname")
    @Serializable
    public class Url(
        /**
         * The user whose display name to set.
         */
        public val userId: String
    )

    @Serializable
    public class Body(
        /**
         * The new display name for this user.
         */
        public val displayname: String? = null
    )
}
