@file:OptIn(ExperimentalCoroutinesApi::class)

package io.github.matrixkt

import io.github.matrixkt.api.*
import io.github.matrixkt.models.*
import io.github.matrixkt.utils.MatrixConfig
import io.github.matrixkt.utils.rpc
import io.ktor.client.*
import io.ktor.client.engine.mock.MockEngine
import io.ktor.http.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import utils.respond
import utils.respondJson
import kotlin.test.*

class ClientTests {
    private val baseUrl = Url("https://matrix-client.popular.org/more/stuff/here/").toString()

    private inline fun <reified T : MatrixError> assertFailsWith(message: String? = null, block: () -> Unit): T {
        val e = assertFailsWith<MatrixException>(message, block).error
        assertTrue(e is T)
        return e
    }

    @Test
    fun testCreateRoom() = runTest {
        val client = HttpClient(MockEngine) {
            engine {
                addHandler {
                    assertEquals("${baseUrl}_matrix/client/r0/createRoom", it.url.toString())

                    respond(CreateRoom.Response("!sefiuhWgwghwWgh:example.com"))
                }
            }

            MatrixConfig(baseUrl)
        }

        val request = CreateRoom(
            url = CreateRoom.Url(),
            body = CreateRoom.Body(
                preset = RoomPreset.PRIVATE_CHAT,
                roomAliasName = "matrixkt_demo",
                name = "MatrixKt Demo",
                topic = "Test room for MatrixKt client",
                roomVersion = "7"
            )
        )

        run {
            val response = client.rpc(request, "THIS IS A SUPER SECURE TOKEN")
            assertEquals("!sefiuhWgwghwWgh:example.com", response.roomId)
        }
    }

    @Test
    fun testJoinRoom() = runTest {
        val engine = MockEngine {
            // language=json
            respondJson("""
                {
                  "errcode": "M_LIMIT_EXCEEDED",
                  "error": "Too many requests",
                  "retry_after_ms": 2000
                }
            """, HttpStatusCode.TooManyRequests)
        }
        val client = HttpClient(engine) {
            MatrixConfig(baseUrl)
        }

        val request = JoinRoom(
            JoinRoom.Url(
                roomIdOrAlias = "!OGEhHVWSdvArJzumhm:matrix.org",
                serverName = listOf("alice.org", "bob.org")
            ),
            JoinRoom.Body(
                thirdPartySigned = ThirdPartySigned(
                    sender = "@alice:example.org",
                    mixid = "@bob:example.org",
                    token = "random8nonce",
                    signatures = mapOf(
                        "example.org" to mapOf(
                            "ed25519:0" to "some9signature"
                        )
                    )
                )
            )
        )

        val error = assertFailsWith<MatrixError.LimitExceeded> {
            client.rpc(request, "THIS IS A SUPER SECURE TOKEN")
        }
        assertEquals("Too many requests", error.error)
        assertEquals(2000, error.retryAfterMillis)

        val requestData = engine.requestHistory.single()
        assertEquals(
            "https://matrix-client.popular.org/more/stuff/here/_matrix/client/r0/join/!OGEhHVWSdvArJzumhm:matrix.org?server_name=alice.org&server_name=bob.org",
            requestData.url.toString()
        )
    }

    @Test
    fun testGetThumbnail() = runTest {
        val mockEngine = MockEngine.create {
            addHandler {
                // language=json
                respondJson("""
                    {
                      "errcode": "M_UNKNOWN",
                      "error": "Cannot generate thumbnails for the requested content"
                    }
                """, HttpStatusCode.BadRequest)
            }
            addHandler {
                // language=json
                respondJson("""
                    {
                      "errcode": "M_TOO_LARGE",
                      "error": "Content is too large to thumbnail"
                    }
                """, HttpStatusCode.PayloadTooLarge)
            }
            addHandler {
                // language=json
                respondJson("""
                    {
                      "errcode": "M_LIMIT_EXCEEDED",
                      "error": "Too many requests",
                      "retry_after_ms": 2000
                    }
                """, HttpStatusCode.TooManyRequests)
            }
            addHandler {
                // language=json
                respondJson("""
                    {
                      "errcode": "M_TOO_LARGE",
                      "error": "Content is too large to thumbnail"
                    }
                """, HttpStatusCode.BadGateway)
            }
        }

        val client = HttpClient(mockEngine) {
            MatrixConfig(baseUrl)
        }

        val request = GetContentThumbnail(
            GetContentThumbnail.Url("example.org", "ascERGshawAWawugaAcauga",
                width = 64, height = 64, method = ThumbnailMethod.SCALE, allowRemote = false)
        )

        run {
            val error = assertFailsWith<MatrixError.Unknown> {
                client.rpc(request)
            }
            assertEquals("Cannot generate thumbnails for the requested content", error.error)
        }
        run {
            val error = assertFailsWith<MatrixError.TooLarge> {
                client.rpc(request)
            }
            assertEquals("Content is too large to thumbnail", error.error)
        }
        run {
            val error = assertFailsWith<MatrixError.LimitExceeded> {
                client.rpc(request)
            }
            assertEquals("Too many requests", error.error)
            assertEquals(2000, error.retryAfterMillis)
        }
        run {
            val error = assertFailsWith<MatrixError.TooLarge> {
                client.rpc(request)
            }
            assertEquals("Content is too large to thumbnail", error.error)
        }

        for (requestData in (mockEngine as MockEngine).requestHistory) {
            assertEquals(
                "https://matrix-client.popular.org/more/stuff/here/_matrix/media/r0/thumbnail/example.org/ascERGshawAWawugaAcauga?width=64&height=64&method=scale&allow_remote=false",
                requestData.url.toString())
            assertEquals("64", requestData.url.parameters["width"])
            assertEquals("64", requestData.url.parameters["height"])
            assertEquals("scale", requestData.url.parameters["method"])
            assertEquals("false", requestData.url.parameters["allow_remote"])
        }
    }

    @Test
    fun testResolveRoom() = runTest {
        val mockEngine = MockEngine.create {
            addHandler {
                respond(GetRoomIdByAlias.Response(
                    roomId = "!abnjk1jdasj98:capuchins.com",
                    servers = listOf("capuchins.com", "matrix.org", "another.com")
                ))
            }
            addHandler {
                // language=json
                respondJson("""
                    {
                      "errcode": "M_NOT_FOUND",
                      "error": "Room alias #monkeys:matrix.org not found."
                    }
                """, HttpStatusCode.NotFound)
            }
        }

        val client = HttpClient(mockEngine) {
            MatrixConfig(baseUrl)
        }

        val request = GetRoomIdByAlias(
            GetRoomIdByAlias.Url(roomAlias = "#lolol:matrix.org")
        )

        run {
            val response = client.rpc(request)
            assertEquals("!abnjk1jdasj98:capuchins.com", response.roomId)
            assertEquals(listOf("capuchins.com", "matrix.org", "another.com"), response.servers)
        }

        run {
            val error = assertFailsWith<MatrixError.NotFound> {
                client.rpc(request)
            }
            assertEquals("Room alias #monkeys:matrix.org not found.", error.error)
        }

        for (requestData in (mockEngine as MockEngine).requestHistory) {
            assertEquals(
                "https://matrix-client.popular.org/more/stuff/here/_matrix/client/r0/directory/room/%23lolol:matrix.org",
                requestData.url.toString()
            )
        }
    }

    @Test
    fun testRegister() = runTest {
        val client = HttpClient(MockEngine) {
            engine {
                addHandler {
                    respond(Register.Response(
                        userId = "@cheeky_monkey:matrix.org",
                        accessToken = "abc123",
                        deviceId = "GHTYAJCE"
                    ))
                }
                addHandler {
                    // language=json
                    respondJson("""
                        {
                          "errcode": "M_USER_IN_USE",
                          "error": "Desired user ID is already taken."
                        }
                    """, HttpStatusCode.BadRequest)
                }
                addHandler {
                    // language=json
                    respondJson("""
                        {
                          "flows": [
                            {
                              "stages": [
                                "example.type.foo"
                              ]
                            }
                          ],
                          "params": {
                            "example.type.baz": {
                              "example_key": "foobar"
                            }
                          },
                          "session": "xxxxxxyz",
                          "completed": [
                            "example.type.foo"
                          ]
                        }
                    """, HttpStatusCode.Unauthorized)
                }
                addHandler {
                    // language=json
                    respondJson("""
                        {
                          "errcode": "M_FORBIDDEN",
                          "error": "Registration is disabled"
                        }
                    """, HttpStatusCode.Forbidden)
                }
                addHandler {
                    // language=json
                    respondJson("""
                        {
                          "errcode": "M_LIMIT_EXCEEDED",
                          "error": "Too many requests",
                          "retry_after_ms": 2000
                        }
                    """, HttpStatusCode.TooManyRequests)
                }
            }

            MatrixConfig(baseUrl)
        }

        val request = Register(
            Register.Url(Register.Kind.USER),
            Register.Body(
                auth = AuthenticationData.Dummy(
                    session = "xxxxx"
                    // exampleCredential = "verypoorsharedsecret"
                ),
                username = "cheeky_monkey",
                password = "ilovebananas",
                deviceId = "GHTYAJCE",
                initialDeviceDisplayName = "Jungle Phone",
                inhibitLogin = false
            )
        )

        run {
            val response = client.rpc(request)
            assertEquals("@cheeky_monkey:matrix.org", response.userId)
            assertEquals("abc123", response.accessToken)
            assertEquals("GHTYAJCE", response.deviceId)
        }

        run {
            val error = assertFailsWith<MatrixError.UserInUse> {
                client.rpc(request)
            }
            assertEquals("Desired user ID is already taken.", error.error)
        }

        // TODO: This case hasn't been handled yet.
        run {
            assertFails {
                client.rpc(request)
            }
        }

        run {
            val error = assertFailsWith<MatrixError.Forbidden> {
                client.rpc(request)
            }
            assertEquals("Registration is disabled", error.error)
        }

        run {
            val error = assertFailsWith<MatrixError.LimitExceeded> {
                client.rpc(request)
            }
            assertEquals("Too many requests", error.error)
        }
    }
}
