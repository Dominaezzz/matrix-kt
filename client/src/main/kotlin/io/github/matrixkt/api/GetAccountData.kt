package io.github.matrixkt.api

import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

/**
 * Get some account_data for the client. This config is only visible to the user
 * that set the account_data.
 */
public class GetAccountData(
    public override val url: Url
) : MatrixRpc.WithAuth<RpcMethod.Get, GetAccountData.Url, Nothing, JsonObject> {
    public override val body: Nothing
        get() = TODO()

    @Resource("_matrix/client/r0/user/{userId}/account_data/{type}")
    @Serializable
    public class Url(
        /**
         * The ID of the user to get account_data for. The access token must be
         * authorized to make requests for this user ID.
         */
        public val userId: String,
        /**
         * The event type of the account_data to get. Custom types should be
         * namespaced to avoid clashes.
         */
        public val type: String
    )
}
