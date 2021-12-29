package io.github.matrixkt.api

import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

/**
 * Get some account_data for the client on a given room. This config is only
 * visible to the user that set the account_data.
 */
public class GetAccountDataPerRoom(
    public override val url: Url
) : MatrixRpc.WithAuth<RpcMethod.Get, GetAccountDataPerRoom.Url, Nothing, JsonObject> {
    public override val body: Nothing
        get() = TODO()

    @Resource("_matrix/client/r0/user/{userId}/rooms/{roomId}/account_data/{type}")
    @Serializable
    public class Url(
        /**
         * The ID of the user to set account_data for. The access token must be
         * authorized to make requests for this user ID.
         */
        public val userId: String,
        /**
         * The ID of the room to get account_data for.
         */
        public val roomId: String,
        /**
         * The event type of the account_data to get. Custom types should be
         * namespaced to avoid clashes.
         */
        public val type: String
    )
}
