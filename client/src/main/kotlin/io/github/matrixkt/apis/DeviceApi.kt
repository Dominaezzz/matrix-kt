package io.github.matrixkt.apis

import io.github.matrixkt.models.AuthenticationData
import io.github.matrixkt.models.Device
import io.github.matrixkt.models.GetDevicesResponse
import io.github.matrixkt.models.SendToDeviceRequest
import io.github.matrixkt.models.events.contents.RoomMessageContent.Redacted.body
import io.ktor.client.HttpClient
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.json.json
import kotlinx.serialization.json.jsonArray
import kotlin.reflect.KProperty0

class DeviceApi internal constructor(private val client: HttpClient, private val accessTokenProp: KProperty0<String>){
    private inline val accessToken: String get() = accessTokenProp.get()

    /**
     * Gets information about all devices for the current user.
     *
     * **Rate-limited**: No.
     *
     * **Requires auth**: Yes.
     *
     * @return A list of all registered devices for this user.
     */
    suspend fun getDevices(): List<Device> {
        val response = client.get<GetDevicesResponse>("_matrix/client/r0/devices") {
            header("Authorization", "Bearer $accessToken")
        }
        return response.devices
    }

    /**
     * Gets information on a single device, by device id.
     *
     * **Rate-limited**: No.
     *
     * **Requires auth**: Yes.
     *
     * @param[deviceId] The device to retrieve.
     */
    suspend fun getDevice(deviceId: String): Device {
        return client.get {
            url {
                path("_matrix", "client", "r0", "devices", deviceId)
            }

            header("Authorization", "Bearer $accessToken")
        }
    }

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
    suspend fun updateDevice(deviceId: String, displayName: String) {
        return client.put {
            url {
                path("_matrix", "client", "r0", "devices", deviceId)
            }

            header("Authorization", "Bearer $accessToken")

            contentType(ContentType.Application.Json)
            body = json {
                "display_name" to displayName
            }
        }
    }

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
    suspend fun deleteDevice(deviceId: String, auth: AuthenticationData? = null) {
        return client.delete {
            url {
                path("_matrix", "client", "r0", "devices", deviceId)
            }

            header("Authorization", "Bearer $accessToken")

            contentType(ContentType.Application.Json)
            body = json {
                "auth" to auth
            }
        }
    }

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
    suspend fun deleteDevices(devices: List<String>, auth: AuthenticationData? = null) {
        return client.post("_matrix/client/r0/delete_devices") {
            header("Authorization", "Bearer $accessToken")
            contentType(ContentType.Application.Json)
            body = json {
                "devices" to jsonArray {
                    for (device in devices) {
                        +device
                    }
                }

                if (auth != null) "auth" to auth
            }
        }
    }

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
    suspend fun sendToDevice(eventType: String, txnId: String, messages: Map<String, Map<String, Any>>? = null) {
        return client.put {
            url {
                path("_matrix", "client", "r0", "sendToDevice", eventType, txnId)
            }

            header("Authorization", "Bearer $accessToken")

            contentType(ContentType.Application.Json)
            body = SendToDeviceRequest(messages)
        }
    }
}
