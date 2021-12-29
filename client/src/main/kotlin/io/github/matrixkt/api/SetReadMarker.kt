package io.github.matrixkt.api

import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Sets the position of the read marker for a given room, and optionally
 * the read receipt's location.
 */
public class SetReadMarker(
    public override val url: Url,
    /**
     * The read marker and optional read receipt locations.
     */
    public override val body: Body
) : MatrixRpc.WithAuth<RpcMethod.Post, SetReadMarker.Url, SetReadMarker.Body, Unit> {
    @Resource("_matrix/client/r0/rooms/{roomId}/read_markers")
    @Serializable
    public class Url(
        /**
         * The room ID to set the read marker in for the user.
         */
        public val roomId: String
    )

    @Serializable
    public class Body(
        /**
         * The event ID the read marker should be located at. The
         * event MUST belong to the room.
         */
        @SerialName("m.fully_read")
        public val fullyRead: String,
        /**
         * The event ID to set the read receipt location at. This is
         * equivalent to calling ``/receipt/m.read/$elsewhere:example.org``
         * and is provided here to save that extra call.
         */
        @SerialName("m.read")
        public val read: String? = null
    )
}
