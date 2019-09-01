package io.github.matrixkt.apis

import io.github.matrixkt.models.*
import io.github.matrixkt.models.push.PushRule

interface PushApi {
    /**
     * Gets the current pushers for the authenticated user.
     *
     * Gets all currently active pushers for the authenticated user.
     *
     * **Rate-limited**: No.
     *
     * **Requires auth**: Yes.
     */
    suspend fun getPushers(): List<Pusher>

    /**
     * This endpoint allows the creation, modification and deletion of `pushers`_ for this user ID.
     * The behaviour of this endpoint varies depending on the values in the JSON body.
     *
     * **Rate-limited**: Yes.
     *
     * **Requires auth**: Yes.
     *
     * @param[pusher] The pusher information.
     */
    suspend fun postPusher(pusher: Pusher)

    /**
     * Retrieve all push rulesets.
     *
     * Retrieve all push rulesets for this user. Clients can "drill-down" on
     * the rulesets by suffixing a ``scope`` to this path e.g. `/pushrules/global/`.
     * This will return a subset of this data under the specified key e.g. the ``global`` key.
     *
     * **Rate-limited**: No.
     *
     * **Requires auth**: Yes.
     */
    suspend fun getPushRules(): GetPushRulesResponse

    /**
     * Retrieve a push rule.
     *
     * Retrieve a single specified push rule.
     *
     * **Rate-limited**: No.
     *
     * **Requires auth**: Yes.
     *
     * @param[scope] ``global`` to specify global rules.
     * @param[kind] The kind of rule.
     * @param[ruleId] The identifier for the rule.
     */
    suspend fun getPushRule(scope: String, kind: PushRuleKind, ruleId: String): PushRule

    /**
     * Delete a push rule.
     *
     * This endpoint removes the push rule defined in the path.
     *
     * **Rate-limited**: No.
     *
     * **Requires auth**: Yes.
     *
     * @param[scope] ``global`` to specify global rules.
     * @param[kind] The kind of rule.
     * @param[ruleId] The identifier for the rule.
     */
    suspend fun deletePushRule(scope: String, kind: PushRuleKind, ruleId: String)

    /**
     * Add or change a push rule.
     *
     * This endpoint allows the creation, modification and deletion of pushers for this user ID.
     * The behaviour of this endpoint varies depending on the values in the JSON body.
     * When creating push rules, they MUST be enabled by default.
     *
     * **Rate-limited**: Yes.
     *
     * **Requires auth**: Yes.
     *
     * @param[scope] ``global`` to specify global rules.
     * @param[kind] The kind of rule.
     * @param[ruleId] The identifier for the rule.
     * @param[before] Use 'before' with a ``rule_id`` as its value to make the new rule the
     * next-most important rule with respect to the given user defined rule.
     * It is not possible to add a rule relative to a predefined server rule.
     * @param[after] This makes the new rule the next-less important rule relative to the
     * given user defined rule.
     * It is not possible to add a rule relative to a predefined server rule.
     */
    suspend fun setPushRule(scope: String, kind: PushRuleKind, ruleId: String, before: String? = null, after: String? = null, pushRule: SetPushRuleRequest)

    /**
     * Get whether a push rule is enabled.
     *
     * This endpoint gets whether the specified push rule is enabled.
     *
     * **Rate-limited**: No.
     *
     * **Requires auth**: Yes.
     *
     * @param[scope] Either ``global`` or ``device/<profile_tag>`` to specify global
     * rules or device rules for the given ``profile_tag``.
     * @param[kind] The kind of rule.
     * @param[ruleId] The identifier for the rule.
     * @return Whether the push rule is enabled or not.
     */
    suspend fun isPushRuleEnabled(scope: String, kind: PushRuleKind, ruleId: String): Boolean

    /**
     * Enable or disable a push rule.
     *
     * This endpoint allows clients to enable or disable the specified push rule.
     *
     * **Rate-limited**: No.
     *
     * **Requires auth**: Yes.
     *
     * @param[scope] Either ``global`` or ``device/<profile_tag>`` to specify global
     * rules or device rules for the given ``profile_tag``.
     * @param[kind] The kind of rule.
     * @param[ruleId] The identifier for the rule.
     */
    suspend fun setPushRuleEnabled(scope: String, kind: PushRuleKind, ruleId: String, enabled: Boolean)

    /**
     * The actions for a push rule.
     *
     * This endpoint get the actions for the specified push rule.
     *
     * **Rate-limited**: No.
     *
     * **Requires auth**: Yes.
     *
     * @param[scope] Either ``global`` or ``device/<profile_tag>`` to specify global
     * rules or device rules for the given ``profile_tag``.
     * @param[kind] The kind of rule.
     * @param[ruleId] The identifier for the rule.
     * @return The action(s) to perform for this rule.
     */
    suspend fun getPushRuleActions(scope: String, kind: PushRuleKind, ruleId: String): List<String>

    /**
     * Set the actions for a push rule.
     *
     * This endpoint allows clients to change the actions of a push rule.
     * This can be used to change the actions of builtin rules.
     *
     * **Rate-limited**: No.
     *
     * **Requires auth**: Yes.
     *
     * @param[scope] Either ``global`` or ``device/<profile_tag>`` to specify global
     * rules or device rules for the given ``profile_tag``.
     * @param[kind] The kind of rule.
     * @param[ruleId] The identifier for the rule.
     * @param[actions] The action(s) to perform for this rule.
     */
    suspend fun setPushRuleActions(scope: String, kind: PushRuleKind, ruleId: String, actions: List<String>)
}
