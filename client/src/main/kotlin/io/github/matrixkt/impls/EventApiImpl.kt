package io.github.matrixkt.impls

import io.github.matrixkt.apis.EventApi
import io.github.matrixkt.models.*
import io.github.matrixkt.models.filter.Filter
import io.github.matrixkt.utils.MatrixJson
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import kotlin.reflect.KProperty0

internal class EventApiImpl(private val client: HttpClient, private val accessTokenProp: KProperty0<String>) : EventApi {
    private inline val accessToken: String get() = accessTokenProp.get()

    override suspend fun sync(
        filter: String?,
        since: String?,
        fullState: Boolean?,
        setPresence: Presence?,
        timeout: Long?
    ): SyncResponse {
        return client.get("/_matrix/client/r0/sync") {
            if (filter != null) parameter("filter", filter)
            if (since != null) parameter("since", since)
            if (fullState != null) parameter("full_state", fullState)
            if (setPresence != null) parameter("set_presence", setPresence.name.toLowerCase())
            if (timeout != null) parameter("timeout", timeout)

            header("Authorization", "Bearer $accessToken")
        }
    }

    override suspend fun sync(
        filter: Filter,
        since: String?,
        fullState: Boolean?,
        setPresence: Presence?,
        timeout: Long?
    ): SyncResponse {
        return sync(
            MatrixJson.stringify(Filter.serializer(), filter),
            since, fullState, setPresence, timeout
        )
    }
}
