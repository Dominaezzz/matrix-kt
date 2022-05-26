package io.github.matrixkt.events.contents.room

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * An event to indicate which servers are permitted to participate in the room.
 * Server ACLs may allow or deny groups of hosts. All servers participating in the room,
 * including those that are denied, are expected to uphold the server ACL.
 * Servers that do not uphold the ACLs MUST be added to the denied hosts
 * list in order for the ACLs to remain effective.
 *
 * The [allow] and [deny] lists are lists of globs supporting ``?`` and ``*`` as wildcards.
 * When comparing against the server ACLs, the suspect server's port number must not be considered.
 * Therefore ``evil.com``, ``evil.com:8448``, and ``evil.com:1234`` would all match rules that apply to ``evil.com``, for example.
 *
 * The ACLs are applied to servers when they make requests, and are applied in the following order:
 *
 * 1. If there is no ``m.room.server_acl`` event in the room state, allow.
 * #. If the server name is an IP address (v4 or v6) literal, and [allowIpLiterals] is present and `false`, deny.
 * #. If the server name matches an entry in the ``deny`` list, deny.
 * #. If the server name matches an entry in the ``allow`` list, allow.
 * #. Otherwise, deny.
 *
 * .. Note::
 * Server ACLs do not restrict the events relative to the room DAG via authorisation
 * rules, but instead act purely at the network layer to determine which servers are
 * allowed to connect and interact with a given room.
 *
 * .. WARNING::
 * Failing to provide an [allow] rule of some kind will prevent **all**
 * servers from participating in the room, including the sender. This renders
 * the room unusable. A common allow rule is ``[ "*" ]`` which would still
 * permit the use of the [deny] list without losing the room.
 *
 * .. WARNING::
 * All compliant servers must implement server ACLs.  However, legacy or noncompliant
 * servers exist which do not uphold ACLs, and these MUST be manually appended to
 * the denied hosts list when setting an ACL to prevent them from leaking events from
 * banned servers into a room. Currently, the only way to determine noncompliant hosts is
 * to check the ``prev_events`` of leaked events, therefore detecting servers which
 * are not upholding the ACLs. Server versions can also be used to try to detect hosts that
 * will not uphold the ACLs, although this is not comprehensive. Server ACLs were added
 * in Synapse v0.32.0, although other server implementations and versions exist in the world.
 */
@SerialName("m.room.server_acl")
@Serializable
public data class ServerAclContent(
    /**
     * True to allow server names that are IP address literals.
     * False to deny. Defaults to true if missing or otherwise not a boolean.
     *
     * This is strongly recommended to be set to ``false`` as servers running
     * with IP literal names are strongly discouraged in order to require
     * legitimate homeservers to be backed by a valid registered domain name.
     */
    @SerialName("allow_ip_literals")
    val allowIpLiterals: Boolean = true,

    /**
     * The server names to allow in the room, excluding any port information.
     * Wildcards may be used to cover a wider range of hosts, where ``*``
     * matches zero or more characters and ``?`` matches exactly one character.
     *
     * **This defaults to an empty list when not provided, effectively disallowing every server.**
     */
    val allow: List<String> = emptyList(),

    /**
     * The server names to disallow in the room, excluding any port information.
     * Wildcards may be used to cover a wider range of hosts, where ``*``
     * matches zero or more characters and ``?`` matches exactly one character.
     *
     * This defaults to an empty list when not provided.
     */
    val deny: List<String> = emptyList()
)
