package io.github.matrixkt.apis

import io.github.matrixkt.models.AuthenticationData
import io.github.matrixkt.models.Device

interface DeviceApi {
    /**
     * Gets information about all devices for the current user.
     *
     * **Rate-limited**: No.
     *
     * **Requires auth**: Yes.
     *
     * @return A list of all registered devices for this user.
     */
    suspend fun getDevices(): List<Device>

    /**
     * Gets information on a single device, by device id.
     *
     * **Rate-limited**: No.
     *
     * **Requires auth**: Yes.
     *
     * @param[deviceId] The device to retrieve.
     */
    suspend fun getDevice(deviceId: String): Device

    /**
     * Updates the metadata on the given device.
     *
     * **Rate-limited**: No.
     *
     * **Requires auth**: Yes.
     *
     * @param[deviceId] The device to update.
     * @param[displayName] The new display name for this device. If not given, the display name is unchanged.
     */
    suspend fun updateDevice(deviceId: String, displayName: String)

    /**
     * This API endpoint uses the [User-Interactive Authentication API](https://matrix.org/docs/spec/client_server/r0.5.0#user-interactive-authentication-api).
     *
     * Deletes the given device, and invalidates any access token associated with it.
     *
     * **Rate-limited**: No.
     *
     * **Requires auth**: Yes.
     *
     * @param[deviceId] The device to delete.
     * @param[auth] Additional authentication information for the user-interactive authentication API.
     */
    suspend fun deleteDevice(deviceId: String, auth: AuthenticationData? = null)

    /**
     * This API endpoint uses the [User-Interactive Authentication API](https://matrix.org/docs/spec/client_server/r0.5.0#user-interactive-authentication-api).
     *
     * Deletes the given devices, and invalidates any access token associated with them.
     *
     * **Rate-limited**: No.
     *
     * **Requires auth**: Yes.
     *
     * @param[devices] The list of device IDs to delete.
     * @param[auth] Additional authentication information for the user-interactive authentication API.
     */
    suspend fun deleteDevices(devices: List<String>, auth: AuthenticationData? = null)

    /**
     * This endpoint is used to send send-to-device events to a set of client devices.
     *
     * **Rate-limited**: No.
     *
     * **Requires auth**: Yes.
     *
     * @param[eventType] The type of event to send.
     * @param[txnId] The transaction ID for this event.
     * Clients should generate an ID unique across requests with the same access token;
     * it will be used by the server to ensure idempotency of requests.
     * @param[messages] The messages to send.
     * A map from user ID, to a map from device ID to message body.
     * The device ID may also be *, meaning all known devices for the user.
     */
    suspend fun sendToDevice(eventType: String, txnId: String, messages: Map<String, Map<String, Any>>? = null)
}
