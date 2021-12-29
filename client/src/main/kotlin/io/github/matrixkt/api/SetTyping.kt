package io.github.matrixkt.api

import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.Serializable

/**
 * This tells the server that the user is typing for the next N
 * milliseconds where N is the value specified in the ``timeout`` key.
 * Alternatively, if ``typing`` is ``false``, it tells the server that the
 * user has stopped typing.
 */
public class SetTyping(
    public override val url: Url,
    /**
     * The current typing state.
     */
    public override val body: Body
) : MatrixRpc.WithAuth<RpcMethod.Put, SetTyping.Url, SetTyping.Body, Unit> {
    @Resource("_matrix/client/r0/rooms/{roomId}/typing/{userId}")
    @Serializable
    public class Url(
        /**
         * The user who has started to type.
         */
        public val userId: String,
        /**
         * The room in which the user is typing.
         */
        public val roomId: String
    )

    @Serializable
    public class Body(
        /**
         * The length of time in milliseconds to mark this user as typing.
         */
        public val timeout: Long? = null,
        /**
         * Whether the user is typing or not. If ``false``, the ``timeout``
         * key can be omitted.
         */
        public val typing: Boolean
    )
}
