package io.github.matrixkt.api

import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Upgrades the given room to a particular room version.
 */
public class UpgradeRoom(
    public override val url: Url,
    /**
     * The request body
     */
    public override val body: Body
) : MatrixRpc.WithAuth<RpcMethod.Post, UpgradeRoom.Url, UpgradeRoom.Body, UpgradeRoom.Response> {
    @Resource("_matrix/client/r0/rooms/{roomId}/upgrade")
    @Serializable
    public class Url(
        /**
         * The ID of the room to upgrade.
         */
        public val roomId: String
    )

    @Serializable
    public class Body(
        /**
         * The new version for the room.
         */
        @SerialName("new_version")
        public val newVersion: String
    )

    @Serializable
    public class Response(
        /**
         * The ID of the new room.
         */
        @SerialName("replacement_room")
        public val replacementRoom: String
    )
}
