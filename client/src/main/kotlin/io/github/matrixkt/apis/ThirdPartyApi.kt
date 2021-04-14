package io.github.matrixkt.apis

import io.github.matrixkt.models.thirdparty.Location
import io.github.matrixkt.models.thirdparty.Protocol
import io.github.matrixkt.models.thirdparty.User
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import kotlin.reflect.KProperty0

public class ThirdPartyApi internal constructor(private val client: HttpClient, private val accessTokenProp: KProperty0<String>) {
    private inline val accessToken: String get() = accessTokenProp.get()

    /**
     * Fetches the overall metadata about protocols supported by the homeserver.
     * Includes both the available protocols and all fields required for queries against each protocol.
     *
     * **Rate-limited**: No.
     *
     * **Requires auth**: Yes.
     *
     * @return The protocols supported by the homeserver.
     */
    public suspend fun getProtocols(): Map<String, Protocol> {
        return client.get("_matrix/client/r0/thirdparty/protocols") {
            header("Authorization", "Bearer $accessToken")
        }
    }

    /**
     * Fetches the metadata from the homeserver about a particular third party protocol.
     *
     * **Rate-limited**: No.
     *
     * **Requires auth**: Yes.
     *
     * @param[protocol] The name of the protocol.
     * @return The protocol was found and metadata returned.
     */
    public suspend fun getProtocolMetadata(protocol: String): Protocol {
        return client.get {
            url {
                path("_matrix", "client", "r0", "thirdparty", "protocols", protocol)
            }
            header("Authorization", "Bearer $accessToken")
        }
    }

    /**
     * Requesting this endpoint with a valid protocol name results in a list of successful mapping results in a JSON array.
     * Each result contains objects to represent the Matrix room or rooms that represent a portal to this third party network.
     * Each has the Matrix room alias string, an identifier for the particular third party network protocol,
     * and an object containing the network-specific fields that comprise this identifier.
     * It should attempt to canonicalise the identifier as much as reasonably possible given the network type.
     *
     * **Rate-limited**: No.
     *
     * **Requires auth**: Yes.
     *
     * @param[protocol] The protocol used to communicate to the third party network.
     * @param[searchFields] One or more custom fields to help identify the third party location.
     *
     */
    public suspend fun queryLocationByProtocol(protocol: String, searchFields: Map<String, String> = emptyMap()): List<Location> {
        return client.get {
            url {
                path("_matrix", "client", "r0", "thirdparty", "location", protocol)
            }
            for ((key, value) in searchFields) {
                parameter(key, value)
            }

            header("Authorization", "Bearer $accessToken")
        }
    }

    /**
     * Retrieve a Matrix User ID linked to a user on the third party service, given a set of user parameters.
     *
     * **Rate-limited**: No.
     *
     * **Requires auth**: Yes.
     *
     * @param[protocol] The name of the protocol.
     * @param[fields] One or more custom fields that are passed to the AS to help identify the user.
     * @return List of matched third party users.
     */
    public suspend fun queryUserByProtocol(protocol: String, fields: Map<String, String> = emptyMap()): List<User> {
        return client.get {
            url {
                path("_matrix", "client", "r0", "thirdparty", "user", protocol)
            }
            for ((key, value) in fields) {
                parameter(key, value)
            }

            header("Authorization", "Bearer $accessToken")
        }
    }

    /**
     * Retrieve an array of third party network locations from a Matrix room alias.
     *
     * **Rate-limited**: No.
     *
     * **Requires auth**: Yes.
     *
     * @param[alias] The Matrix room alias to look up.
     * @return List of matched third party locations.
     */
    public suspend fun queryLocationByAlias(alias: String): List<Location> {
        return client.get("_matrix/client/r0/thirdparty/location") {
            parameter("alias", alias)

            header("Authorization", "Bearer $accessToken")
        }
    }

    /**
     * Retrieve an array of third party users from a Matrix User ID.
     *
     * **Rate-limited**: No.
     *
     * **Requires auth**: Yes.
     *
     * @param[userId] The Matrix User ID to look up.
     * @return List of matched third party users.
     */
    public suspend fun queryUserByID(userId: String): List<User> {
        return client.get("_matrix/client/r0/thirdparty/user") {
            parameter("userid", userId)

            header("Authorization", "Bearer $accessToken")
        }
    }
}
