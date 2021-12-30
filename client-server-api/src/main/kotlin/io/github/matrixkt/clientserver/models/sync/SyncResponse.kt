package io.github.matrixkt.clientserver.models.sync

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class SyncResponse(
    /**
     * The batch token to supply in the since param of the next `/sync` request.
     */
    @SerialName("next_batch")
    val nextBatch: String,

    /**
     * Updates to rooms.
     */
    val rooms: Rooms? = null,

    /**
     * The updates to the presence status of other users.
     */
    val presence: Presence? = null,

    /**
     * The global private data created by this user.
     */
    @SerialName("account_data")
    val accountData: AccountData? = null,

    /**
     * Information on the send-to-device messages for the client device,
     * as defined in [Send-to-Device messaging](https://matrix.org/docs/spec/client_server/r0.5.0#send-to-device-sync).
     */
    @SerialName("to_device")
    val toDevice: ToDevice? = null,

    /**
     * Information on end-to-end device updates, as specified in [End-to-end encryption](https://matrix.org/docs/spec/client_server/r0.5.0#device-lists-sync).
     * Note: only present on an incremental sync.
     */
    @SerialName("device_lists")
    val deviceLists: DeviceLists? = null,

    /**
     * Information on end-to-end encryption keys, as specified in [End-to-end encryption](https://matrix.org/docs/spec/client_server/r0.5.0#device-lists-sync).
     * For each key algorithm, the number of unclaimed one-time keys currently held on the server for this device.
     */
    @SerialName("device_one_time_keys_count")
    val deviceOneTimeKeysCount: Map<String, Long>? = null
)
