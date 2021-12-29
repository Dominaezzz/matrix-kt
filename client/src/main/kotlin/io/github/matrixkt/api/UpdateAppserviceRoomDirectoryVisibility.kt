package io.github.matrixkt.api

import io.github.matrixkt.models.RoomVisibility
import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.Serializable

/**
 * Updates the visibility of a given room on the application service's room
 * directory.
 *
 * This API is similar to the room directory visibility API used by clients
 * to update the homeserver's more general room directory.
 *
 * This API requires the use of an application service access token (``as_token``)
 * instead of a typical client's access_token. This API cannot be invoked by
 * users who are not identified as application services.
 */
public class UpdateAppserviceRoomDirectoryVisibility(
    public override val url: Url,
    public override val body: Body
) : MatrixRpc.WithAuth<RpcMethod.Put, UpdateAppserviceRoomDirectoryVisibility.Url,
        UpdateAppserviceRoomDirectoryVisibility.Body,
        Unit> {
    @Resource("_matrix/client/r0/directory/list/appservice/{networkId}/{roomId}")
    @Serializable
    public class Url(
        /**
         * The protocol (network) ID to update the room list for. This would
         * have been provided by the application service as being listed as
         * a supported protocol.
         */
        public val networkId: String,
        /**
         * The room ID to add to the directory.
         */
        public val roomId: String
    )

    @Serializable
    public class Body(
        /**
         * Whether the room should be visible (public) in the directory
         * or not (private).
         */
        public val visibility: RoomVisibility
    )
}
