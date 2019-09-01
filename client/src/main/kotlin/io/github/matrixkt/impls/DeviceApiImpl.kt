package io.github.matrixkt.impls

import io.github.matrixkt.apis.DeviceApi
import io.github.matrixkt.models.*
import io.ktor.client.HttpClient
import io.ktor.client.call.receive
import io.ktor.client.request.*
import io.ktor.client.response.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.io.core.use
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.json
import kotlin.reflect.KProperty0

internal class DeviceApiImpl(private val client: HttpClient, private val accessTokenProp: KProperty0<String>) : DeviceApi {
    private inline val accessToken: String get() = accessTokenProp.get()

    override suspend fun getDevices(): List<Device> {
        val response = client.get<HttpResponse>("_matrix/client/r0/devices") {
            header("Authorization", "Bearer $accessToken")
        }

        response.use {
            when (it.status) {
                HttpStatusCode.OK -> return it.receive<GetDevicesResponse>().devices
                else -> throw it.receive<MatrixError>()
            }
        }
    }

    override suspend fun getDevice(deviceId: String): Device {
        val response = client.get<HttpResponse> {
            url {
                path("_matrix", "client", "r0", "devices", deviceId)
            }

            header("Authorization", "Bearer $accessToken")
        }

        response.use {
            when (it.status) {
                HttpStatusCode.OK -> return it.receive()
                else -> throw it.receive<MatrixError>()
            }
        }
    }

    override suspend fun updateDevice(deviceId: String, displayName: String) {
        val response = client.put<HttpResponse> {
            url {
                path("_matrix", "client", "r0", "devices", deviceId)
            }

            header("Authorization", "Bearer $accessToken")

            contentType(ContentType.Application.Json)
            body = json {
                "display_name" to displayName
            }
        }

        response.use {
            when (it.status) {
                HttpStatusCode.OK -> return it.receive()
                else -> throw it.receive<MatrixError>()
            }
        }
    }

    override suspend fun deleteDevice(deviceId: String, auth: AuthenticationData?) {
        val response = client.delete<HttpResponse> {
            url {
                path("_matrix", "client", "r0", "devices", deviceId)
            }

            header("Authorization", "Bearer $accessToken")

            contentType(ContentType.Application.Json)
            body = json {
                "auth" to auth
            }
        }

        response.use {
            when (it.status) {
                HttpStatusCode.OK -> return it.receive()
                else -> throw it.receive<MatrixError>()
            }
        }
    }

    override suspend fun deleteDevices(devices: List<String>, auth: AuthenticationData?) {
        val response = client.post<HttpResponse>("_matrix/client/r0/delete_devices") {
            header("Authorization", "Bearer $accessToken")
            contentType(ContentType.Application.Json)
            body = json {
                "devices" to JsonArray(devices.map { JsonPrimitive(it) })
                if (auth != null) "auth" to auth
            }
        }

        response.use {
            when (it.status) {
                HttpStatusCode.OK -> return it.receive()
                else -> throw it.receive<MatrixError>()
            }
        }
    }

    override suspend fun sendToDevice(eventType: String, txnId: String, messages: Map<String, Map<String, Any>>?) {
        val response = client.put<HttpResponse> {
            url {
                path("_matrix", "client", "r0", "sendToDevice", eventType, txnId)
            }

            header("Authorization", "Bearer $accessToken")

            contentType(ContentType.Application.Json)
            body = SendToDeviceRequest(messages)
        }

        response.use {
            when (it.status) {
                HttpStatusCode.OK -> return it.receive()
                else -> throw it.receive<MatrixError>()
            }
        }
    }
}
