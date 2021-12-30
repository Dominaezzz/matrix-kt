package io.github.matrixkt.clientserver.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

/**
 * Used by servers to indicate that additional authentication information is required,
 */
@Serializable
public class AuthenticationResponse(
    /**
     * A list of the login flows supported by the server for this API.
     */
    public val flows: List<FlowInformation>,

    /**
     * Contains any information that the client will need to know in order to
     * use a given type of authentication. For each login type presented,
     * that type may be present as a key in this dictionary. For example, the
     * public part of an OAuth client ID could be given here.
     */
    public val params: Map<String, JsonObject> = emptyMap(),

    /**
     * This is a session identifier that the client must pass back to the home server,
     * if one is provided, in subsequent attempts to authenticate in the same API call.
     */
    public val session: String? = null,

    /**
     * A list of the stages the client has completed successfully.
     */
    public val completed: List<String> = emptyList()
) {
    @Serializable
    public class FlowInformation(
        /**
         * The login type of each of the stages required to complete this authentication flow.
         */
        public val stages: List<String>
    )
}
