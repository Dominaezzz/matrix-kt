package io.github.matrixkt.api

import io.github.matrixkt.utils.MatrixRpc
import io.github.matrixkt.utils.RpcMethod
import io.ktor.resources.*
import kotlinx.serialization.Serializable

/**
 * Gets a list of users who have updated their device identity keys since a
 * previous sync token.
 *
 * The server should include in the results any users who:
 *
 * * currently share a room with the calling user (ie, both users have
 *   membership state ``join``); *and*
 * * added new device identity keys or removed an existing device with
 *   identity keys, between ``from`` and ``to``.
 */
public class GetKeysChanges(
    public override val url: Url
) : MatrixRpc.WithAuth<RpcMethod.Get, GetKeysChanges.Url, Nothing, GetKeysChanges.Response> {
    public override val body: Nothing
        get() = TODO()

    @Resource("_matrix/client/r0/keys/changes")
    @Serializable
    public class Url(
        /**
         * The desired start point of the list. Should be the ``next_batch`` field
         * from a response to an earlier call to |/sync|. Users who have not
         * uploaded new device identity keys since this point, nor deleted
         * existing devices with identity keys since then, will be excluded
         * from the results.
         */
        public val from: String,
        /**
         * The desired end point of the list. Should be the ``next_batch``
         * field from a recent call to |/sync| - typically the most recent
         * such call. This may be used by the server as a hint to check its
         * caches are up to date.
         */
        public val to: String
    )

    @Serializable
    public class Response(
        /**
         * The Matrix User IDs of all users who updated their device
         * identity keys.
         */
        public val changed: List<String> = emptyList(),
        /**
         * The Matrix User IDs of all users who may have left all
         * the end-to-end encrypted rooms they previously shared
         * with the user.
         */
        public val left: List<String> = emptyList()
    )
}
