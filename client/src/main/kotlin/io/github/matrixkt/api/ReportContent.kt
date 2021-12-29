package io.github.matrixkt.api

import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.Serializable

/**
 * Reports an event as inappropriate to the server, which may then notify
 * the appropriate people.
 */
public class ReportContent(
    public override val url: Url,
    public override val body: Body
) : MatrixRpc.WithAuth<RpcMethod.Post, ReportContent.Url, ReportContent.Body, Unit> {
    @Resource("_matrix/client/r0/rooms/{roomId}/report/{eventId}")
    @Serializable
    public class Url(
        /**
         * The room in which the event being reported is located.
         */
        public val roomId: String,
        /**
         * The event to report.
         */
        public val eventId: String
    )

    @Serializable
    public class Body(
        /**
         * The reason the content is being reported. May be blank.
         */
        public val reason: String? = null,
        /**
         * The score to rate this content as where -100 is most offensive
         * and 0 is inoffensive.
         */
        public val score: Long? = null
    )
}
