package io.github.matrixkt.impls

import io.github.matrixkt.apis.DeviceApi
import io.github.matrixkt.models.*
import io.ktor.client.HttpClient
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.json.*
import kotlin.reflect.KProperty0

internal class DeviceApiImpl(private val client: HttpClient, private val accessTokenProp: KProperty0<String>) : DeviceApi {
    private inline val accessToken: String get() = accessTokenProp.get()

    override suspend fun getDevices(): List<Device> {
        val response = client.get<GetDevicesResponse>("_matrix/client/r0/devices") {
            header("Authorization", "Bearer $accessToken")
        }
        return response.devices
    }

    override suspend fun getDevice(deviceId: String): Device {
        return client.get {
            url {
                path("_matrix", "client", "r0", "devices", deviceId)
            }

            header("Authorization", "Bearer $accessToken")
        }
    }

    override suspend fun updateDevice(deviceId: String, displayName: String) {
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

    override suspend fun deleteDevice(deviceId: String, auth: AuthenticationData?) {
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

    override suspend fun deleteDevices(devices: List<String>, auth: AuthenticationData?) {
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

    override suspend fun sendToDevice(eventType: String, txnId: String, messages: Map<String, Map<String, Any>>?) {
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
