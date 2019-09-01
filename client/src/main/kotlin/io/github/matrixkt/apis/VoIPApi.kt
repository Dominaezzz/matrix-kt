package io.github.matrixkt.apis

import io.github.matrixkt.models.TurnServerResponse

interface VoIPApi {
    /**
     * This API provides credentials for the client to use when initiating calls.
     *
     * **Rate-limited**: Yes.
     *
     * **Requires auth**: Yes.
     */
    suspend fun getTurnServer(): TurnServerResponse
}
