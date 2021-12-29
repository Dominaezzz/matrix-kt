package io.github.matrixkt.api

import io.github.matrixkt.models.Invite3pid
import io.github.matrixkt.models.RoomPreset
import io.github.matrixkt.models.RoomVisibility
import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

/**
 * Create a new room with various configuration options.
 *
 * The server MUST apply the normal state resolution rules when creating
 * the new room, including checking power levels for each event. It MUST
 * apply the events implied by the request in the following order:
 *
 * 1. The `m.room.create` event itself. Must be the first event in the
 *    room.
 *
 * 2. An `m.room.member` event for the creator to join the room. This is
 *    needed so the remaining events can be sent.
 *
 * 3. A default `m.room.power_levels` event, giving the room creator
 *    (and not other members) permission to send state events. Overridden
 *    by the `power_level_content_override` parameter.
 *
 * 4. An `m.room.canonical_alias` event if `room_alias_name` is given.
 *
 * 5. Events set by the `preset`. Currently these are the `m.room.join_rules`,
 *    `m.room.history_visibility`, and `m.room.guest_access` state events.
 *
 * 6. Events listed in `initial_state`, in the order that they are
 *    listed.
 *
 * 7. Events implied by `name` and `topic` (`m.room.name` and `m.room.topic`
 *    state events).
 *
 * 8. Invite events implied by `invite` and `invite_3pid` (`m.room.member` with
 *    `membership: invite` and `m.room.third_party_invite`).
 *
 * The available presets do the following with respect to room state:
 *
 * | Preset                 | `join_rules` | `history_visibility` | `guest_access` | Other |
 * |------------------------|--------------|----------------------|----------------|-------|
 * | `private_chat`         | `invite`     | `shared`             | `can_join`     |       |
 * | `trusted_private_chat` | `invite`     | `shared`             | `can_join`     | All invitees
 * are given the same power level as the room creator. |
 * | `public_chat`          | `public`     | `shared`             | `forbidden`    |       |
 *
 * The server will create a `m.room.create` event in the room with the
 * requesting user as the creator, alongside other keys provided in the
 * `creation_content`.
 */
public class CreateRoom(
    public override val url: Url,
    /**
     * The desired room configuration.
     */
    public override val body: Body
) : MatrixRpc.WithAuth<RpcMethod.Post, CreateRoom.Url, CreateRoom.Body, CreateRoom.Response> {
    @Resource("_matrix/client/r0/createRoom")
    @Serializable
    public class Url

    @Serializable
    public class StateEvent(
        /**
         * The content of the event.
         */
        public val content: JsonObject,
        /**
         * The state_key of the state event. Defaults to an empty string.
         */
        @SerialName("state_key")
        public val stateKey: String? = null,
        /**
         * The type of event to send.
         */
        public val type: String
    )

    @Serializable
    public class Body(
        /**
         * Extra keys, such as ``m.federate``, to be added to the content
         * of the `m.room.create`_ event. The server will clobber the following
         * keys: ``creator``, ``room_version``. Future versions of the specification
         * may allow the server to clobber other keys.
         */
        @SerialName("creation_content")
        public val creationContent: JsonObject? = null,
        /**
         * A list of state events to set in the new room. This allows
         * the user to override the default state events set in the new
         * room. The expected format of the state events are an object
         * with type, state_key and content keys set.
         *
         * Takes precedence over events set by ``preset``, but gets
         * overriden by ``name`` and ``topic`` keys.
         */
        @SerialName("initial_state")
        public val initialState: List<StateEvent>? = null,
        /**
         * A list of user IDs to invite to the room. This will tell the
         * server to invite everyone in the list to the newly created room.
         */
        public val invite: List<String>? = null,
        /**
         * A list of objects representing third party IDs to invite into
         * the room.
         */
        @SerialName("invite_3pid")
        public val invite3pid: List<Invite3pid>? = null,
        /**
         * This flag makes the server set the `is_direct` flag on the
         * `m.room.member` events sent to the users in `invite` and
         * `invite_3pid`. See [Direct Messaging](/client-server-api/#direct-messaging) for more
         * information.
         */
        @SerialName("is_direct")
        public val isDirect: Boolean? = null,
        /**
         * If this is included, an `m.room.name` event will be sent
         * into the room to indicate the name of the room. See Room
         * Events for more information on `m.room.name`.
         */
        public val name: String? = null,
        /**
         * The power level content to override in the default power level
         * event. This object is applied on top of the generated
         * [`m.room.power_levels`](/client-server-api/#mroompower_levels)
         * event content prior to it being sent to the room. Defaults to
         * overriding nothing.
         */
        @SerialName("power_level_content_override")
        public val powerLevelContentOverride: JsonObject? = null,
        /**
         * Convenience parameter for setting various default state events
         * based on a preset.
         *
         * If unspecified, the server should use the `visibility` to determine
         * which preset to use. A visbility of `public` equates to a preset of
         * `public_chat` and `private` visibility equates to a preset of
         * `private_chat`.
         */
        public val preset: RoomPreset? = null,
        /**
         * The desired room alias **local part**. If this is included, a
         * room alias will be created and mapped to the newly created
         * room. The alias will belong on the *same* homeserver which
         * created the room. For example, if this was set to "foo" and
         * sent to the homeserver "example.com" the complete room alias
         * would be `#foo:example.com`.
         *
         * The complete room alias will become the canonical alias for
         * the room and an `m.room.canonical_alias` event will be sent
         * into the room.
         */
        @SerialName("room_alias_name")
        public val roomAliasName: String? = null,
        /**
         * The room version to set for the room. If not provided, the homeserver is
         * to use its configured default. If provided, the homeserver will return a
         * 400 error with the errcode `M_UNSUPPORTED_ROOM_VERSION` if it does not
         * support the room version.
         */
        @SerialName("room_version")
        public val roomVersion: String? = null,
        /**
         * If this is included, an `m.room.topic` event will be sent
         * into the room to indicate the topic for the room. See Room
         * Events for more information on `m.room.topic`.
         */
        public val topic: String? = null,
        /**
         * A `public` visibility indicates that the room will be shown
         * in the published room list. A `private` visibility will hide
         * the room from the published room list. Rooms default to
         * `private` visibility if this key is not included. NB: This
         * should not be confused with `join_rules` which also uses the
         * word `public`.
         */
        public val visibility: RoomVisibility? = null
    )

    @Serializable
    public class Response(
        /**
         * The created room's ID.
         */
        @SerialName("room_id")
        public val roomId: String
    )
}
