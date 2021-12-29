package io.github.matrixkt.api

import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

/**
 * Set some account_data for the client. This config is only visible to the user
 * that set the account_data. The config will be synced to clients in the
 * top-level ``account_data``.
 */
public class SetAccountData(
    public override val url: Url,
    /**
     * The content of the account_data
     */
    public override val body: JsonObject
) : MatrixRpc.WithAuth<RpcMethod.Put, SetAccountData.Url, JsonObject, Unit> {
    @Resource("_matrix/client/r0/user/{userId}/account_data/{type}")
    @Serializable
    public class Url(
        /**
         * The ID of the user to set account_data for. The access token must be
         * authorized to make requests for this user ID.
         */
        public val userId: String,
        /**
         * The event type of the account_data to set. Custom types should be
         * namespaced to avoid clashes.
         */
        public val type: String
    )
}
