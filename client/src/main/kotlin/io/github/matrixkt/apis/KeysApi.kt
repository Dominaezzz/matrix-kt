package io.github.matrixkt.apis

import io.github.matrixkt.models.*
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlin.reflect.KProperty0

class KeysApi internal constructor(private val client: HttpClient, private val accessTokenProp: KProperty0<String>) {
    private inline val accessToken: String get() = accessTokenProp.get()

    /**
     * Upload end-to-end encryption keys.
     *
     * Publishes end-to-end encryption keys for the device.
     *
     * **Rate-limited**: No.
     *
     * **Requires auth**: Yes.
     */
    suspend fun uploadKeys(keys: UploadKeysRequest): UploadKeysResponse {
        return client.post(path = "_matrix/client/r0/keys/upload") {
            header("Authorization", "Bearer $accessToken")
            contentType(ContentType.Application.Json)
            body = keys
        }
    }

    /**
     * Download device identity keys.
     *
     * Returns the current devices and identity keys for the given users.
     *
     * **Rate-limited**: No.
     *
     * **Requires auth**: Yes.
     *
     */
    suspend fun queryKeys(query: QueryKeysRequest): QueryKeysResponse {
        return client.post(path = "_matrix/client/r0/keys/query") {
            header("Authorization", "Bearer $accessToken")
            contentType(ContentType.Application.Json)
            body = query
        }
    }

    /**
     * Claim one-time encryption keys.
     *
     * Claims one-time keys for use in pre-key messages.
     *
     * **Rate-limited**: No.
     *
     * **Requires auth**: Yes.
     */
    suspend fun claimKeys(query: ClaimKeysRequest): ClaimKeysResponse {
        return client.post(path = "_matrix/client/r0/keys/claim") {
            header("Authorization", "Bearer $accessToken")
            contentType(ContentType.Application.Json)
            body = query
        }
    }

    /**
     * Query users with recent device key updates.
     *
     * Gets a list of users who have updated their device identity keys since a previous sync token.
     *
     * The server should include in the results any users who:
     * - currently share a room with the calling user (ie, both users have membership state ``join``); *and*
     * - added new device identity keys or removed an existing device with identity keys, between ``from`` and ``to``.
     *
     * **Rate-limited**: No.
     *
     * **Requires auth**: Yes.
     *
     * @param[from] The desired start point of the list.
     * Should be the ``next_batch`` field from a response to an earlier call to |/sync|.
     * Users who have not uploaded new device identity keys since this point,
     * nor deleted existing devices with identity keys since then,
     * will be excluded from the results.
     *
     * @param[to] The desired end point of the list.
     * Should be the ``next_batch`` field from a recent call to |/sync| - typically the most recent such call.
     * This may be used by the server as a hint to check its caches are up to date.
     */
    suspend fun getKeysChanges(from: String, to: String): KeyChangesResponse {
        return client.get {
            url {
                encodedPath = "_matrix/client/r0/keys/changes"
                parameter("from", from)
                parameter("to", to)
            }
            header("Authorization", "Bearer $accessToken")
        }
    }
}
