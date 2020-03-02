package io.github.matrixkt.impls

import io.github.matrixkt.apis.PushApi
import io.github.matrixkt.models.*
import io.github.matrixkt.models.push.PushRule
import io.ktor.client.HttpClient
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlin.reflect.KProperty0

internal class PushApiImpl(private val client: HttpClient, private val accessTokenProp: KProperty0<String>) : PushApi {
    private inline val accessToken: String get() = accessTokenProp.get()

    override suspend fun getPushers(): List<Pusher> {
        val response = client.get<Pushers>(path = "_matrix/client/r0/pushers") {
            header("Authorization", "Bearer $accessToken")
        }
        return response.pushers
    }

    override suspend fun postPusher(pusher: Pusher) {
        return client.put(path = "_matrix/client/r0/pushers/set") {
            header("Authorization", "Bearer $accessToken")

            contentType(ContentType.Application.Json)
            body = pusher
        }
    }

    override suspend fun getPushRules(): GetPushRulesResponse {
        return client.get(path = "_matrix/client/r0/pushrules/") {
            header("Authorization", "Bearer $accessToken")
        }
    }

    override suspend fun getPushRule(scope: String, kind: PushRuleKind, ruleId: String): PushRule {
        return client.get {
            url {
                path("_matrix", "client", "r0", "pushrules", scope, kind.name.toLowerCase(), ruleId)
            }
            header("Authorization", "Bearer $accessToken")
        }
    }

    override suspend fun deletePushRule(scope: String, kind: PushRuleKind, ruleId: String) {
        return client.delete {
            url {
                path("_matrix", "client", "r0", "pushrules", scope, kind.name.toLowerCase(), ruleId)
            }
            header("Authorization", "Bearer $accessToken")
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
        return client.put {
            url {
                path("_matrix", "client", "r0", "pushrules", scope, kind.name.toLowerCase(), ruleId)
                if (before != null) parameter("before", before)
                if (after != null) parameter("after", after)
            }
            header("Authorization", "Bearer $accessToken")
            contentType(ContentType.Application.Json)
            body = pushRule
        }
    }

    override suspend fun isPushRuleEnabled(scope: String, kind: PushRuleKind, ruleId: String): Boolean {
        val response = client.get<PushRuleEnabled> {
            url {
                path("_matrix", "client", "r0", "pushrules", scope, kind.name.toLowerCase(), ruleId, "enabled")
            }
            header("Authorization", "Bearer $accessToken")
        }
        return response.enabled
    }

    override suspend fun setPushRuleEnabled(scope: String, kind: PushRuleKind, ruleId: String, enabled: Boolean) {
        return client.put {
            url {
                path("_matrix", "client", "r0", "pushrules", scope, kind.name.toLowerCase(), ruleId, "enabled")
            }
            header("Authorization", "Bearer $accessToken")

            contentType(ContentType.Application.Json)
            body = PushRuleEnabled(enabled)
        }
    }

    override suspend fun getPushRuleActions(scope: String, kind: PushRuleKind, ruleId: String): List<String> {
        val response = client.get<PushRuleActions> {
            url {
                path("_matrix", "client", "r0", "pushrules", scope, kind.name.toLowerCase(), ruleId, "actions")
            }
            header("Authorization", "Bearer $accessToken")
        }
        return response.actions
    }

    override suspend fun setPushRuleActions(scope: String, kind: PushRuleKind, ruleId: String, actions: List<String>) {
        return client.put {
            url {
                path("_matrix", "client", "r0", "pushrules", scope, kind.name.toLowerCase(), ruleId, "actions")
            }
            header("Authorization", "Bearer $accessToken")

            contentType(ContentType.Application.Json)
            body = PushRuleActions(actions)
        }
    }
}
