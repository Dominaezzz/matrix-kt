package io.github.matrixkt.api

import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Gets all currently active pushers for the authenticated user.
 */
public class GetPushers(
    public override val url: Url
) : MatrixRpc.WithAuth<RpcMethod.Get, GetPushers.Url, Nothing, GetPushers.Response> {
    public override val body: Nothing
        get() = TODO()

    @Resource("_matrix/client/r0/pushers")
    @Serializable
    public class Url

    @Serializable
    public class PusherData(
        /**
         * The format to use when sending notifications to the Push
         * Gateway.
         */
        public val format: String? = null,
        /**
         * Required if ``kind`` is ``http``. The URL to use to send
         * notifications to.
         */
        public val url: String? = null
    )

    @Serializable
    public class Pusher(
        /**
         * A string that will allow the user to identify what application
         * owns this pusher.
         */
        @SerialName("app_display_name")
        public val appDisplayName: String,
        /**
         * This is a reverse-DNS style identifier for the application.
         * Max length, 64 chars.
         */
        @SerialName("app_id")
        public val appId: String,
        /**
         * A dictionary of information for the pusher implementation
         * itself.
         */
        public val `data`: PusherData,
        /**
         * A string that will allow the user to identify what device owns
         * this pusher.
         */
        @SerialName("device_display_name")
        public val deviceDisplayName: String,
        /**
         * The kind of pusher. ``"http"`` is a pusher that
         * sends HTTP pokes.
         */
        public val kind: String,
        /**
         * The preferred language for receiving notifications (e.g. 'en'
         * or 'en-US')
         */
        public val lang: String,
        /**
         * This string determines which set of device specific rules this
         * pusher executes.
         */
        @SerialName("profile_tag")
        public val profileTag: String? = null,
        /**
         * This is a unique identifier for this pusher. See ``/set`` for
         * more detail.
         * Max length, 512 bytes.
         */
        public val pushkey: String
    )

    @Serializable
    public class Response(
        /**
         * An array containing the current pushers for the user
         */
        public val pushers: List<Pusher> = emptyList()
    )
}
