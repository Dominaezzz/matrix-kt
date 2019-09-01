package io.github.matrixkt.impls

import io.github.matrixkt.apis.PushApi
import io.github.matrixkt.models.*
import io.github.matrixkt.models.push.PushRule
import io.ktor.client.HttpClient
import io.ktor.client.call.receive
import io.ktor.client.request.*
import io.ktor.client.response.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.io.core.use
import kotlin.reflect.KProperty0

internal class PushApiImpl(private val client: HttpClient, private val accessTokenProp: KProperty0<String>) : PushApi {
    private inline val accessToken: String get() = accessTokenProp.get()

    override suspend fun getPushers(): List<Pusher> {
        val response = client.get<HttpResponse>(path = "_matrix/client/r0/pushers") {
            header("Authorization", "Bearer $accessToken")
        }

        response.use {
            when (it.status) {
                HttpStatusCode.OK -> return it.receive<Pushers>().pushers
                else -> throw it.receive<MatrixError>()
            }
        }
    }

    override suspend fun postPusher(pusher: Pusher) {
        val response = client.put<HttpResponse>(path = "_matrix/client/r0/pushers/set") {
            header("Authorization", "Bearer $accessToken")

            contentType(ContentType.Application.Json)
            body = pusher
        }

        response.use {
            when (it.status) {
                HttpStatusCode.OK -> return it.receive()
                else -> throw it.receive<MatrixError>()
            }
        }
    }

    override suspend fun getPushRules(): GetPushRulesResponse {
        val response = client.get<HttpResponse>(path = "_matrix/client/r0/pushrules/") {
            header("Authorization", "Bearer $accessToken")
        }

        response.use {
            when (it.status) {
                HttpStatusCode.OK -> return it.receive()
                else -> throw it.receive<MatrixError>()
            }
        }
    }

    override suspend fun getPushRule(scope: String, kind: PushRuleKind, ruleId: String): PushRule {
        val response = client.get<HttpResponse> {
            url {
                path("_matrix", "client", "r0", "pushrules", scope, kind.name.toLowerCase(), ruleId)
            }
            header("Authorization", "Bearer $accessToken")
        }

        response.use {
            when (it.status) {
                HttpStatusCode.OK -> return it.receive()
                else -> throw it.receive<MatrixError>()
            }
        }
    }

    override suspend fun deletePushRule(scope: String, kind: PushRuleKind, ruleId: String) {
        val response = client.delete<HttpResponse>() {
            url {
                path("_matrix", "client", "r0", "pushrules", scope, kind.name.toLowerCase(), ruleId)
            }
            header("Authorization", "Bearer $accessToken")
        }

        response.use {
            when (it.status) {
                HttpStatusCode.OK -> return it.receive()
                else -> throw it.receive<MatrixError>()
            }
        }
    }

    override suspend fun setPushRule(
        scope: String,
        kind: PushRuleKind,
        ruleId: String,
        before: String?,
        after: String?,
        pushRule: SetPushRuleRequest
    ) {
        val response = client.put<HttpResponse> {
            url {
                path("_matrix", "client", "r0", "pushrules", scope, kind.name.toLowerCase(), ruleId)
                if (before != null) parameter("before", before)
                if (after != null) parameter("after", after)
            }
            header("Authorization", "Bearer $accessToken")
            contentType(ContentType.Application.Json)
            body = pushRule
        }

        response.use {
            when (it.status) {
                HttpStatusCode.OK -> return it.receive()
                else -> throw it.receive<MatrixError>()
            }
        }
    }

    override suspend fun isPushRuleEnabled(scope: String, kind: PushRuleKind, ruleId: String): Boolean {
        val response = client.get<HttpResponse> {
            url {
                path("_matrix", "client", "r0", "pushrules", scope, kind.name.toLowerCase(), ruleId, "enabled")
            }
            header("Authorization", "Bearer $accessToken")
        }

        response.use {
            when (it.status) {
                HttpStatusCode.OK -> return it.receive<PushRuleEnabled>().enabled
                else -> throw it.receive<MatrixError>()
            }
        }
    }

    override suspend fun setPushRuleEnabled(scope: String, kind: PushRuleKind, ruleId: String, enabled: Boolean) {
        val response = client.put<HttpResponse> {
            url {
                path("_matrix", "client", "r0", "pushrules", scope, kind.name.toLowerCase(), ruleId, "enabled")
            }
            header("Authorization", "Bearer $accessToken")

            contentType(ContentType.Application.Json)
            body = PushRuleEnabled(enabled)
        }

        response.use {
            when (it.status) {
                HttpStatusCode.OK -> return it.receive()
                else -> throw it.receive<MatrixError>()
            }
        }
    }

    override suspend fun getPushRuleActions(scope: String, kind: PushRuleKind, ruleId: String): List<String> {
        val response = client.get<HttpResponse> {
            url {
                path("_matrix", "client", "r0", "pushrules", scope, kind.name.toLowerCase(), ruleId, "actions")
            }
            header("Authorization", "Bearer $accessToken")
        }

        response.use {
            when (it.status) {
                HttpStatusCode.OK -> return it.receive<PushRuleActions>().actions
                else -> throw it.receive<MatrixError>()
            }
        }
    }

    override suspend fun setPushRuleActions(scope: String, kind: PushRuleKind, ruleId: String, actions: List<String>) {
        val response = client.put<HttpResponse> {
            url {
                path("_matrix", "client", "r0", "pushrules", scope, kind.name.toLowerCase(), ruleId, "actions")
            }
            header("Authorization", "Bearer $accessToken")

            contentType(ContentType.Application.Json)
            body = PushRuleActions(actions)
        }

        response.use {
            when (it.status) {
                HttpStatusCode.OK -> return it.receive()
                else -> throw it.receive<MatrixError>()
            }
        }
    }
}
