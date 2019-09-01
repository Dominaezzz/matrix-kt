package io.github.matrixkt.apis

import io.github.matrixkt.models.filter.Filter

interface FilterApi {
    /**
     * Uploads a new filter definition to the homeserver.
     * Returns a filter ID that may be used in future requests to restrict which events are returned to the client.
     *
     * **Rate-limited**: No.
     *
     * **Requires auth**: Yes.
     *
     * @param[userId] The id of the user uploading the filter. The access token must be authorized to make requests for this user id.
     * @return The ID of the filter that was created.
     * Cannot start with a '{' as this character is used to determine if the filter
     * provided is inline JSON or a previously declared filter by homeservers on some APIs.
     */
    suspend fun defineFilter(userId: String, filter: Filter): String

    /**
     * Download a filter.
     *
     * **Rate-limited**: No.
     *
     * **Requires auth**: Yes.
     *
     * @param[userId] The user ID to download a filter for.
     * @param[filterId] The filter ID to download.
     */
    suspend fun getFilter(userId: String, filterId: String): Filter
}
