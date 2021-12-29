package io.github.matrixkt.api

import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This endpoint allows the creation, modification and deletion of `pushers`_
 * for this user ID. The behaviour of this endpoint varies depending on the
 * values in the JSON body.
 */
public class PostPusher(
    public override val url: Url,
    /**
     * The pusher information.
     */
    public override val body: Body
) : MatrixRpc.WithAuth<RpcMethod.Post, PostPusher.Url, PostPusher.Body, Unit> {
    @Resource("_matrix/client/r0/pushers/set")
    @Serializable
    public class Url

    @Serializable
    public class PusherData(
        /**
         * The format to send notifications in to Push Gateways if the
         * ``kind`` is ``http``. The details about what fields the
         * homeserver should send to the push gateway are defined in the
         * `Push Gateway Specification`_. Currently the only format
         * available is 'event_id_only'.
         */
        public val format: String? = null,
        /**
         * Required if ``kind`` is ``http``. The URL to use to send
         * notifications to. MUST be an HTTPS URL with a path of
         * ``/_matrix/push/v1/notify``.
         */
        public val url: String? = null
    )

    @Serializable
    public class Body(
        /**
         * A string that will allow the user to identify what application
         * owns this pusher.
         */
        @SerialName("app_display_name")
        public val appDisplayName: String,
        /**
         * This is a reverse-DNS style identifier for the application.
         * It is recommended that this end with the platform, such that
         * different platform versions get different app identifiers.
         * Max length, 64 chars.
         *
         * If the ``kind`` is ``"email"``, this is ``"m.email"``.
         */
        @SerialName("app_id")
        public val appId: String,
        /**
         * If true, the homeserver should add another pusher with the
         * given pushkey and App ID in addition to any others with
         * different user IDs. Otherwise, the homeserver must remove any
         * other pushers with the same App ID and pushkey for different
         * users. The default is ``false``.
         */
        public val append: Boolean? = null,
        /**
         * A dictionary of information for the pusher implementation
         * itself. If ``kind`` is ``http``, this should contain ``url``
         * which is the URL to use to send notifications to.
         */
        public val `data`: PusherData,
        /**
         * A string that will allow the user to identify what device owns
         * this pusher.
         */
        @SerialName("device_display_name")
        public val deviceDisplayName: String,
        /**
         * The kind of pusher to configure. ``"http"`` makes a pusher that
         * sends HTTP pokes. ``"email"`` makes a pusher that emails the
         * user with unread notifications. ``null`` deletes the pusher.
         */
        public val kind: String,
        /**
         * The preferred language for receiving notifications (e.g. 'en'
         * or 'en-US').
         */
        public val lang: String,
        /**
         * This string determines which set of device specific rules this
         * pusher executes.
         */
        @SerialName("profile_tag")
        public val profileTag: String? = null,
        /**
         * This is a unique identifier for this pusher. The value you
         * should use for this is the routing or destination address
         * information for the notification, for example, the APNS token
         * for APNS or the Registration ID for GCM. If your notification
         * client has no such concept, use any unique identifier.
         * Max length, 512 bytes.
         *
         * If the ``kind`` is ``"email"``, this is the email address to
         * send notifications to.
         */
        public val pushkey: String
    )
}
