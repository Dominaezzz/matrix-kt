package io.github.matrixkt

import io.github.matrixkt.models.*
import io.github.matrixkt.models.events.UnsignedData
import io.github.matrixkt.models.events.contents.room.Membership
import io.github.matrixkt.models.events.contents.TagContent
import io.github.matrixkt.models.events.contents.room.MemberContent
import io.github.matrixkt.models.events.contents.room.MessageContent
import io.github.matrixkt.models.filter.*
import io.github.matrixkt.models.push.PushRuleAction
import io.github.matrixkt.models.push.PushRuleKind
import io.github.matrixkt.models.search.*
import io.github.matrixkt.utils.MatrixJson
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.toByteArray
import io.ktor.http.*
import io.ktor.util.KtorExperimentalAPI
import kotlinx.serialization.json.*
import testutils.runSuspendTest
import utils.respondJson
import kotlin.test.*

class ClientTests {
    private val baseUrl = Url("https://matrix-client.popular.org/more/stuff/here/")

    @Test
    fun testCreateRoom() = runSuspendTest {
        val mockEngine = MockEngine {
            assertEquals("${baseUrl}_matrix/client/r0/createRoom", it.url.toString())

            // language=json
            respondJson("""
                {
                  "room_id": "!sefiuhWgwghwWgh:example.com"
                }
                """
            )
        }

        val client = MatrixClient(mockEngine, baseUrl)

        val roomApi = client.roomApi

        val request = CreateRoomRequest(
            preset = RoomPreset.PRIVATE_CHAT,
            roomAliasName = "matrixkt_demo",
            name = "MatrixKt Demo",
            topic = "Test room for MatrixKt client",
            roomVersion = "THIS DOESN'T BREAK?!?!"
        )

        run {
            val response = roomApi.createRoom(request)
            assertEquals("!sefiuhWgwghwWgh:example.com", response)
        }
    }

    @Test
    fun testResolveRoom() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "room_id": "!abnjk1jdasj98:capuchins.com",
                      "servers": ["capuchins.com", "matrix.org", "another.com"]
                    }
                """)
            }
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "errcode": "M_NOT_FOUND",
                      "error": "Room alias #monkeys:matrix.org not found."
                    }
                """, HttpStatusCode.NotFound)
            }
        }

        val client = MatrixClient(mockEngine, baseUrl)
        val roomApi = client.roomApi

        run {
            val response = roomApi.getRoomIdByAlias("#lolol:matrix.org")
            assertEquals("!abnjk1jdasj98:capuchins.com", response.roomId)
            assertEquals(listOf("capuchins.com", "matrix.org", "another.com"), response.servers)
        }

        val error = assertFailsWith<MatrixError.NotFound> {
            val response = roomApi.getRoomIdByAlias("#matrixkt_demo_fail:matrix.org")
            println(response)
        }
        assertEquals("Room alias #monkeys:matrix.org not found.", error.error)
    }

    @Test
    fun testJoinedRooms() = runSuspendTest {
        val mockEngine = MockEngine {
        	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
            // language=json
            respondJson("""
                {
                  "joined_rooms": [
                    "!foo:example.com"
                  ]
                }
                """
            )
        }
        val client = MatrixClient(mockEngine, baseUrl)

        val result = client.roomApi.getJoinedRooms()
        assertEquals(listOf("!foo:example.com"), result)
    }

    @Test
    fun testInvite() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {}
                """)
            }
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "errcode": "M_UNKNOWN",
                      "error": "An unknown error occurred"
                    }
                """, HttpStatusCode.NotFound)
            }
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "errcode": "M_FORBIDDEN",
                      "error": "@cheeky_monkey:matrix.org is banned from the room"
                    }
                """, HttpStatusCode.Forbidden)
            }
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
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

        val client = MatrixClient(mockEngine, baseUrl)
        val roomApi = client.roomApi

        roomApi.inviteUser("!LALALA:matrix.org", "@me:matrix.org")

        run {
            val error = assertFailsWith<MatrixError.Unknown> {
                roomApi.inviteUser("!LALALA:matrix.org", "@me:matrix.org")
            }
            assertEquals("An unknown error occurred", error.error)
        }

        run {
            val error = assertFailsWith<MatrixError.Forbidden> {
                roomApi.inviteUser("!LALALA:matrix.org", "@me:matrix.org")
            }
            assertEquals("@cheeky_monkey:matrix.org is banned from the room", error.error)
        }

        run {
            val error = assertFailsWith<MatrixError.LimitExceeded> {
                roomApi.inviteUser("!LALALA:matrix.org", "@me:matrix.org")
            }
            assertEquals("Too many requests", error.error)
            assertEquals(2000, error.retryAfterMillis)
        }
    }

    @Test
    fun testVisibility() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "visibility": "public"
                    }
                """)
            }
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "visibility": "private"
                    }
                """)
            }
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "errcode": "M_NOT_FOUND",
                      "error": "Room not found"
                    }
                """, HttpStatusCode.NotFound)
            }
        }
        val client = MatrixClient(mockEngine, baseUrl)
        val roomApi = client.roomApi

        run {
            val response = roomApi.getVisibility("!GoodRoom:matrix.org")
            assertEquals(RoomVisibility.PUBLIC, response)
        }

        run {
            val response = roomApi.getVisibility("!GoodRoom:matrix.org")
            assertEquals(RoomVisibility.PRIVATE, response)
        }

        run {
            val error = assertFailsWith<MatrixError.NotFound> {
                roomApi.getVisibility("!GoodRoom:matrix.org")
            }
            assertEquals("Room not found", error.error)
        }
    }

    @Test
    fun testGetPublicRooms() = runSuspendTest {
        val mockEngine = MockEngine {
        	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
            // language=json
            respondJson("""
                {
                  "chunk": [
                    {
                      "aliases": [
                        "#murrays:cheese.bar"
                      ],
                      "avatar_url": "mxc://bleeker.street/CHEDDARandBRIE",
                      "guest_can_join": false,
                      "name": "CHEESE",
                      "num_joined_members": 37,
                      "room_id": "!ol19s:bleecker.street",
                      "topic": "Tasty tasty cheese",
                      "world_readable": true
                    }
                  ],
                  "next_batch": "p190q",
                  "prev_batch": "p1902",
                  "total_room_count_estimate": 115
                }
                """
            )
        }
        val client = MatrixClient(mockEngine, baseUrl)
        val roomApi = client.roomApi

        val response = roomApi.getPublicRooms(limit = 15)
        assertEquals("p190q", response.nextBatch)
        assertEquals("p1902", response.prevBatch)
        assertEquals(115, response.totalRoomCountEstimate)
        assertEquals(1, response.chunk.size)
        assertEquals("!ol19s:bleecker.street", response.chunk.single().roomId)
    }

    @Test
    fun testGetEvent() = runSuspendTest {
        val mockEngine = MockEngine {
        	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
            // language=json
            respondJson("""
                {
                  "content": {
                    "body": "This is an example text message",
                    "msgtype": "m.text",
                    "format": "org.matrix.custom.html",
                    "formatted_body": "<b>This is an example text message</b>"
                  },
                  "type": "m.room.message",
                  "event_id": "${'$'}143273582443PhrSn:example.org",
                  "room_id": "!636q39766251:matrix.org",
                  "sender": "@example:example.org",
                  "origin_server_ts": 1432735824653,
                  "unsigned": {
                    "age": 1234
                  }
                }
                """)
        }

        val client = MatrixClient(mockEngine, baseUrl)

        val event = client.roomApi.getOneRoomEvent(
            "!636q39766251:matrix.org",
            "\$143273582443PhrSn:example.org"
        )

        assertEquals("\$143273582443PhrSn:example.org", event.eventId)
        assertEquals("!636q39766251:matrix.org", event.roomId)
        assertEquals("m.room.message", event.type)

        val unsigned = MatrixJson.decodeFromJsonElement(UnsignedData.serializer(), event.unsigned!!)

        assertEquals(1234, unsigned.age)

        // TODO: Make this less verbose.
        val content = MatrixJson.decodeFromJsonElement(MessageContent.serializer(), event.content)

        assertEquals("This is an example text message", content.body)
        assertTrue(content is MessageContent.Text)
        assertEquals("org.matrix.custom.html", content.format)
        assertEquals("<b>This is an example text message</b>", content.formattedBody)
    }

    @Test
    fun testGetStateEvents() = runSuspendTest {
        val mockEngine = MockEngine {
        	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
            // language=json
            respondJson("""
                [
                  {
                    "content": {
                      "join_rule": "public"
                    },
                    "type": "m.room.join_rules",
                    "event_id": "${'$'}143273582443PhrSn:example.org",
                    "room_id": "!636q39766251:example.com",
                    "sender": "@example:example.org",
                    "origin_server_ts": 1432735824653,
                    "unsigned": {
                      "age": 1234
                    },
                    "state_key": ""
                  },
                  {
                    "content": {
                      "membership": "join",
                      "avatar_url": "mxc://example.org/SEsfnsuifSDFSSEF",
                      "displayname": "Alice Margatroid"
                    },
                    "type": "m.room.member",
                    "event_id": "${'$'}143273582443PhrSn:example.org",
                    "room_id": "!636q39766251:example.com",
                    "sender": "@example:example.org",
                    "origin_server_ts": 1432735824653,
                    "unsigned": {
                      "age": 1234
                    },
                    "state_key": "@alice:example.org"
                  },
                  {
                    "content": {
                      "creator": "@example:example.org",
                      "room_version": "1",
                      "m.federate": true,
                      "predecessor": {
                        "event_id": "${'$'}something:example.org",
                        "room_id": "!oldroom:example.org"
                      }
                    },
                    "type": "m.room.create",
                    "event_id": "${'$'}143273582443PhrSn:example.org",
                    "room_id": "!636q39766251:example.com",
                    "sender": "@example:example.org",
                    "origin_server_ts": 1432735824653,
                    "unsigned": {
                      "age": 1234
                    },
                    "state_key": ""
                  },
                  {
                    "content": {
                      "ban": 50,
                      "events": {
                        "m.room.name": 100,
                        "m.room.power_levels": 100
                      },
                      "events_default": 0,
                      "invite": 50,
                      "kick": 50,
                      "redact": 50,
                      "state_default": 50,
                      "users": {
                        "@example:localhost": 100
                      },
                      "users_default": 0,
                      "notifications": {
                        "room": 20
                      }
                    },
                    "type": "m.room.power_levels",
                    "event_id": "${'$'}143273582443PhrSn:example.org",
                    "room_id": "!636q39766251:example.com",
                    "sender": "@example:example.org",
                    "origin_server_ts": 1432735824653,
                    "unsigned": {
                      "age": 1234
                    },
                    "state_key": ""
                  }
                ]
                """)
        }

        val client = MatrixClient(mockEngine, baseUrl)

        val events = client.roomApi.getRoomState("!QNblkMeHRKNQGSYGQO:matrix.org")

        assertEquals(4, events.size)
        assertEquals("m.room.join_rules", events[0].type)
        assertEquals("m.room.member", events[1].type)
        assertEquals("m.room.create", events[2].type)
        assertEquals("m.room.power_levels", events[3].type)
    }

    @Test
    fun testGetMembers() = runSuspendTest {
        val mockEngine = MockEngine {
        	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
            // language=json
            respondJson("""
                {
                  "chunk": [
                    {
                      "content": {
                        "membership": "join",
                        "avatar_url": "mxc://example.org/SEsfnsuifSDFSSEF",
                        "displayname": "Alice Margatroid"
                      },
                      "type": "m.room.member",
                      "event_id": "${'$'}143273582443PhrSn:example.org",
                      "room_id": "!636q39766251:example.com",
                      "sender": "@example:example.org",
                      "origin_server_ts": 1432735824653,
                      "unsigned": {
                        "age": 1234
                      },
                      "state_key": "@alice:example.org"
                    }
                  ]
                }
                """)
        }

        val client = MatrixClient(mockEngine, baseUrl)

        val response = client.roomApi.getMembersByRoom("!QNblkMeHRKNQGSYGQO:matrix.org")

        assertEquals(1, response.chunk.size)
        assertEquals("m.room.member", response.chunk.single().type)
        assertEquals("@alice:example.org", response.chunk.single().stateKey)
    }

    @Test
    fun testGetVersions() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "versions": [
                        "r0.0.1"
                      ],
                      "unstable_features": {
                        "org.example.my_feature": true
                      }
                    }
                """)
            }
        }

        val client = MatrixClient(mockEngine, baseUrl)

        run {
            val response = client.miscApi.getVersions()
            assertEquals(1, response.versions.size)
            assertEquals("r0.0.1", response.versions.single())
            assertEquals(1, response.unstableFeatures.size)
            assertTrue(response.unstableFeatures.getValue("org.example.my_feature"))
        }
    }

    @Test
    fun testWellKnown() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "m.homeserver": {
                        "base_url": "https://matrix.example.com"
                      },
                      "m.identity_server": {
                        "base_url": "https://identity.example.com"
                      },
                      "org.example.custom.property": {
                        "app_url": "https://custom.app.example.org"
                      }
                    }
                """)
            }
        }

        val client = MatrixClient(mockEngine, baseUrl)

        run {
            val response = client.miscApi.getWellKnown()
            assertEquals("https://matrix.example.com", response.homeServer.baseUrl)
            assertEquals("https://identity.example.com", response.identityServer!!.baseUrl)
        }
    }

    @Test
    fun testRegister() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "user_id": "@cheeky_monkey:matrix.org",
                      "access_token": "abc123",
                      "device_id": "GHTYAJCE"
                    }
                """)
            }
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "errcode": "M_USER_IN_USE",
                      "error": "Desired user ID is already taken."
                    }
                """, HttpStatusCode.BadRequest)
            }
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
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
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "errcode": "M_FORBIDDEN",
                      "error": "Registration is disabled"
                    }
                """, HttpStatusCode.Forbidden)
            }
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
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

        val client = MatrixClient(mockEngine, baseUrl)
        val request = RegisterRequest(
            auth = AuthenticationData.Dummy(
                session = "xxxxx"
                // exampleCredential = "verypoorsharedsecret"
            ),
            bindEmail = false,
            bindMsisdn = false,
            username = "cheeky_monkey",
            password = "ilovebananas",
            deviceId = "GHTYAJCE",
            initialDeviceDisplayName = "Jungle Phone",
            inhibitLogin = false
        )

        run {
            val response = client.authApi.register(RegistrationKind.USER, request)
            assertEquals("@cheeky_monkey:matrix.org", response.userId)
            assertEquals("abc123", response.accessToken)
            assertEquals("GHTYAJCE", response.deviceId)
        }

        run {
            val error = assertFailsWith<MatrixError.UserInUse> {
                client.authApi.register(RegistrationKind.USER, request)
            }
            assertEquals("Desired user ID is already taken.", error.error)
        }

        // TODO: This case hasn't been handled yet.
        run {
            assertFails {
                client.authApi.register(RegistrationKind.USER, request)
            }
        }

        run {
            val error = assertFailsWith<MatrixError.Forbidden> {
                client.authApi.register(RegistrationKind.USER, request)
            }
            assertEquals("Registration is disabled", error.error)
        }

        run {
            val error = assertFailsWith<MatrixError.LimitExceeded> {
                client.authApi.register(RegistrationKind.USER, request)
            }
            assertEquals("Too many requests", error.error)
        }
    }

    @Test
    fun testGetLogin() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "flows": [
                        {
                          "type": "m.login.password"
                        }
                      ]
                    }
                """)
            }
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
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

        val client = MatrixClient(mockEngine, baseUrl)

        run {
            val response = client.authApi.getLoginFlows()
            assertEquals(1, response.flows.size)
            assertEquals("m.login.password", response.flows[0].type)
        }

        run {
            val error = assertFailsWith<MatrixError.LimitExceeded> {
                client.authApi.getLoginFlows()
            }
            assertEquals("Too many requests", error.error)
            assertEquals(2000, error.retryAfterMillis)
        }
    }

    @Test
    fun testPostLogin() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "user_id": "@cheeky_monkey:matrix.org",
                      "access_token": "abc123",
                      "device_id": "GHTYAJCE",
                      "well_known": {
                        "m.homeserver": {
                          "base_url": "https://example.org"
                        },
                        "m.identity_server": {
                          "base_url": "https://id.example.org"
                        }
                      }
                    }
                """)
            }
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "errcode": "M_UNKNOWN",
                      "error": "Bad login type."
                    }
                """, HttpStatusCode.BadRequest)
            }
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "errcode": "M_FORBIDDEN",
                      "error": "TODO: This is missing in the spec for some reason."
                    }
                """, HttpStatusCode.Forbidden)
            }
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
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

        val client = MatrixClient(mockEngine, baseUrl)
        val request = LoginRequest.Password(
            identifier = UserIdentifier.Matrix(
                "Dominaezzz"
            ),
            password = "ilovebananas",
            initialDeviceDisplayName = "Jungle Phone"
        )

        run {
            val response = client.authApi.login(request)

            assertEquals("@cheeky_monkey:matrix.org", response.userId)
            assertEquals("abc123", response.accessToken)
            assertEquals("GHTYAJCE", response.deviceId)
            assertEquals("https://example.org", response.wellKnown!!.homeServer.baseUrl)
            assertEquals("https://id.example.org", response.wellKnown!!.identityServer!!.baseUrl)
        }

        run {
            val error = assertFailsWith<MatrixError.Unknown> {
                client.authApi.login(request)
            }
            assertEquals("Bad login type.", error.error)
        }

        run {
            val error = assertFailsWith<MatrixError.Forbidden> {
                client.authApi.login(request)
            }
            assertNotEquals("", error.error)
        }

        run {
            val error = assertFailsWith<MatrixError.LimitExceeded> {
                client.authApi.login(request)
            }
            assertEquals("Too many requests", error.error)
            assertEquals(2000, error.retryAfterMillis)
        }
    }

    @Test
    fun testSync() = runSuspendTest {
        // Mock not working because of https://github.com/Kotlin/kotlinx-io/issues/38

        // val mockEngine = MockEngine {
        //     // language=json
        //     respondJson("""
        //         {
        //           "next_batch": "s72595_4483_1934",
        //           "presence": {
        //             "events": [
        //               {
        //                 "content": {
        //                   "avatar_url": "mxc://localhost:wefuiwegh8742w",
        //                   "last_active_ago": 2478593,
        //                   "presence": "online",
        //                   "currently_active": false,
        //                   "status_msg": "Making cupcakes"
        //                 },
        //                 "type": "m.presence",
        //                 "sender": "@example:localhost"
        //               }
        //             ]
        //           },
        //           "account_data": {
        //             "events": [
        //               {
        //                 "type": "org.example.custom.config",
        //                 "content": {
        //                   "custom_config_key": "custom_config_value"
        //                 }
        //               }
        //             ]
        //           },
        //           "rooms": {
        //             "join": {
        //               "!726s6s6q:example.com": {
        //                 "summary": {
        //                   "m.heroes": [
        //                     "@alice:example.com",
        //                     "@bob:example.com"
        //                   ],
        //                   "m.joined_member_count": 2,
        //                   "m.invited_member_count": 0
        //                 },
        //                 "state": {
        //                   "events": [
        //                     {
        //                       "content": {
        //                         "membership": "join",
        //                         "avatar_url": "mxc://example.org/SEsfnsuifSDFSSEF",
        //                         "displayname": "Alice Margatroid"
        //                       },
        //                       "type": "m.room.member",
        //                       "event_id": "${'$'}143273582443PhrSn:example.org",
        //                       "room_id": "!726s6s6q:example.com",
        //                       "sender": "@example:example.org",
        //                       "origin_server_ts": 1432735824653,
        //                       "unsigned": {
        //                         "age": 1234
        //                       },
        //                       "state_key": "@alice:example.org"
        //                     }
        //                   ]
        //                 },
        //                 "timeline": {
        //                   "events": [
        //                     {
        //                       "content": {
        //                         "membership": "join",
        //                         "avatar_url": "mxc://example.org/SEsfnsuifSDFSSEF",
        //                         "displayname": "Alice Margatroid"
        //                       },
        //                       "type": "m.room.member",
        //                       "event_id": "${'$'}143273582443PhrSn:example.org",
        //                       "room_id": "!726s6s6q:example.com",
        //                       "sender": "@example:example.org",
        //                       "origin_server_ts": 1432735824653,
        //                       "unsigned": {
        //                         "age": 1234
        //                       },
        //                       "state_key": "@alice:example.org"
        //                     },
        //                     {
        //                       "content": {
        //                         "body": "This is an example text message",
        //                         "msgtype": "m.text",
        //                         "format": "org.matrix.custom.html",
        //                         "formatted_body": "<b>This is an example text message</b>"
        //                       },
        //                       "type": "m.room.message",
        //                       "event_id": "${'$'}143273582443PhrSn:example.org",
        //                       "room_id": "!726s6s6q:example.com",
        //                       "sender": "@example:example.org",
        //                       "origin_server_ts": 1432735824653,
        //                       "unsigned": {
        //                         "age": 1234
        //                       }
        //                     }
        //                   ],
        //                   "limited": true,
        //                   "prev_batch": "t34-23535_0_0"
        //                 },
        //                 "ephemeral": {
        //                   "events": [
        //                     {
        //                       "content": {
        //                         "user_ids": [
        //                           "@alice:matrix.org",
        //                           "@bob:example.com"
        //                         ]
        //                       },
        //                       "type": "m.typing",
        //                       "room_id": "!jEsUZKDJdhlrceRyVU:example.org"
        //                     }
        //                   ]
        //                 },
        //                 "account_data": {
        //                   "events": [
        //                     {
        //                       "content": {
        //                         "tags": {
        //                           "u.work": {
        //                             "order": 0.9
        //                           }
        //                         }
        //                       },
        //                       "type": "m.tag"
        //                     },
        //                     {
        //                       "type": "org.example.custom.room.config",
        //                       "content": {
        //                         "custom_config_key": "custom_config_value"
        //                       }
        //                     }
        //                   ]
        //                 }
        //               }
        //             },
        //             "invite": {
        //               "!696r7674:example.com": {
        //                 "invite_state": {
        //                   "events": [
        //                     {
        //                       "sender": "@alice:example.com",
        //                       "type": "m.room.name",
        //                       "state_key": "",
        //                       "content": {
        //                         "name": "My Room Name"
        //                       }
        //                     },
        //                     {
        //                       "sender": "@alice:example.com",
        //                       "type": "m.room.member",
        //                       "state_key": "@bob:example.com",
        //                       "content": {
        //                         "membership": "invite"
        //                       }
        //                     }
        //                   ]
        //                 }
        //               }
        //             },
        //             "leave": {}
        //           }
        //         }
        //         """.trimIndent())
        // }

        val mockEngine = MockEngine {
        	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
            // language=json
            respondJson("""
                {
                  "next_batch": "s72595_4483_1934",
                  "rooms": {
                    "join": {
                      "!726s6s6q:example.com": {
                        "summary": {
                          "m.heroes": [
                            "@alice:example.com",
                            "@bob:example.com"
                          ],
                          "m.joined_member_count": 2,
                          "m.invited_member_count": 0
                        },
                        "state": {
                          "events": [
                            {
                              "content": {
                                "membership": "join",
                                "avatar_url": "mxc://example.org/SEsfnsuifSDFSSEF",
                                "displayname": "Alice Margatroid"
                              },
                              "type": "m.room.member",
                              "event_id": "${'$'}143273582443PhrSn:example.org",
                              "room_id": "!726s6s6q:example.com",
                              "sender": "@example:example.org",
                              "origin_server_ts": 1432735824653,
                              "unsigned": {
                                "age": 1234
                              },
                              "state_key": "@alice:example.org"
                            }
                          ]
                        },
                        "timeline": {
                          "events": [
                            {
                              "content": {
                                "membership": "join",
                                "avatar_url": "mxc://example.org/SEsfnsuifSDFSSEF",
                                "displayname": "Alice Margatroid"
                              },
                              "type": "m.room.member",
                              "event_id": "${'$'}143273582443PhrSn:example.org",
                              "room_id": "!726s6s6q:example.com",
                              "sender": "@example:example.org",
                              "origin_server_ts": 1432735824653,
                              "unsigned": {
                                "age": 1234
                              },
                              "state_key": "@alice:example.org"
                            }
                          ],
                          "limited": true,
                          "prev_batch": "t34-23535_0_0"
                        },
                        "ephemeral": {
                          "events": [
                            {
                              "content": {
                                "user_ids": [
                                  "@alice:matrix.org"
                                ]
                              },
                              "type": "m.typing",
                              "room_id": "!jEsUZKDJdhlrceRyVU:example.org"
                            }
                          ]
                        },
                        "account_data": {
                          "events": [
                            {
                              "content": {
                                "tags": {
                                  "u.work": {
                                    "order": 0.9
                                  }
                                }
                              },
                              "type": "m.tag"
                            }
                          ]
                        }
                      }
                    },
                    "invite": {
                      "!696r7674:example.com": {
                        "invite_state": {
                          "events": [
                            {
                              "sender": "@alice:example.com",
                              "type": "m.room.name",
                              "state_key": "",
                              "content": {
                                "name": "My Room Name"
                              }
                            }
                          ]
                        }
                      }
                    },
                    "leave": {}
                  }
                }
                """.trimIndent())
        }

        val client = MatrixClient(mockEngine, baseUrl)

        val response = client.eventApi.sync(
            "66696p746572",
            since = "s72594_4483_1934",
            fullState = false,
            setPresence = Presence.OFFLINE,
            timeout = 30000
        )

        assertEquals("s72595_4483_1934", response.nextBatch)
    }

    @Test
    fun testLogout() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {}
                """)
            }
        }

        val client = MatrixClient(mockEngine, baseUrl)

        run {
            client.authApi.logout()
        }
    }

    @Test
    fun testLogoutAll() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {}
                """)
            }
        }

        val client = MatrixClient(mockEngine, baseUrl)

        client.authApi.logoutAll()
    }

    @Test
    fun testRegisterEmailRequestToken() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "sid": "123abc",
                      "submit_url": "https://example.org/path/to/submitToken"
                    }
                """)
            }
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "errcode": "M_THREEPID_DENIED",
                      "error": "Third party identifier is not allowed"
                    }
                """, HttpStatusCode.Forbidden)
            }
        }

        val client = MatrixClient(mockEngine, baseUrl)
        val request = EmailValidationRequest(
            clientSecret = "monkeys_are_GREAT",
            email = "foo@example.com",
            sendAttempt = 1,
            idServer = "TODO: Not in spec example"
        )

        run {
            val response = client.authApi.requestTokenToRegisterEmail(request)
            assertEquals("123abc", response.sid)
            assertEquals("https://example.org/path/to/submitToken", response.submitUrl)
        }

        run {
            val error = assertFailsWith<MatrixError.ThreePIdDenied> {
                client.authApi.requestTokenToRegisterEmail(request)
            }
            assertEquals("Third party identifier is not allowed", error.error)
        }
    }

    @Test
    fun testRegisterMsisdnRequestToken() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "sid": "123abc",
                      "submit_url": "https://example.org/path/to/submitToken"
                    }
                """)
            }
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "errcode": "M_THREEPID_DENIED",
                      "error": "Third party identifier is not allowed"
                    }
                """, HttpStatusCode.Forbidden)
            }
        }

        val client = MatrixClient(mockEngine, baseUrl)

        val request = MSISDNValidationRequest(
            clientSecret = "monkeys_are_GREAT",
            country = "GB",
            phoneNumber = "07700900001",
            sendAttempt = 1,
            idServer = "TODO: Not in spec example!"
        )

        run {
            val response = client.authApi.requestTokenToRegisterMSISDN(request)
            assertEquals("123abc", response.sid)
            assertEquals("https://example.org/path/to/submitToken", response.submitUrl)
        }
        run {
            val error = assertFailsWith<MatrixError.ThreePIdDenied> {
                client.authApi.requestTokenToRegisterMSISDN(request)
            }
            assertEquals("Third party identifier is not allowed", error.error)
        }
    }

    @Test
    fun testChangePassword() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {}
                """)
            }
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
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
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
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

        val client = MatrixClient(mockEngine, baseUrl)
        val request = ChangePasswordRequest(
            newPassword = "ihatebananas",
            auth = AuthenticationData.Dummy(
                session = "xxxxx"
            )
        )

        run {
            client.accountApi.changePassword(request)
        }
        run {
            assertFails {
                client.accountApi.changePassword(request)
            }
        }
        run {
            val error = assertFailsWith<MatrixError.LimitExceeded> {
                client.accountApi.changePassword(request)
            }
            assertEquals("Too many requests", error.error)
            assertEquals(2000, error.retryAfterMillis)
        }
    }

    @Test
    fun testAccountPasswordEmailRequestToken() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "sid": "123abc",
                      "submit_url": "https://example.org/path/to/submitToken"
                    }
                """)
            }
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "errcode": "M_THREEPID_NOT_FOUND",
                      "error": "Email not found"
                    }
                """, HttpStatusCode.BadRequest)
            }
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "errcode": "M_THREEPID_DENIED",
                      "error": "Third party identifier is not allowed"
                    }
                """, HttpStatusCode.Forbidden)
            }
        }

        val client = MatrixClient(mockEngine, baseUrl)
        val request = EmailValidationRequest(
            clientSecret = "monkeys_are_GREAT",
            email = "foo@example.com",
            sendAttempt = 1,
            idServer = "TODO: Not in spec example!"
        )

        run {
            val response = client.accountApi.requestTokenToResetPasswordEmail(request)
            assertEquals("123abc", response.sid)
            assertEquals("https://example.org/path/to/submitToken", response.submitUrl)
        }
        run {
            val error = assertFailsWith<MatrixError.ThreePIdNotFound> {
                client.accountApi.requestTokenToResetPasswordEmail(request)
            }
            assertEquals("Email not found", error.error)
        }
        run {
            val error = assertFailsWith<MatrixError.ThreePIdDenied> {
                client.accountApi.requestTokenToResetPasswordEmail(request)
            }
            assertEquals("Third party identifier is not allowed", error.error)
        }
    }

    @Test
    fun testAccountPasswordMsisdnRequestToken() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "sid": "123abc",
                      "submit_url": "https://example.org/path/to/submitToken"
                    }
                """)
            }
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "errcode": "M_THREEPID_NOT_FOUND",
                      "error": "Phone number not found"
                    }
                """, HttpStatusCode.BadRequest)
            }
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "errcode": "M_THREEPID_DENIED",
                      "error": "Third party identifier is not allowed"
                    }
                """, HttpStatusCode.Forbidden)
            }
        }

        val client = MatrixClient(mockEngine, baseUrl)
        val request = MSISDNValidationRequest(
            clientSecret = "monkeys_are_GREAT",
            country = "GB",
            phoneNumber = "07700900001",
            sendAttempt = 1,
            idServer = "TODO: Not in spec example!"
        )

        run {
            val response = client.accountApi.requestTokenToResetPasswordMSISDN(request)
            assertEquals("123abc", response.sid)
            assertEquals("https://example.org/path/to/submitToken", response.submitUrl)
        }
        run {
            val error = assertFailsWith<MatrixError.ThreePIdNotFound> {
                client.accountApi.requestTokenToResetPasswordMSISDN(request)
            }
            assertEquals("Phone number not found", error.error)
        }
        run {
            val error = assertFailsWith<MatrixError.ThreePIdDenied> {
                client.accountApi.requestTokenToResetPasswordMSISDN(request)
            }
            assertEquals("Third party identifier is not allowed", error.error)
        }
    }

    @Test
    fun testAccountDeactivate() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "id_server_unbind_result": "success"
                    }
                """)
            }
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
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
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
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

        val client = MatrixClient(mockEngine, baseUrl)
        val request = DeactivateRequest(
            auth = AuthenticationData.Dummy(
                session = "xxxxx"
                // example_credential = "verypoorsharedsecret"
            ),
            idServer = "example.org"
        )

        run {
            val response = client.accountApi.deactivateAccount(request)
            assertEquals("success", response.idServerUnbindResult)
        }
        run {
            assertFails {
                client.accountApi.deactivateAccount(request)
            }
        }
        run {
            val error = assertFailsWith<MatrixError.LimitExceeded> {
                client.accountApi.deactivateAccount(request)
            }
            assertEquals("Too many requests", error.error)
            assertEquals(2000, error.retryAfterMillis)
        }
    }

    @Test
    fun testRegisterAvailable() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "available": true
                    }
                """)
            }
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
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

        val client = MatrixClient(mockEngine, baseUrl)

        run {
            val response = client.authApi.checkUsernameAvailability("my_cool_localpart")
            assertTrue(response)
        }
        run {
            val error = assertFailsWith<MatrixError.LimitExceeded> {
                client.authApi.checkUsernameAvailability("my_cool_localpart")
            }
            assertEquals("Too many requests", error.error)
            assertEquals(2000, error.retryAfterMillis)
        }
    }

    @Test
    fun testAccount3pid() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "threepids": [
                        {
                          "medium": "email",
                          "address": "monkey@banana.island",
                          "validated_at": 1535176800000,
                          "added_at": 1535336848756
                        }
                      ]
                    }
                """)
            }
        }

        val client = MatrixClient(mockEngine, baseUrl)

        run {
            val response = client.accountApi.getAccount3PIDs()
            assertEquals(1, response.size)
            assertEquals(Medium.EMAIL, response[0].medium)
            assertEquals("monkey@banana.island", response[0].address)
            assertEquals(1535176800000, response[0].validatedAt)
            assertEquals(1535336848756, response[0].addedAt)
        }
    }

    @Test
    fun testPostAccount3Pid() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "submit_url": "https://example.org/path/to/submitToken"
                    }
                """)
            }
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "errcode": "M_THREEPID_AUTH_FAILED",
                      "error": "The third party credentials could not be verified by the identity server."
                    }
                """, HttpStatusCode.Forbidden)
            }
        }

        val client = MatrixClient(mockEngine, baseUrl)
        val request = Add3PidRequest(
            threePidCredentials = ThreePidCredentials(
                idServer = "matrix.org",
                sid = "abc123987",
                clientSecret = "d0n'tT3ll"
            ),
            bind = false
        )

        run {
            @Suppress("UNUSED_VARIABLE")
            val response = client.accountApi.add3PID(request)
            // TODO: assertEquals("https://example.org/path/to/submitToken", response)
        }
        run {
            val error = assertFailsWith<MatrixError.ThreePIdAuthFailed> {
                client.accountApi.add3PID(request)
            }
            assertEquals("The third party credentials could not be verified by the identity server.", error.error)
        }
    }

    @Test
    fun testAccount3PidDelete() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "id_server_unbind_result": "success"
                    }
                """)
            }
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "id_server_unbind_result": "no-support"
                    }
                """)
            }
        }

        val client = MatrixClient(mockEngine, baseUrl)

        val request = Remove3PidRequest(
            idServer = "example.org",
            medium = Medium.EMAIL,
            address = "example@example.org"
        )

        run {
            val response = client.accountApi.delete3pidFromAccount(request)
            assertEquals(IdServerUnbindResult.SUCCESS, response.idServerUnbindResult)
        }

        run {
            val response = client.accountApi.delete3pidFromAccount(request)
            assertEquals(IdServerUnbindResult.NO_SUPPORT, response.idServerUnbindResult)
        }
    }

    @Test
    fun testAccount3PIdEmailRequestToken() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "sid": "123abc",
                      "submit_url": "https://example.org/path/to/submitToken"
                    }
                """)
            }
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "errcode": "M_THREEPID_IN_USE",
                      "error": "Third party identifier already in use"
                    }
                """, HttpStatusCode.BadRequest)
            }
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "errcode": "M_THREEPID_DENIED",
                      "error": "Third party identifier is not allowed"
                    }
                """, HttpStatusCode.Forbidden)
            }
        }

        val client = MatrixClient(mockEngine, baseUrl)
        val request = EmailValidationRequest(
            clientSecret = "monkeys_are_GREAT",
            email = "foo@example.com",
            sendAttempt = 1,
            idServer = "TODO: Not specified in spec"
        )

        run {
            val response = client.accountApi.requestTokenTo3PIDEmail(request)
            assertEquals("123abc", response.sid)
            assertEquals("https://example.org/path/to/submitToken", response.submitUrl)
        }
        run {
            val error = assertFailsWith<MatrixError.ThreePIdInUse> {
                client.accountApi.requestTokenTo3PIDEmail(request)
            }
            assertEquals("Third party identifier already in use", error.error)
        }
        run {
            val error = assertFailsWith<MatrixError.ThreePIdDenied> {
                client.accountApi.requestTokenTo3PIDEmail(request)
            }
            assertEquals("Third party identifier is not allowed", error.error)
        }
    }

    @Test
    fun testAccount3PIdMsisdnRequestToken() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "sid": "123abc",
                      "submit_url": "https://example.org/path/to/submitToken"
                    }
                """)
            }
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "errcode": "M_THREEPID_IN_USE",
                      "error": "Third party identifier already in use"
                    }
                """, HttpStatusCode.BadRequest)
            }
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "errcode": "M_THREEPID_DENIED",
                      "error": "Third party identifier is not allowed"
                    }
                """, HttpStatusCode.Forbidden)
            }
        }

        val client = MatrixClient(mockEngine, baseUrl)
        val request = MSISDNValidationRequest(
            clientSecret = "monkeys_are_GREAT",
            country = "GB",
            phoneNumber = "07700900001",
            sendAttempt = 1,
            idServer = "TODO: Not in spec example"
        )

        run {
            val response = client.accountApi.requestTokenTo3PIDMSISDN(request)
            assertEquals("123abc", response.sid)
            assertEquals("https://example.org/path/to/submitToken", response.submitUrl)
        }
        run {
            val error = assertFailsWith<MatrixError.ThreePIdInUse> {
                client.accountApi.requestTokenTo3PIDMSISDN(request)
            }
            assertEquals("Third party identifier already in use", error.error)
        }
        run {
            val error = assertFailsWith<MatrixError.ThreePIdDenied> {
                client.accountApi.requestTokenTo3PIDMSISDN(request)
            }
            assertEquals("Third party identifier is not allowed", error.error)
        }

    }

    @Test
    fun testWhoAmI() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "user_id": "@joe:example.org"
                    }
                """)
            }
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "errcode": "M_UNKNOWN_TOKEN",
                      "error": "Unrecognised access token."
                    }
                """, HttpStatusCode.Unauthorized)
            }
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "errcode": "M_FORBIDDEN",
                      "error": "Application service has not registered this user."
                    }
                """, HttpStatusCode.Forbidden)
            }
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
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

        val client = MatrixClient(mockEngine, baseUrl)

        run {
            val response = client.accountApi.getTokenOwner()
            assertEquals("@joe:example.org", response)
        }
        run {
            val error = assertFailsWith<MatrixError.UnknownToken> {
                client.accountApi.getTokenOwner()
            }
            assertEquals("Unrecognised access token.", error.error)
        }
        run {
            val error = assertFailsWith<MatrixError.Forbidden> {
                client.accountApi.getTokenOwner()
            }
            assertEquals("Application service has not registered this user.", error.error)
        }
        run {
            val error = assertFailsWith<MatrixError.LimitExceeded> {
                client.accountApi.getTokenOwner()
            }
            assertEquals("Too many requests", error.error)
            assertEquals(2000, error.retryAfterMillis)
        }
    }

    @Test
    fun testCapabilities() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "capabilities": {
                        "m.change_password": {
                          "enabled": false
                        },
                        "m.room_versions": {
                          "default": "1",
                          "available": {
                            "1": "stable",
                            "2": "stable",
                            "3": "unstable",
                            "test-version": "unstable"
                          }
                        },
                        "com.example.custom.ratelimit": {
                          "max_requests_per_hour": 600
                        }
                      }
                    }
                """)
            }
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
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

        val client = MatrixClient(mockEngine, baseUrl)

        run {
            val response = client.miscApi.getCapabilities()
            assertEquals(false, response.capabilities.changePassword?.enabled)
            assertEquals("1", response.capabilities.roomVersions?.default)
            assertEquals(RoomVersionStability.STABLE, response.capabilities.roomVersions!!.available["1"])
            assertEquals(RoomVersionStability.STABLE, response.capabilities.roomVersions!!.available["2"])
            assertEquals(RoomVersionStability.UNSTABLE, response.capabilities.roomVersions!!.available["3"])
            assertEquals(RoomVersionStability.UNSTABLE, response.capabilities.roomVersions!!.available["test-version"])
        }
        run {
            val error = assertFailsWith<MatrixError.LimitExceeded> {
                client.miscApi.getCapabilities()
            }
            assertEquals("Too many requests", error.error)
            assertEquals(2000, error.retryAfterMillis)
        }
    }

    @Test
    fun testUploadFilter() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "filter_id": "66696p746572"
                    }
                """)
            }
        }

        val client = MatrixClient(mockEngine, baseUrl)
        val filter = Filter(
            room = RoomFilter(
                state = StateFilter(
                    types = listOf("m.room.*"),
                    notRooms = listOf("!726s6s6q:example.com")
                ),
                timeline = RoomEventFilter(
                    limit = 10,
                    types = listOf("m.room.message"),
                    notRooms = listOf("!726s6s6q:example.com"),
                    notSenders = listOf("@spam:example.com")
                ),
                ephemeral = RoomEventFilter(
                    types = listOf(
                        "m.receipt",
                        "m.typing"
                    ),
                    notRooms = listOf("!726s6s6q:example.com"),
                    notSenders = listOf("@spam:example.com")
                )
            ),
            presence = EventFilter(
                types = listOf("m.presence"),
                notSenders = listOf("@alice:example.com")
            ),
            eventFormat = "client",
            eventFields = listOf(
                "type",
                "content",
                "sender"
            )
        )

        run {
            val response = client.filterApi.defineFilter("@alice:example.com", filter)
            assertEquals("66696p746572", response)
        }
    }

    @Test
    fun testDownloadFilter() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "room": {
                        "state": {
                          "types": [
                            "m.room.*"
                          ],
                          "not_rooms": [
                            "!726s6s6q:example.com"
                          ]
                        },
                        "timeline": {
                          "limit": 10,
                          "types": [
                            "m.room.message"
                          ],
                          "not_rooms": [
                            "!726s6s6q:example.com"
                          ],
                          "not_senders": [
                            "@spam:example.com"
                          ]
                        },
                        "ephemeral": {
                          "types": [
                            "m.receipt",
                            "m.typing"
                          ],
                          "not_rooms": [
                            "!726s6s6q:example.com"
                          ],
                          "not_senders": [
                            "@spam:example.com"
                          ]
                        }
                      },
                      "presence": {
                        "types": [
                          "m.presence"
                        ],
                        "not_senders": [
                          "@alice:example.com"
                        ]
                      },
                      "event_format": "client",
                      "event_fields": [
                        "type",
                        "content",
                        "sender"
                      ]
                    }
                """)
            }
        }

        val client = MatrixClient(mockEngine, baseUrl)

        run {
            client.filterApi.getFilter("@alice:example.com", "66696p746572")
        }
    }

    @Test
    fun testGetEventContent() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "name": "Example room name"
                    }
                """)
            }
        }

        val client = MatrixClient(mockEngine, baseUrl)

        run {
            val response = client.roomApi.getRoomStateWithKey(
                "!21636q39766251@example.com", "m.room.name", "")
            assertEquals("Example room name", response["name"]?.let { it.jsonPrimitive.contentOrNull })
        }
    }

    @Test
    fun testGetJoinedMembers() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "joined": {
                        "@bar:example.com": {
                          "display_name": "Bar",
                          "avatar_url": "mxc://riot.ovh/printErCATzZijQsSDWorRaK"
                        }
                      }
                    }
                """)
            }
        }

        val client = MatrixClient(mockEngine, baseUrl)

        run {
            val response = client.roomApi.getJoinedMembersByRoom("!21636q39766251@example.com")
            assertEquals(1, response.size)
            val (userId, some) = response.entries.single()
            assertEquals("@bar:example.com", userId)
            assertEquals("Bar", some.displayName)
            assertEquals("mxc://riot.ovh/printErCATzZijQsSDWorRaK", some.avatarUrl)
        }
    }

    @Test
    fun testGetRoomMessages() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "start": "t47429-4392820_219380_26003_2265",
                      "end": "t47409-4357353_219380_26003_2265",
                      "chunk": [
                        {
                          "content": {
                            "body": "This is an example text message",
                            "msgtype": "m.text",
                            "format": "org.matrix.custom.html",
                            "formatted_body": "<b>This is an example text message</b>"
                          },
                          "type": "m.room.message",
                          "event_id": "$143273582443PhrSn:example.org",
                          "room_id": "!636q39766251:example.com",
                          "sender": "@example:example.org",
                          "origin_server_ts": 1432735824653,
                          "unsigned": {
                            "age": 1234
                          }
                        },
                        {
                          "content": {
                            "name": "The room name"
                          },
                          "type": "m.room.name",
                          "event_id": "$143273582443PhrSn:example.org",
                          "room_id": "!636q39766251:example.com",
                          "sender": "@example:example.org",
                          "origin_server_ts": 1432735824653,
                          "unsigned": {
                            "age": 1234
                          },
                          "state_key": ""
                        },
                        {
                          "content": {
                            "body": "Gangnam Style",
                            "url": "mxc://example.org/a526eYUSFFxlgbQYZmo442",
                            "info": {
                              "thumbnail_url": "mxc://example.org/FHyPlCeYUSFFxlgbQYZmoEoe",
                              "thumbnail_info": {
                                "mimetype": "image/jpeg",
                                "size": 46144,
                                "w": 300,
                                "h": 300
                              },
                              "w": 480,
                              "h": 320,
                              "duration": 2140786,
                              "size": 1563685,
                              "mimetype": "video/mp4"
                            },
                            "msgtype": "m.video"
                          },
                          "type": "m.room.message",
                          "event_id": "$143273582443PhrSn:example.org",
                          "room_id": "!636q39766251:example.com",
                          "sender": "@example:example.org",
                          "origin_server_ts": 1432735824653,
                          "unsigned": {
                            "age": 1234
                          }
                        }
                      ]
                    }
                """)
            }
        }

        val client = MatrixClient(mockEngine, baseUrl)

        run {
            val response = client.roomApi.getRoomEvents(
                "!636q39766251@example.com", "s345_678_333", dir = Direction.B, limit = 3)
            assertEquals("t47429-4392820_219380_26003_2265", response.start)
            assertEquals("t47409-4357353_219380_26003_2265", response.end)
        }
    }

    @Test
    fun testUpdateRoomState() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "event_id": "${'$'}YUwRidLecu:example.com"
                    }
                """)
            }
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "errcode": "M_FORBIDDEN",
                      "error": "You do not have permission to send the event."
                    }
                """, HttpStatusCode.Forbidden)
            }
        }

        val client = MatrixClient(mockEngine, baseUrl)
        val request = MatrixJson.encodeToJsonElement(
            MemberContent.serializer(),
            MemberContent(
                membership = Membership.JOIN,
                avatarUrl = "mxc://localhost/SEsfnsuifSDFSSEF",
                displayName = "Alice Margatroid"
            )
        )

        run {
            val response = client.roomApi.setRoomStateWithKey(
                "!636q39766251@example.com", "m.room.member", "@alice@example.com",
                request)
            assertEquals("${'$'}YUwRidLecu:example.com", response)
        }
        run {
            val error = assertFailsWith<MatrixError.Forbidden> {
                client.roomApi.setRoomStateWithKey(
                    "!636q39766251@example.com", "m.room.member", "@alice@example.com",
                    request)
            }
            assertEquals("You do not have permission to send the event.", error.error)
        }
    }

    @Test
    fun testSendEventType() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "event_id": "${'$'}YUwRidLecu:example.com"
                    }
                """)
            }
        }

        val client = MatrixClient(mockEngine, baseUrl)

        // val request = RoomMessageContent.Text(
        //     body = "hello"
        // )
        val request = buildJsonObject {
            put("msgtype", "m.text")
            put("body", "hello")
        }

        run {
            val response = client.roomApi.sendMessage(
                "!636q39766251@example.com", "m.room.message", "35", request)
            assertEquals("${'$'}YUwRidLecu:example.com", response)
        }
    }

    @Test
    fun testRedact() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "event_id": "${'$'}YUwQidLecu:example.com"
                    }
                """)
            }
        }

        val client = MatrixClient(mockEngine, baseUrl)

        run {
            val response = client.roomApi.redactEvent(
                "!637q39766251@example.com", "bai2b1i9%3Amatrix.org", "37", "Indecent material")
            assertEquals("${'$'}YUwQidLecu:example.com", response)
        }
    }

    @Test
    fun testCreateRoomAlias() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {}
                """)
            }
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "errcode": "M_UNKNOWN",
                      "error": "Room alias #monkeys:matrix.org already exists."
                    }
                """, HttpStatusCode.Conflict)
            }
        }

        val client = MatrixClient(mockEngine, baseUrl)

        run {
            client.roomApi.setRoomAlias("!monkeys@matrix.org", "!abnjk1jdasj98:capuchins.com")
        }
        run {
            val error = assertFailsWith<MatrixError.Unknown> {
                client.roomApi.setRoomAlias("!monkeys@matrix.org", "!abnjk1jdasj98:capuchins.com")
            }
            assertEquals("Room alias #monkeys:matrix.org already exists.", error.error)
        }
    }

    @Test
    fun testDeleteRoomAlias() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {}
                """)
            }
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "errcode": "M_NOT_FOUND",
                      "error": "Room alias #monkeys:example.org not found."
                    }
                """, HttpStatusCode.NotFound)
            }
        }

        val client = MatrixClient(mockEngine, baseUrl)

        run {
            client.roomApi.deleteRoomAlias("%23monkeys%3Amatrix.org")
        }
        run {
            val error = assertFailsWith<MatrixError.NotFound> {
                client.roomApi.deleteRoomAlias("%23monkeys%3Amatrix.org")
            }
            assertEquals("Room alias #monkeys:example.org not found.", error.error)
        }
    }

    @Test
    fun testJoinRoom() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
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

        val client = MatrixClient(mockEngine, baseUrl)

        val request = JoinRoomRequest(
            thirdPartySigned = ThirdPartySigned(
                sender = "@alice:example.org",
                mixid = "@bob:example.org",
                token = "random8nonce"
                // TODO
                // "signatures": {
                //   "example.org": {
                //     "ed25519:0": "some9signature"
                //   }
                // }
            )
        )

        run {
            val error = assertFailsWith<MatrixError.LimitExceeded> {
                client.roomApi.joinRoomById("A room", request)
            }
            assertEquals("Too many requests", error.error)
            assertEquals(2000, error.retryAfterMillis)
        }
    }

    @Test
    fun testJoinRoomRoomIdOrAlias() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
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

        val client = MatrixClient(mockEngine, baseUrl)

        val request = JoinRoomRequest(
            thirdPartySigned = ThirdPartySigned(
                sender = "@alice:example.org",
                mixid = "@bob:example.org",
                token = "random8nonce"
                // TODO
                // "signatures": {
                //   "example.org": {
                //     "ed25519:0": "some9signature"
                //   }
                // }
            )
        )

        run {
            val error = assertFailsWith<MatrixError.LimitExceeded> {
                client.roomApi.joinRoom(
                    "%23monkeys@matrix.org", listOf("matrix.org", "elsewhere.ca"), request)
            }
            assertEquals("Too many requests", error.error)
            assertEquals(2000, error.retryAfterMillis)
        }
    }

    @Test
    fun testLeave() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {}
                """)
            }
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
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

        val client = MatrixClient(mockEngine, baseUrl)

        // POST /_matrix/client/r0/rooms//leave HTTP/1.1

        run {
            client.roomApi.leaveRoom("%21nkl290a%@matrix.org")
        }
        run {
            val error = assertFailsWith<MatrixError.LimitExceeded> {
                client.roomApi.leaveRoom("%21nkl290a%@matrix.org")
            }
            assertEquals("Too many requests", error.error)
            assertEquals(2000, error.retryAfterMillis)
        }
    }

    @Test
    fun testForget() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {}
                """)
            }
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "errcode": "M_UNKNOWN",
                      "error": "User @example:matrix.org is in room !au1ba7o:matrix.org"
                    }
                """, HttpStatusCode.BadRequest)
            }
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
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

        val client = MatrixClient(mockEngine, baseUrl)

        run {
            client.roomApi.forgetRoom("%21au1ba7o%3Amatrix.org")
        }
        run {
            val error = assertFailsWith<MatrixError.Unknown> {
                client.roomApi.forgetRoom("%21au1ba7o%3Amatrix.org")
            }
            assertEquals("User @example:matrix.org is in room !au1ba7o:matrix.org", error.error)
        }
        run {
            val error = assertFailsWith<MatrixError.LimitExceeded> {
                client.roomApi.forgetRoom("%21au1ba7o%3Amatrix.org")
            }
            assertEquals("Too many requests", error.error)
            assertEquals(2000, error.retryAfterMillis)
        }

    }

    @Test
    fun testKick() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {}
                """)
            }
        }

        val client = MatrixClient(mockEngine, baseUrl)
        val request = KickRequest(
            reason = "Telling unfunny jokes",
            userId = "@cheeky_monkey:matrix.org"
        )

        run {
            client.roomApi.kick("%21e42d8c%3Amatrix.org", request)
        }
    }

    @Test
    fun testBan() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {}
                """)
            }
        }

        val client = MatrixClient(mockEngine, baseUrl)
        val request = BanRequest(
            reason = "Telling unfunny jokes",
            userId = "@cheeky_monkey:matrix.org"
        )

        run {
            client.roomApi.ban("%21e42d8c@matrix.org", request)
        }
    }

    @Test
    fun testUnban() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {}
                """)
            }
        }

        val client = MatrixClient(mockEngine, baseUrl)

        run {
            client.roomApi.unban("!e42d8c@matrix.org", "@cheeky_monkey:matrix.org")
        }
    }

    @Test
    fun testSetVisibility() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {}
                """)
            }
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "errcode": "M_NOT_FOUND",
                      "error": "Room not found"
                    }
                """, HttpStatusCode.NotFound)
            }
        }

        val client = MatrixClient(mockEngine, baseUrl)

        run {
            client.roomApi.setVisibility("%21curbf%3Amatrix.org", RoomVisibility.PUBLIC)
        }
        run {
            val error = assertFailsWith<MatrixError.NotFound> {
                client.roomApi.setVisibility("%21curbf%3Amatrix.org", RoomVisibility.PUBLIC)
            }
            assertEquals("Room not found", error.error)
        }
    }

    @Test
    fun testSearchPublicRooms() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "chunk": [
                        {
                          "aliases": [
                            "#murrays:cheese.bar"
                          ],
                          "avatar_url": "mxc://bleeker.street/CHEDDARandBRIE",
                          "guest_can_join": false,
                          "name": "CHEESE",
                          "num_joined_members": 37,
                          "room_id": "!ol19s:bleecker.street",
                          "topic": "Tasty tasty cheese",
                          "world_readable": true
                        }
                      ],
                      "next_batch": "p190q",
                      "prev_batch": "p1902",
                      "total_room_count_estimate": 115
                    }
                """)
            }
        }

        val client = MatrixClient(mockEngine, baseUrl)

        val request = SearchPublicRoomsRequest(
            limit = 10,
            filter = SearchPublicRoomsRequest.Filter(
                genericSearchTerm = "foo"
            ),
            includeAllNetworks = false,
            thirdPartyInstanceId = "irc"
        )

        run {
            val response = client.roomApi.queryPublicRooms(params = request)
            assertEquals("p190q", response.nextBatch)
            assertEquals("p1902", response.prevBatch)
            assertEquals(115, response.totalRoomCountEstimate)
        }
    }

    @Test
    fun testUserDirectorySearch() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "results": [
                        {
                          "user_id": "@foo:bar.com",
                          "display_name": "Foo",
                          "avatar_url": "mxc://bar.com/foo"
                        }
                      ],
                      "limited": false
                    }
                """)
            }
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
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

        val client = MatrixClient(mockEngine, baseUrl)

        run {
            val response = client.userApi.searchUserDirectory("foo", 10)
            assertEquals(false, response.limited)
            assertEquals(1, response.results.size)
            assertEquals("@foo:bar.com", response.results[0].userId)
            assertEquals("Foo", response.results[0].displayName)
            assertEquals("mxc://bar.com/foo", response.results[0].avatarUrl)
        }
        run {
            val error = assertFailsWith<MatrixError.LimitExceeded> {
                client.userApi.searchUserDirectory("foo", 10)
            }
            assertEquals("Too many requests", error.error)
            assertEquals(2000, error.retryAfterMillis)
        }
    }

    @Test
    fun testUserDisplayName() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {}
                """)
            }
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
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

        val client = MatrixClient(mockEngine, baseUrl)

        run {
            client.userApi.setDisplayName("%40alice@example.com", "Alice Margatroid")
        }
        run {
            val error = assertFailsWith<MatrixError.LimitExceeded> {
                client.userApi.setDisplayName("%40alice@example.com", "Alice Margatroid")
            }
            assertEquals("Too many requests", error.error)
            assertEquals(2000, error.retryAfterMillis)
        }
    }

    @Test
    fun testGetUserDisplayName() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "displayname": "Alice Margatroid"
                    }
                """)
            }
        }

        val client = MatrixClient(mockEngine, baseUrl)

        run {
            val response = client.userApi.getDisplayName("@alice:example.com")
            assertEquals("Alice Margatroid", response)
        }
    }

    @Test
    fun testSetUserAvatar() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {}
                """)
            }
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
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

        val client = MatrixClient(mockEngine, baseUrl)

        run {
            client.userApi.setAvatarUrl("%40alice%3Aexample.com", "mxc://matrix.org/wefh34uihSDRGhw34")
        }
        run {
            val error = assertFailsWith<MatrixError.LimitExceeded> {
                client.userApi.setAvatarUrl("%40alice%3Aexample.com", "mxc://matrix.org/wefh34uihSDRGhw34")
            }
            assertEquals("Too many requests", error.error)
            assertEquals(2000, error.retryAfterMillis)
        }
    }

    @Test
    fun testGetAvatarUrl() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "avatar_url": "mxc://matrix.org/SDGdghriugerRg"
                    }
                """)
            }
        }

        val client = MatrixClient(mockEngine, baseUrl)

        run {
            val response = client.userApi.getAvatarUrl("%40alice%3Aexample.com")
            assertEquals("mxc://matrix.org/SDGdghriugerRg", response)
        }
    }

    @Test
    fun testGetUserProfile() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "avatar_url": "mxc://matrix.org/SDGdghriugerRg",
                      "displayname": "Alice Margatroid"
                    }
                """)
            }
        }

        val client = MatrixClient(mockEngine, baseUrl)

        run {
            val response = client.userApi.getUserProfile("%40alice%3Aexample.com")
            assertEquals("mxc://matrix.org/SDGdghriugerRg", response.avatarUrl)
            assertEquals("Alice Margatroid", response.displayName)
        }
    }

    @Test
    fun testGetTurnServer() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "username": "1443779631:@user:example.com",
                      "password": "JlKfBy1QwLrO20385QyAtEyIv0=",
                      "uris": [
                        "turn:turn.example.com:3478?transport=udp",
                        "turn:10.20.30.40:3478?transport=tcp",
                        "turns:10.20.30.40:443?transport=tcp"
                      ],
                      "ttl": 86400
                    }
                """)
            }
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
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

        val client = MatrixClient(mockEngine, baseUrl)

        run {
            val response = client.voIPApi.getTurnServer()
            assertEquals("1443779631:@user:example.com", response.username)
            assertEquals("JlKfBy1QwLrO20385QyAtEyIv0=", response.password)
            assertEquals(86400, response.ttl)
            assertEquals(3, response.uris.size)
            assertEquals("turn:turn.example.com:3478?transport=udp", response.uris[0])
            assertEquals("turn:10.20.30.40:3478?transport=tcp", response.uris[1])
            assertEquals("turns:10.20.30.40:443?transport=tcp", response.uris[2])
        }
        run {
            val error = assertFailsWith<MatrixError.LimitExceeded> {
                client.voIPApi.getTurnServer()
            }
            assertEquals("Too many requests", error.error)
            assertEquals(2000, error.retryAfterMillis)
        }
    }

    @Test
    fun testTyping() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {}
                """)
            }
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
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

        val client = MatrixClient(mockEngine, baseUrl)
        val request = TypingRequest(
            typing = true,
            timeout = 30000
        )

        run {
            client.roomApi.setTyping("@alice:example.com", "@wefh3sfukhs:example.com", request)
        }
        run {
            val error = assertFailsWith<MatrixError.LimitExceeded> {
                client.roomApi.setTyping("@alice:example.com", "@wefh3sfukhs:example.com", request)
            }
            assertEquals("Too many requests", error.error)
            assertEquals(2000, error.retryAfterMillis)
        }
    }

    @Test
    fun testRoomUpdateReceipt() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {}
                """)
            }
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
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

        val client = MatrixClient(mockEngine, baseUrl)

        run {
            client.roomApi.postReceipt("!wefuh21ffskfuh345@example.com", "m.read", "%241924376522eioj%3Aexample.com")
        }
        run {
            val error = assertFailsWith<MatrixError.LimitExceeded> {
                client.roomApi.postReceipt("!wefuh21ffskfuh345@example.com", "m.read", "%241924376522eioj%3Aexample.com")
            }
            assertEquals("Too many requests", error.error)
            assertEquals(2000, error.retryAfterMillis)
        }
    }

    @Test
    fun testReadMarkers() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {}
                """)
            }
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
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

        val client = MatrixClient(mockEngine, baseUrl)
        val request = ReadMarkersRequest(
            fullyRead = "\$somewhere:example.org",
            read = "\$elsewhere:example.org"
        )

        run {
            client.roomApi.setReadMarker("%21somewhere%3Aexample.org", request)
        }
        run {
            val error = assertFailsWith<MatrixError.LimitExceeded> {
                client.roomApi.setReadMarker("%21somewhere%3Aexample.org", request)
            }
            assertEquals("Too many requests", error.error)
            assertEquals(2000, error.retryAfterMillis)
        }
    }

    @Test
    fun testSetPresenceAndStatus() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {}
                """)
            }
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
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

        val client = MatrixClient(mockEngine, baseUrl)

        run {
            client.userApi.setPresence("@alice:example.com", Presence.ONLINE, "I am here.")
        }
        run {
            val error = assertFailsWith<MatrixError.LimitExceeded> {
                client.userApi.setPresence("%40alice:Aexample.com", Presence.ONLINE, "I am here.")
            }
            assertEquals("Too many requests", error.error)
            assertEquals(2000, error.retryAfterMillis)
        }
    }

    @Test
    fun testGetUserPresence() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "presence": "unavailable",
                      "last_active_ago": 420845
                    }
                """)
            }
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "errcode": "M_FORBIDDEN",
                      "error": "You are not allowed to see their presence"
                    }
                """, HttpStatusCode.Forbidden)
            }
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "errcode": "M_UNKNOWN",
                      "error": "An unknown error occurred"
                    }
                """, HttpStatusCode.NotFound)
            }
        }

        val client = MatrixClient(mockEngine, baseUrl)

        run {
            val response = client.userApi.getPresence("@alice:example.com")
            assertEquals(Presence.UNAVAILABLE, response.presence)
            assertEquals(420845, response.lastActiveAgo)
        }
        run {
            val error = assertFailsWith<MatrixError.Forbidden> {
                client.userApi.getPresence("@alice:example.com")
            }
            assertEquals("You are not allowed to see their presence", error.error)
        }
        run {
            val error = assertFailsWith<MatrixError.Unknown> {
                client.userApi.getPresence("@alice:example.com")
            }
            assertEquals("An unknown error occurred", error.error)
        }
    }

    @KtorExperimentalAPI
    @Test
    fun testUpload() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                assertEquals(ContentType.Application.Pdf, it.body.contentType)
                assertTrue(byteArrayOf(123, 21, 0).contentEquals(it.body.toByteArray()))

                // language=json
                respondJson("""
                    {
                      "content_uri": "mxc://example.com/AQwafuaFswefuhsfAFAgsw"
                    }
                """)
            }
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "errcode": "M_TOO_LARGE",
                      "error": "Cannot upload files larger than 100mb"
                    }
                """, HttpStatusCode.PayloadTooLarge)
            }
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
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

        val client = MatrixClient(mockEngine, baseUrl)

        val blob = byteArrayOf(123, 21, 0)

        run {
            val response = client.contentApi.uploadContent("application/pdf", "War and Peace.pdf", blob)
            assertEquals("mxc://example.com/AQwafuaFswefuhsfAFAgsw", response)
        }
        run {
            val error = assertFailsWith<MatrixError.TooLarge> {
                client.contentApi.uploadContent("application/pdf", "War and Peace.pdf", blob)
            }
            assertEquals("Cannot upload files larger than 100mb", error.error)
        }
        run {
            val error = assertFailsWith<MatrixError.LimitExceeded> {
                client.contentApi.uploadContent("application/pdf", "War and Peace.pdf", blob)
            }
            assertEquals("Too many requests", error.error)
            assertEquals(2000, error.retryAfterMillis)
        }
    }

    @Test
    fun testDownload() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
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
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "errcode": "M_TOO_LARGE",
                      "error": "Content is too large to serve"
                    }
                """, HttpStatusCode.BadGateway)
            }
        }

        val client = MatrixClient(mockEngine, baseUrl)

        run {
            val error = assertFailsWith<MatrixError.LimitExceeded> {
                client.contentApi.getContent("matrix.org", "ascERGshawAWawugaAcauga", false)
            }
            assertEquals("Too many requests", error.error)
            assertEquals(2000, error.retryAfterMillis)
        }
        run {
            val error = assertFailsWith<MatrixError.TooLarge> {
                client.contentApi.getContent("matrix.org", "ascERGshawAWawugaAcauga", false)
            }
            assertEquals("Content is too large to serve", error.error)
        }
    }

    @Test
    fun testDownloadServerName() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
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
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "errcode": "M_TOO_LARGE",
                      "error": "Content is too large to serve"
                    }
                """, HttpStatusCode.BadGateway)
            }
        }

        val client = MatrixClient(mockEngine, baseUrl)

        run {
            val error = assertFailsWith<MatrixError.LimitExceeded> {
                client.contentApi.getContentOverrideName("matrix.org", "ascERGshawAWawugaAcauga", "filename.jpg", false)
            }
            assertEquals("Too many requests", error.error)
            assertEquals(2000, error.retryAfterMillis)
        }
        run {
            val error = assertFailsWith<MatrixError.TooLarge> {
                client.contentApi.getContentOverrideName("matrix.org", "ascERGshawAWawugaAcauga", "filename.jpg", false)
            }
            assertEquals("Content is too large to serve", error.error)
        }
    }

    @Test
    fun testGetThumbnail() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "errcode": "M_UNKNOWN",
                      "error": "Cannot generate thumbnails for the requested content"
                    }
                """, HttpStatusCode.BadRequest)
            }
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "errcode": "M_TOO_LARGE",
                      "error": "Content is too large to thumbnail"
                    }
                """, HttpStatusCode.PayloadTooLarge)
            }
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
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
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "errcode": "M_TOO_LARGE",
                      "error": "Content is too large to thumbnail"
                    }
                """, HttpStatusCode.BadGateway)
            }
        }

        val client = MatrixClient(mockEngine, baseUrl)

        run {
            val error = assertFailsWith<MatrixError.Unknown> {
                client.contentApi.getContentThumbnail("example.org", "ascERGshawAWawugaAcauga",
                    width = 64, height = 64, method = ThumbnailMethod.SCALE, allowRemote = false)
            }
            assertEquals("Cannot generate thumbnails for the requested content", error.error)
        }
        run {
            val error = assertFailsWith<MatrixError.TooLarge> {
                client.contentApi.getContentThumbnail("example.org", "ascERGshawAWawugaAcauga",
                    width = 64, height = 64, method = ThumbnailMethod.SCALE, allowRemote = false)
            }
            assertEquals("Content is too large to thumbnail", error.error)
        }
        run {
            val error = assertFailsWith<MatrixError.LimitExceeded> {
                client.contentApi.getContentThumbnail("example.org", "ascERGshawAWawugaAcauga",
                    width = 64, height = 64, method = ThumbnailMethod.SCALE, allowRemote = false)
            }
            assertEquals("Too many requests", error.error)
            assertEquals(2000, error.retryAfterMillis)
        }
        run {
            val error = assertFailsWith<MatrixError.TooLarge> {
                client.contentApi.getContentThumbnail("example.org", "ascERGshawAWawugaAcauga",
                    width = 64, height = 64, method = ThumbnailMethod.SCALE, allowRemote = false)
            }
            assertEquals("Content is too large to thumbnail", error.error)
        }
    }

    @Test
    fun testGetPreviewUrl() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "og:title": "Matrix Blog Post",
                      "og:description": "This is a really cool blog post from matrix.org",
                      "og:image": "mxc://example.com/ascERGshawAWawugaAcauga",
                      "og:image:type": "image/png",
                      "og:image:height": 48,
                      "og:image:width": 48,
                      "matrix:image:size": 102400
                    }
                """)
            }
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
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

        val client = MatrixClient(mockEngine, baseUrl)

        run {
            val response = client.contentApi.getUrlPreview("https://matrix.org", 1510610716656)
            assertEquals("Matrix Blog Post", response.title)
            assertEquals("This is a really cool blog post from matrix.org", response.description)
            assertEquals("mxc://example.com/ascERGshawAWawugaAcauga", response.image)
            assertEquals("image/png", response.imageType)
            assertEquals(48, response.imageHeight)
            assertEquals(48, response.imageWidth)
            assertEquals(102400, response.imageSize)
        }
        run {
            val error = assertFailsWith<MatrixError.LimitExceeded> {
                client.contentApi.getUrlPreview("https://matrix.org", 1510610716656)
            }
            assertEquals("Too many requests", error.error)
            assertEquals(2000, error.retryAfterMillis)
        }
    }

    @Test
    fun testGetMediaConfig() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "m.upload.size": 50000000
                    }
                """)
            }
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "errcode": "M_UNKNOWN",
                      "error": "An unknown error occurred"
                    }
                """, HttpStatusCode.TooManyRequests)
            }
        }

        val client = MatrixClient(mockEngine, baseUrl)

        run {
            val response = client.contentApi.getConfig()
            assertEquals(50000000, response.uploadSize)
        }
        run {
            val error = assertFailsWith<MatrixError.Unknown> {
                client.contentApi.getConfig()
            }
            assertEquals("An unknown error occurred", error.error)
        }
    }

    @Test
    fun testSendToDevice() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {}
                """)
            }
        }

        val client = MatrixClient(mockEngine, baseUrl)

        val messages = mapOf(
            "@alice:example.com" to mapOf(
                "TLLBEANAAG" to buildJsonObject {
                    put("example_content_key", "value")
                }
            )
        )

        run {
            client.deviceApi.sendToDevice("m.new_device", "35", messages)
        }
    }

    @Test
    fun testGetDevices() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "devices": [
                        {
                          "device_id": "QBUAZIFURK",
                          "display_name": "android",
                          "last_seen_ip": "1.2.3.4",
                          "last_seen_ts": 1474491775024
                        }
                      ]
                    }
                """)
            }
        }

        val client = MatrixClient(mockEngine, baseUrl)

        run {
            val response = client.deviceApi.getDevices()
            assertEquals(1, response.size)
            assertEquals("QBUAZIFURK", response[0].deviceId)
            assertEquals("android", response[0].displayName)
            assertEquals("1.2.3.4", response[0].lastSeenIp)
            assertEquals(1474491775024, response[0].lastSeenTs)
        }
    }

    @Test
    fun testGetDevice() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "device_id": "QBUAZIFURK",
                      "display_name": "android",
                      "last_seen_ip": "1.2.3.4",
                      "last_seen_ts": 1474491775024
                    }
                """)
            }
        }

        val client = MatrixClient(mockEngine, baseUrl)

        run {
            val response = client.deviceApi.getDevice("The id of device")
            assertEquals("QBUAZIFURK", response.deviceId)
            assertEquals("android", response.displayName)
            assertEquals("1.2.3.4", response.lastSeenIp)
            assertEquals(1474491775024, response.lastSeenTs)
        }
    }

    @Test
    fun testSetDeviceDisplayName() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {}
                """)
            }
        }

        val client = MatrixClient(mockEngine, baseUrl)

        run {
            client.deviceApi.updateDevice("QBUAZIFURK", "My other phone")
        }
    }

    @Test
    fun testDeleteDevice() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {}
                """)
            }
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
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
        }

        val client = MatrixClient(mockEngine, baseUrl)
        val request = AuthenticationData.Dummy(
            session = "xxxxx"
            // "example_credential": "verypoorsharedsecret"
        )

        run {
            client.deviceApi.deleteDevice("QBUAZIFURK", request)
        }
        run {
            assertFails {
                client.deviceApi.deleteDevice("QBUAZIFURK", request)
            }
        }
    }

    @Test
    fun testDeleteDevices() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {}
                """)
            }
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
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
        }

        val client = MatrixClient(mockEngine, baseUrl)

        val devices = listOf(
            "QBUAZIFURK",
            "AUIECTSRND"
        )
        val auth = AuthenticationData.Dummy(
            session = "xxxxx"
            // "example_credential": "verypoorsharedsecret"
        )

        run {
            client.deviceApi.deleteDevices(devices, auth)
        }
        run {
            assertFails {
                client.deviceApi.deleteDevices(devices, auth)
            }
        }
    }

    @Test
    fun testKeyUpload() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "one_time_key_counts": {
                        "curve25519": 10,
                        "signed_curve25519": 20
                      }
                    }
                """)
            }
        }

        val client = MatrixClient(mockEngine, baseUrl)

        val keys = UploadKeysRequest(
            deviceKeys = DeviceKeys(
                userId = "@alice:example.com",
                deviceId = "JLAFKJWSCS",
                algorithms = listOf(
                    "m.olm.curve25519-aes-sha256",
                    "m.megolm.v1.aes-sha"
                ),
                keys = mapOf(
                    "curve25519:JLAFKJWSCS" to "3C5BFWi2Y8MaVvjM8M22DBmh24PmgR0nPvJOIArzgyI",
                    "ed25519:JLAFKJWSCS" to "lEuiRJBit0IG6nUf5pUzWTUEsRVVe/HJkoKuEww9ULI"
                ),
                signatures = mapOf(
                    "@alice:example.com" to mapOf(
                        "ed25519:JLAFKJWSCS" to "dSO80A01XiigH3uBiDVx/EjzaoycHcjq9lfQX0uWsqxl2giMIiSPR8a4d291W1ihKJL/a+myXS367WT6NAIcBA"
                    )
                )
            ),
            oneTimeKeys = mapOf(
                "curve25519:AAAAAQ" to "/qyvZvwjiTxGdGU0RCguDCLeR+nmsb3FfNG3/Ve4vU8",
                "signed_curve25519:AAAAHg" to KeyObject(
                    key = "zKbLg+NrIjpnagy+pIY6uPL4ZwEG2v+8F9lmgsnlZzs",
                    signatures = emptyMap()
                    // signatures = mapOf(
                    //     "@alice:example.com" to mapOf(
                    //         "ed25519:JLAFKJWSCS" to "FLWxXqGbwrb8SM3Y795eB6OA8bwBcoMZFXBqnTn58AYWZSqiD45tlBVcDa2L7RwdKXebW/VzDlnfVJ+9jok1Bw"
                    //     )
                    // )
                ),
                "signed_curve25519:AAAAHQ" to KeyObject(
                    key = "j3fR3HemM16M7CWhoI4Sk5ZsdmdfQHsKL1xuSft6MSw",
                    signatures = emptyMap()
                    // signatures = mapOf(
                    //     "@alice:example.com" to mapOf(
                    //         "ed25519:JLAFKJWSCS" to "IQeCEPb9HFk217cU9kw9EOiusC6kMIkoIRnbnfOh5Oc63S1ghgyjShBGpu34blQomoalCyXWyhaaT3MrLZYQAA"
                    //     )
                    // )
                )
            )
        )

        run {
            val response = client.keysApi.uploadKeys(keys)
            assertEquals(2, response.oneTimeKeyCounts.size)
            assertEquals(10, response.oneTimeKeyCounts["curve25519"])
            assertEquals(20, response.oneTimeKeyCounts["signed_curve25519"])
        }
    }

    @Test
    fun testKeysQuery() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "failures": {},
                      "device_keys": {
                        "@alice:example.com": {
                          "JLAFKJWSCS": {
                            "user_id": "@alice:example.com",
                            "device_id": "JLAFKJWSCS",
                            "algorithms": [
                              "m.olm.v1.curve25519-aes-sha256",
                              "m.megolm.v1.aes-sha"
                            ],
                            "keys": {
                              "curve25519:JLAFKJWSCS": "3C5BFWi2Y8MaVvjM8M22DBmh24PmgR0nPvJOIArzgyI",
                              "ed25519:JLAFKJWSCS": "lEuiRJBit0IG6nUf5pUzWTUEsRVVe/HJkoKuEww9ULI"
                            },
                            "signatures": {
                              "@alice:example.com": {
                                "ed25519:JLAFKJWSCS": "dSO80A01XiigH3uBiDVx/EjzaoycHcjq9lfQX0uWsqxl2giMIiSPR8a4d291W1ihKJL/a+myXS367WT6NAIcBA"
                              }
                            },
                            "unsigned": {
                              "device_display_name": "Alice's mobile phone"
                            }
                          }
                        }
                      }
                    }
                """)
            }
        }

        val client = MatrixClient(mockEngine, baseUrl)

        val query = QueryKeysRequest(
            timeout = 10000,
            deviceKeys = mapOf(
                "@alice:example.com" to listOf()
            ),
            token = "string"
        )

        run {
            val response = client.keysApi.queryKeys(query)
            assertTrue(response.failures.isEmpty())
            assertEquals(1, response.deviceKeys.size)
            assertEquals("@alice:example.com", response.deviceKeys.keys.single())
        }
    }

    @Test
    fun testKeysClaim() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "failures": {},
                      "one_time_keys": {
                        "@alice:example.com": {
                          "JLAFKJWSCS": {
                            "signed_curve25519:AAAAHg": {
                              "key": "zKbLg+NrIjpnagy+pIY6uPL4ZwEG2v+8F9lmgsnlZzs",
                              "signatures": {
                                "@alice:example.com": {
                                  "ed25519:JLAFKJWSCS": "FLWxXqGbwrb8SM3Y795eB6OA8bwBcoMZFXBqnTn58AYWZSqiD45tlBVcDa2L7RwdKXebW/VzDlnfVJ+9jok1Bw"
                                }
                              }
                            }
                          }
                        }
                      }
                    }
                """)
            }
        }

        val client = MatrixClient(mockEngine, baseUrl)

        val query = ClaimKeysRequest(
            timeout = 10000,
            oneTimeKeys = mapOf(
                "@alice:example.com" to mapOf(
                    "JLAFKJWSCS" to "signed_curve25519"
                )
            )
        )

        run {
            val response = client.keysApi.claimKeys(query)
            assertTrue(response.failures!!.isEmpty())
            assertEquals(1, response.oneTimeKeys.size)
            assertTrue(response.oneTimeKeys.getValue("@alice:example.com").getValue("JLAFKJWSCS")["signed_curve25519:AAAAHg"] is KeyObject)
        }
    }

    @Test
    fun testKeyChanges() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "changed": [
                        "@alice:example.com",
                        "@bob:example.org"
                      ],
                      "left": [
                        "@clara:example.com",
                        "@doug:example.org"
                      ]
                    }
                """)
            }
        }

        val client = MatrixClient(mockEngine, baseUrl)

        run {
            val response = client.keysApi.getKeysChanges(from = "s72594_4483_1934", to = "s75689_5632_2435")
            assertEquals("@alice:example.com", response.changed[0])
            assertEquals("@bob:example.org", response.changed[1])
            assertEquals("@clara:example.com", response.left[0])
            assertEquals("@doug:example.org", response.left[1])
        }
    }

    @Test
    fun testGetPushers() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "pushers": [
                        {
                          "pushkey": "Xp/MzCt8/9DcSNE9cuiaoT5Ac55job3TdLSSmtmYl4A=",
                          "kind": "http",
                          "app_id": "face.mcapp.appy.prod",
                          "app_display_name": "Appy McAppface",
                          "device_display_name": "Alice's Phone",
                          "profile_tag": "xyz",
                          "lang": "en-US",
                          "data": {
                            "url": "https://example.com/_matrix/push/v1/notify"
                          }
                        }
                      ]
                    }
                """)
            }
        }

        val client = MatrixClient(mockEngine, baseUrl)

        run {
            val response = client.pushApi.getPushers()
            assertEquals(1, response.size)
            assertEquals("Xp/MzCt8/9DcSNE9cuiaoT5Ac55job3TdLSSmtmYl4A=", response[0].pushKey)
            assertEquals("http", response[0].kind)
            assertEquals("Alice's Phone", response[0].deviceDisplayName)
        }
    }

    @Test
    fun testSetPushers() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {}
                """)
            }
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "error": "Missing parameters: lang, data",
                      "errcode": "M_MISSING_PARAM"
                    }
                """, HttpStatusCode.BadRequest)
            }
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
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

        val client = MatrixClient(mockEngine, baseUrl)

        val pusher = Pusher(
            lang = "en",
            kind = "http",
            appDisplayName = "Mat Rix",
            deviceDisplayName = "iPhone 9",
            profileTag = "xxyyzz",
            appId = "com.example.app.ios",
            pushKey = "APA91bHPRgkF3JUikC4ENAHEeMrd41Zxv3hVZjC9KtT8OvPVGJ-hQMRKRrZuJAEcl7B338qju59zJMjw2DELjzEvxwYv7hH5Ynpc1ODQ0aT4U4OFEeco8ohsN5PjL1iC2dNtk2BAokeMCg2ZXKqpc8FXKmhX94kIxQ",
            data = Pusher.Data(
                url = "https://push-gateway.location.here/_matrix/push/v1/notify",
                format = "event_id_only"
            )
            //append = false
        )

        run {
            client.pushApi.postPusher(pusher)
        }
        run {
            val error = assertFailsWith<MatrixError.MissingParam> {
                client.pushApi.postPusher(pusher)
            }
            assertEquals("Missing parameters: lang, data", error.error)
        }
        run {
            val error = assertFailsWith<MatrixError.LimitExceeded> {
                client.pushApi.postPusher(pusher)
            }
            assertEquals("Too many requests", error.error)
            assertEquals(2000, error.retryAfterMillis)
        }
    }

    @Test
    fun testGetNotifications() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "next_token": "abcdef",
                      "notifications": [
                        {
                          "actions": [
                            "notify"
                          ],
                          "profile_tag": "hcbvkzxhcvb",
                          "read": true,
                          "room_id": "!abcdefg:example.com",
                          "ts": 1475508881945,
                          "event": {
                            "content": {
                              "body": "This is an example text message",
                              "msgtype": "m.text",
                              "format": "org.matrix.custom.html",
                              "formatted_body": "<b>This is an example text message</b>"
                            },
                            "type": "m.room.message",
                            "event_id": "$143273582443PhrSn:example.org",
                            "room_id": "!jEsUZKDJdhlrceRyVU:example.org",
                            "sender": "@example:example.org",
                            "origin_server_ts": 1432735824653,
                            "unsigned": {
                              "age": 1234
                            }
                          }
                        }
                      ]
                    }
                """)
            }
        }

        val client = MatrixClient(mockEngine, baseUrl)

        run {
            val response = client.miscApi.getNotifications(from = "xxxxx", limit = 20, only = "highlight")
            assertEquals("abcdef", response.nextToken)
            assertEquals(1, response.notifications.size)
            assertEquals(1, response.notifications[0].actions.size)
            assertEquals("notify", response.notifications[0].actions[0].jsonPrimitive.content)
            assertEquals("hcbvkzxhcvb", response.notifications[0].profileTag)
            assertEquals(true, response.notifications[0].read)
            assertEquals("!abcdefg:example.com", response.notifications[0].roomId)
            assertEquals(1475508881945, response.notifications[0].ts)
            assertEquals("m.room.message", response.notifications[0].event.type)
        }
    }

    @Test
    fun testGetPushRules() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "global": {
                        "content": [
                          {
                            "actions": [
                              "notify",
                              {
                                "set_tweak": "sound",
                                "value": "default"
                              },
                              {
                                "set_tweak": "highlight"
                              }
                            ],
                            "default": true,
                            "enabled": true,
                            "pattern": "alice",
                            "rule_id": ".m.rule.contains_user_name"
                          }
                        ],
                        "override": [
                          {
                            "actions": [
                              "dont_notify"
                            ],
                            "conditions": [],
                            "default": true,
                            "enabled": false,
                            "rule_id": ".m.rule.master"
                          },
                          {
                            "actions": [
                              "dont_notify"
                            ],
                            "conditions": [
                              {
                                "key": "content.msgtype",
                                "kind": "event_match",
                                "pattern": "m.notice"
                              }
                            ],
                            "default": true,
                            "enabled": true,
                            "rule_id": ".m.rule.suppress_notices"
                          }
                        ],
                        "room": [],
                        "sender": [],
                        "underride": [
                          {
                            "actions": [
                              "notify",
                              {
                                "set_tweak": "sound",
                                "value": "ring"
                              },
                              {
                                "set_tweak": "highlight",
                                "value": false
                              }
                            ],
                            "conditions": [
                              {
                                "key": "type",
                                "kind": "event_match",
                                "pattern": "m.call.invite"
                              }
                            ],
                            "default": true,
                            "enabled": true,
                            "rule_id": ".m.rule.call"
                          },
                          {
                            "actions": [
                              "notify",
                              {
                                "set_tweak": "sound",
                                "value": "default"
                              },
                              {
                                "set_tweak": "highlight"
                              }
                            ],
                            "conditions": [
                              {
                                "kind": "contains_display_name"
                              }
                            ],
                            "default": true,
                            "enabled": true,
                            "rule_id": ".m.rule.contains_display_name"
                          },
                          {
                            "actions": [
                              "notify",
                              {
                                "set_tweak": "sound",
                                "value": "default"
                              },
                              {
                                "set_tweak": "highlight",
                                "value": false
                              }
                            ],
                            "conditions": [
                              {
                                "is": "2",
                                "kind": "room_member_count"
                              }
                            ],
                            "default": true,
                            "enabled": true,
                            "rule_id": ".m.rule.room_one_to_one"
                          },
                          {
                            "actions": [
                              "notify",
                              {
                                "set_tweak": "sound",
                                "value": "default"
                              },
                              {
                                "set_tweak": "highlight",
                                "value": false
                              }
                            ],
                            "conditions": [
                              {
                                "key": "type",
                                "kind": "event_match",
                                "pattern": "m.room.member"
                              },
                              {
                                "key": "content.membership",
                                "kind": "event_match",
                                "pattern": "invite"
                              },
                              {
                                "key": "state_key",
                                "kind": "event_match",
                                "pattern": "@alice:example.com"
                              }
                            ],
                            "default": true,
                            "enabled": true,
                            "rule_id": ".m.rule.invite_for_me"
                          },
                          {
                            "actions": [
                              "notify",
                              {
                                "set_tweak": "highlight",
                                "value": false
                              }
                            ],
                            "conditions": [
                              {
                                "key": "type",
                                "kind": "event_match",
                                "pattern": "m.room.member"
                              }
                            ],
                            "default": true,
                            "enabled": true,
                            "rule_id": ".m.rule.member_event"
                          },
                          {
                            "actions": [
                              "notify",
                              {
                                "set_tweak": "highlight",
                                "value": false
                              }
                            ],
                            "conditions": [
                              {
                                "key": "type",
                                "kind": "event_match",
                                "pattern": "m.room.message"
                              }
                            ],
                            "default": true,
                            "enabled": true,
                            "rule_id": ".m.rule.message"
                          }
                        ]
                      }
                    }
                """.trimIndent())
            }
        }

        val client = MatrixClient(mockEngine, baseUrl)

        run {
            val response = client.pushApi.getPushRules()
            assertEquals(true, response.global.room.isEmpty())
            assertEquals(true, response.global.sender.isEmpty())
            assertEquals(1, response.global.content.size)
            assertEquals(".m.rule.contains_user_name", response.global.content[0].ruleId)
        }
    }

    @Test
    fun testGetPushRule() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "actions": [
                        "dont_notify"
                      ],
                      "pattern": "cake*lie",
                      "rule_id": "nocake",
                      "enabled": true,
                      "default": false
                    }
                """)
            }
        }

        val client = MatrixClient(mockEngine, baseUrl)

        run {
            val response = client.pushApi.getPushRule("global", PushRuleKind.CONTENT,"nocake")
            assertEquals(JsonArray(listOf(JsonPrimitive("dont_notify"))), response.actions)
            assertEquals("cake*lie", response.pattern)
            assertEquals("nocake", response.ruleId)
            assertEquals(true, response.enabled)
            assertEquals(false, response.default)
        }
    }

    @Test
    fun testDeletePushRule() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {}
                """)
            }
        }

        val client = MatrixClient(mockEngine, baseUrl)

        run {
            client.pushApi.deletePushRule("global", PushRuleKind.CONTENT,"nocake")
        }
    }

    @Test
    fun testSetPushRule() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {}
                """)
            }
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "error": "before/after rule not found: someRuleId",
                      "errcode": "M_UNKNOWN"
                    }
                """, HttpStatusCode.BadRequest)
            }
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
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

        val client = MatrixClient(mockEngine, baseUrl)

        val pushRule = SetPushRuleRequest(
            pattern = "cake*lie",
            actions = listOf(
                PushRuleAction.NOTIFY
            )
        )

        run {
            client.pushApi.setPushRule(
                "global", PushRuleKind.CONTENT,"nocake",
                before = "someRuleId", after = "anotherRuleId", pushRule = pushRule)
        }
        run {
            val error = assertFailsWith<MatrixError.Unknown> {
                client.pushApi.setPushRule(
                    "global", PushRuleKind.CONTENT,"nocake",
                    before = "someRuleId", after = "anotherRuleId", pushRule = pushRule)
            }
            assertEquals("before/after rule not found: someRuleId", error.error)
        }
        run {
            val error = assertFailsWith<MatrixError.LimitExceeded> {
                client.pushApi.setPushRule(
                    "global", PushRuleKind.CONTENT,"nocake",
                    before = "someRuleId", after = "anotherRuleId", pushRule = pushRule)
            }
            assertEquals("Too many requests", error.error)
            assertEquals(2000, error.retryAfterMillis)
        }
    }

    @Test
    fun testIsPushRuleEnabled() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "enabled": true
                    }
                """)
            }
        }

        val client = MatrixClient(mockEngine, baseUrl)

        run {
            val response = client.pushApi.isPushRuleEnabled("global", PushRuleKind.CONTENT,"nocake")
            assertEquals(true, response)
        }
    }

    @Test
    fun testSetPushRuleEnabled() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {}
                """)
            }
        }

        val client = MatrixClient(mockEngine, baseUrl)

        run {
            client.pushApi.setPushRuleEnabled("global", PushRuleKind.CONTENT, "nocake", true)
        }
    }

    @Test
    fun testGetPushRuleActions() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "actions": [
                        "notify"
                      ]
                    }
                """)
            }
        }

        val client = MatrixClient(mockEngine, baseUrl)

        run {
            val response = client.pushApi.getPushRuleActions("global", PushRuleKind.CONTENT,"nocake")
            assertEquals("notify", response.single())
        }
    }

    @Test
    fun testSetPushRuleActions() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {}
                """)
            }
        }

        val client = MatrixClient(mockEngine, baseUrl)

        run {
            client.pushApi.setPushRuleActions("global", PushRuleKind.ROOM, "!spam:example.com", listOf("notify"))
        }
    }

    @Test
    fun testSearch() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "search_categories": {
                        "room_events": {
                          "groups": {
                            "room_id": {
                              "!qPewotXpIctQySfjSy:localhost": {
                                "order": 1,
                                "next_batch": "BdgFsdfHSf-dsFD",
                                "results": [
                                  "$144429830826TWwbB:localhost"
                                ]
                              }
                            }
                          },
                          "highlights": [
                            "martians",
                            "men"
                          ],
                          "next_batch": "5FdgFsd234dfgsdfFD",
                          "count": 1224,
                          "results": [
                            {
                              "rank": 0.00424866,
                              "result": {
                                "content": {
                                  "body": "This is an example text message",
                                  "msgtype": "m.text",
                                  "format": "org.matrix.custom.html",
                                  "formatted_body": "<b>This is an example text message</b>"
                                },
                                "type": "m.room.message",
                                "event_id": "$144429830826TWwbB:localhost",
                                "room_id": "!qPewotXpIctQySfjSy:localhost",
                                "sender": "@example:example.org",
                                "origin_server_ts": 1432735824653,
                                "unsigned": {
                                  "age": 1234
                                }
                              }
                            }
                          ]
                        }
                      }
                    }
                """)
            }
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
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

        val client = MatrixClient(mockEngine, baseUrl)

        val request = SearchRequest(
            searchCategories = Categories(
                roomEvents = RoomEventsCriteria(
                    keys = listOf("content.body"),
                    searchTerm = "martians and men",
                    orderBy = Ordering.RECENT,
                    groupings = Groupings(
                        groupBy = listOf(
                            Group(key = GroupKey.ROOM_ID)
                        )
                    )
                )
            )
        )

        run {
            val response = client.miscApi.search(nextBatch = "YWxsCgpOb25lLDM1ODcwOA", body = request)
            assertEquals(2, response.searchCategories.roomEvents!!.highlights.size)
            assertEquals("5FdgFsd234dfgsdfFD", response.searchCategories.roomEvents!!.nextBatch)
            assertEquals(1224, response.searchCategories.roomEvents!!.count)
            assertEquals(1, response.searchCategories.roomEvents!!.results.size)
        }
        run {
            val error = assertFailsWith<MatrixError.LimitExceeded> {
                client.miscApi.search(nextBatch = "YWxsCgpOb25lLDM1ODcwOA", body = request)
            }
            assertEquals("Too many requests", error.error)
            assertEquals(2000, error.retryAfterMillis)
        }
    }

    @Test
    fun testGetRoomTags() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "tags": {
                        "m.favourite": {
                          "order": 0.1
                        },
                        "u.Work": {
                          "order": 0.7
                        },
                        "u.Customers": {}
                      }
                    }
                """)
            }
        }

        val client = MatrixClient(mockEngine, baseUrl)

        run {
            val response = client.userApi.getRoomTags("@alice:example.com", "\$726s6s6q:example.com")
            assertEquals(3, response.tags.size)
            assertEquals(TagContent.Tag(0.1), response.tags["m.favourite"])
            assertEquals(TagContent.Tag(0.7), response.tags["u.Work"])
            assertEquals(TagContent.Tag(), response.tags["u.Customers"])
        }
    }

    @Test
    fun testSetRoomTag() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {}
                """)
            }
        }

        val client = MatrixClient(mockEngine, baseUrl)

        run {
            client.userApi.setRoomTag("@alice:example.com", "\$726s6s6q:example.com", "u.work", 0.25)
        }
    }

    @Test
    fun testDeleteRoomTag() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {}
                """)
            }
        }

        val client = MatrixClient(mockEngine, baseUrl)

        run {
            client.userApi.deleteRoomTag("@alice:example.com", "\$726s6s6q:example.com", "u.work")
        }
    }

    @Test
    fun testGetAccountData() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "custom_account_data_key": "custom_config_value"
                    }
                """)
            }
        }

        val client = MatrixClient(mockEngine, baseUrl)

        run {
            val response = client.userApi.getAccountData("@alice:example.com", "org.example.custom.config")
            assertTrue(response is JsonObject)
            assertEquals("custom_config_value", response.getValue("custom_account_data_key").jsonPrimitive.content)
        }
    }

    @Test
    fun testGetAccountDataPerRoom() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "custom_account_data_key": "custom_config_value"
                    }
                """)
            }
        }

        val client = MatrixClient(mockEngine, baseUrl)

        run {
            val response = client.userApi.getAccountDataPerRoom("@alice:example.com", "\$726s6s6q:example.com", "org.example.custom.room.config")
            assertTrue(response is JsonObject)
            assertEquals("custom_config_value", response.getValue("custom_account_data_key").jsonPrimitive.content)
        }
    }

    @Test
    fun testGetWhoIs() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "user_id": "@peter:rabbit.rocks",
                      "devices": {
                        "teapot": {
                          "sessions": [
                            {
                              "connections": [
                                {
                                  "ip": "127.0.0.1",
                                  "last_seen": 1411996332123,
                                  "user_agent": "curl/7.31.0-DEV"
                                },
                                {
                                  "ip": "10.0.0.2",
                                  "last_seen": 1411996332123,
                                  "user_agent": "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.120 Safari/537.36"
                                }
                              ]
                            }
                          ]
                        }
                      }
                    }
                """)
            }
        }

        val client = MatrixClient(mockEngine, baseUrl)

        run {
            val response = client.adminApi.getWhoIs("@peter:rabbit.rocks")
            assertEquals("@peter:rabbit.rocks", response.userId)
            assertEquals(1, response.devices.size)
            assertEquals(1, response.devices.getValue("teapot").sessions.size)
            assertEquals(2, response.devices.getValue("teapot").sessions[0].connections.size)
            assertEquals("127.0.0.1", response.devices.getValue("teapot").sessions[0].connections[0].ip)
            assertEquals(1411996332123, response.devices.getValue("teapot").sessions[0].connections[0].lastSeen)
            assertEquals("curl/7.31.0-DEV", response.devices.getValue("teapot").sessions[0].connections[0].userAgent)
            assertEquals("10.0.0.2", response.devices.getValue("teapot").sessions[0].connections[1].ip)
            assertEquals(1411996332123, response.devices.getValue("teapot").sessions[0].connections[1].lastSeen)
            assertEquals("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.120 Safari/537.36", response.devices.getValue(
                "teapot"
            ).sessions[0].connections[1].userAgent)
        }
    }

    @Test
    fun testGetEventContext() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "end": "t29-57_2_0_2",
                      "events_after": [
                        {
                          "content": {
                            "body": "This is an example text message",
                            "msgtype": "m.text",
                            "format": "org.matrix.custom.html",
                            "formatted_body": "<b>This is an example text message</b>"
                          },
                          "type": "m.room.message",
                          "event_id": "$143273582443PhrSn:example.org",
                          "room_id": "!636q39766251:example.com",
                          "sender": "@example:example.org",
                          "origin_server_ts": 1432735824653,
                          "unsigned": {
                            "age": 1234
                          }
                        }
                      ],
                      "event": {
                        "content": {
                          "body": "filename.jpg",
                          "info": {
                            "h": 398,
                            "w": 394,
                            "mimetype": "image/jpeg",
                            "size": 31037
                          },
                          "url": "mxc://example.org/JWEIFJgwEIhweiWJE",
                          "msgtype": "m.image"
                        },
                        "type": "m.room.message",
                        "event_id": "${'$'}f3h4d129462ha:example.com",
                        "room_id": "!636q39766251:example.com",
                        "sender": "@example:example.org",
                        "origin_server_ts": 1432735824653,
                        "unsigned": {
                          "age": 1234
                        }
                      },
                      "events_before": [
                        {
                          "content": {
                            "body": "something-important.doc",
                            "filename": "something-important.doc",
                            "info": {
                              "mimetype": "application/msword",
                              "size": 46144
                            },
                            "msgtype": "m.file",
                            "url": "mxc://example.org/FHyPlCeYUSFFxlgbQYZmoEoe"
                          },
                          "type": "m.room.message",
                          "event_id": "$143273582443PhrSn:example.org",
                          "room_id": "!636q39766251:example.com",
                          "sender": "@example:example.org",
                          "origin_server_ts": 1432735824653,
                          "unsigned": {
                            "age": 1234
                          }
                        }
                      ],
                      "start": "t27-54_2_0_2",
                      "state": [
                        {
                          "content": {
                            "creator": "@example:example.org",
                            "room_version": "1",
                            "m.federate": true,
                            "predecessor": {
                              "event_id": "${'$'}something:example.org",
                              "room_id": "!oldroom:example.org"
                            }
                          },
                          "type": "m.room.create",
                          "event_id": "$143273582443PhrSn:example.org",
                          "room_id": "!636q39766251:example.com",
                          "sender": "@example:example.org",
                          "origin_server_ts": 1432735824653,
                          "unsigned": {
                            "age": 1234
                          },
                          "state_key": ""
                        },
                        {
                          "content": {
                            "membership": "join",
                            "avatar_url": "mxc://example.org/SEsfnsuifSDFSSEF",
                            "displayname": "Alice Margatroid"
                          },
                          "type": "m.room.member",
                          "event_id": "$143273582443PhrSn:example.org",
                          "room_id": "!636q39766251:example.com",
                          "sender": "@example:example.org",
                          "origin_server_ts": 1432735824653,
                          "unsigned": {
                            "age": 1234
                          },
                          "state_key": "@alice:example.org"
                        }
                      ]
                    }
                """)
            }
        }

        val client = MatrixClient(mockEngine, baseUrl)

        run {
            val response = client.roomApi.getEventContext("!636q39766251:example.com", "\$f3h4d129462ha:example.com", limit = 3)
            assertEquals("t27-54_2_0_2", response.start)
            assertEquals("t29-57_2_0_2", response.end)
            assertEquals(2, response.state.size)
            assertEquals(1, response.eventsBefore.size)
            assertEquals(1, response.eventsAfter.size)
            assertEquals("m.room.message", response.event?.type)
        }
    }

    @Test
    fun testReportContent() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {}
                """)
            }
        }

        val client = MatrixClient(mockEngine, baseUrl)

        run {
            client.roomApi.reportContent("!637q39766251:example.com", "\$something:example.org", -100, "this makes me sad")
        }
    }

    @Test
    fun testGetProtocols() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "irc": {
                        "user_fields": [
                          "network",
                          "nickname"
                        ],
                        "location_fields": [
                          "network",
                          "channel"
                        ],
                        "icon": "mxc://example.org/aBcDeFgH",
                        "field_types": {
                          "network": {
                            "regexp": "([a-z0-9]+\\.)*[a-z0-9]+",
                            "placeholder": "irc.example.org"
                          },
                          "nickname": {
                            "regexp": "[^\\s]+",
                            "placeholder": "username"
                          },
                          "channel": {
                            "regexp": "#[^\\s]+",
                            "placeholder": "#foobar"
                          }
                        },
                        "instances": [
                          {
                            "network_id": "freenode",
                            "desc": "Freenode",
                            "icon": "mxc://example.org/JkLmNoPq",
                            "fields": {
                              "network": "freenode.net"
                            }
                          }
                        ]
                      },
                      "gitter": {
                        "user_fields": [
                          "username"
                        ],
                        "location_fields": [
                          "room"
                        ],
                        "field_types": {
                          "username": {
                            "regexp": "@[^\\s]+",
                            "placeholder": "@username"
                          },
                          "room": {
                            "regexp": "[^\\s]+\\/[^\\s]+",
                            "placeholder": "matrix-org/matrix-doc"
                          }
                        },
                        "instances": [
                          {
                            "network_id": "gitter",
                            "desc": "Gitter",
                            "icon": "mxc://example.org/zXyWvUt",
                            "fields": {}
                          }
                        ]
                      }
                    }
                """)
            }
        }

        val client = MatrixClient(mockEngine, baseUrl)

        run {
            val response = client.thirdPartyApi.getProtocols()
            assertEquals(2, response.size)
            assertEquals("network", response["irc"]!!.userFields[0])
            assertEquals("nickname", response["irc"]!!.userFields[1])
            assertEquals("mxc://example.org/aBcDeFgH", response["irc"]!!.icon)
        }
    }

    @Test
    fun testGetProtocolMetadata() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "user_fields": [
                        "network",
                        "nickname"
                      ],
                      "location_fields": [
                        "network",
                        "channel"
                      ],
                      "icon": "mxc://example.org/aBcDeFgH",
                      "field_types": {
                        "network": {
                          "regexp": "([a-z0-9]+\\.)*[a-z0-9]+",
                          "placeholder": "irc.example.org"
                        },
                        "nickname": {
                          "regexp": "[^\\s#]+",
                          "placeholder": "username"
                        },
                        "channel": {
                          "regexp": "#[^\\s]+",
                          "placeholder": "#foobar"
                        }
                      },
                      "instances": [
                        {
                          "desc": "Freenode",
                          "icon": "mxc://example.org/JkLmNoPq",
                          "fields": {
                            "network": "freenode"
                          },
                          "network_id": "freenode"
                        }
                      ]
                    }
                """)
            }
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "errcode": "M_NOT_FOUND"
                    }
                """, HttpStatusCode.NotFound)
            }
        }

        val client = MatrixClient(mockEngine, baseUrl)

        run {
            val response = client.thirdPartyApi.getProtocolMetadata("irc")
            assertEquals(2, response.userFields.size)
            assertEquals(2, response.locationFields.size)
            assertEquals(3, response.fieldTypes.size)
            assertEquals(1, response.instances.size)
        }
        // run {
        //     val error = assertFailsWith<MatrixError.NotFound> {
        //         client.thirdPartyApi.getProtocolMetadata("irc")
        //     }
        //     assertEquals("TODO: Wasn't in spec for some reason.", error.error)
        // }
    }

    @Test
    fun testQueryLocationByProtocol() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    [
                      {
                        "alias": "#freenode_#matrix:matrix.org",
                        "protocol": "irc",
                        "fields": {
                          "network": "freenode",
                          "channel": "#matrix"
                        }
                      }
                    ]
                """)
            }
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "errcode": "M_NOT_FOUND"
                    }
                """, HttpStatusCode.NotFound)
            }
        }

        val client = MatrixClient(mockEngine, baseUrl)

        run {
            val response = client.thirdPartyApi.queryLocationByProtocol("irc")
            assertEquals(1, response.size)
            assertEquals("#freenode_#matrix:matrix.org", response[0].alias)
            assertEquals("irc", response[0].protocol)
            assertEquals(2, response[0].fields.size)
            assertEquals("freenode", response[0].fields["network"]!!.jsonPrimitive.content)
            assertEquals("#matrix", response[0].fields["channel"]!!.jsonPrimitive.content)
        }
        // run {
        //     val error = assertFailsWith<MatrixError.NotFound> {
        //         client.thirdPartyApi.queryLocationByProtocol("irc")
        //     }
        //     assertEquals("TODO: Wasn't in spec for some reason.", error.error)
        // }
    }

    @Test
    fun testQueryUserByProtocol() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    [
                      {
                        "userid": "@_gitter_jim:matrix.org",
                        "protocol": "gitter",
                        "fields": {
                          "user": "jim"
                        }
                      }
                    ]
                """)
            }
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "errcode": "M_NOT_FOUND"
                    }
                """, HttpStatusCode.NotFound)
            }
        }

        val client = MatrixClient(mockEngine, baseUrl)

        // GET /_matrix/client/r0/thirdparty/user/irc HTTP/1.1

        run {
            val response = client.thirdPartyApi.queryUserByProtocol("irc")
            assertEquals(1, response.size)
            assertEquals("@_gitter_jim:matrix.org", response[0].userId)
            assertEquals("gitter", response[0].protocol)
            assertEquals(1, response[0].fields.size)
        }
        // run {
        //     val error = assertFailsWith<MatrixError.NotFound> {
        //         client.thirdPartyApi.queryUserByProtocol("irc")
        //     }
        //     assertEquals("TODO: Wasn't in spec for some reason.", error.error)
        // }
    }

    @Test
    fun testQueryLocationByAlias() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    [
                      {
                        "alias": "#freenode_#matrix:matrix.org",
                        "protocol": "irc",
                        "fields": {
                          "network": "freenode",
                          "channel": "#matrix"
                        }
                      }
                    ]
                """)
            }
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "errcode": "M_NOT_FOUND"
                    }
                """, HttpStatusCode.NotFound)
            }
        }

        val client = MatrixClient(mockEngine, baseUrl)

        run {
            val response = client.thirdPartyApi.queryLocationByAlias(alias = "#matrix:matrix.org")
            assertEquals(1, response.size)
            assertEquals("#freenode_#matrix:matrix.org", response[0].alias)
            assertEquals("irc", response[0].protocol)
            assertEquals(2, response[0].fields.size)
        }
        // run {
        //     val error = assertFailsWith<MatrixError.NotFound> {
        //         client.thirdPartyApi.queryLocationByAlias(alias = "#matrix:matrix.org")
        //     }
        //     assertEquals("TODO: Wasn't in spec for some reason.", error.error)
        // }
    }

    @Test
    fun testQueryUserByID() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    [
                      {
                        "userid": "@_gitter_jim:matrix.org",
                        "protocol": "gitter",
                        "fields": {
                          "user": "jim"
                        }
                      }
                    ]
                """)
            }
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "errcode": "M_NOT_FOUND"
                    }
                """, HttpStatusCode.NotFound)
            }
        }

        val client = MatrixClient(mockEngine, baseUrl)

        run {
            val response = client.thirdPartyApi.queryUserByID(userId = "@bob:matrix.org")
            assertEquals(1, response.size)
            assertEquals("@_gitter_jim:matrix.org", response[0].userId)
            assertEquals("gitter", response[0].protocol)
            assertEquals(1, response[0].fields.size)
        }
        // run {
        //     val error = assertFailsWith<MatrixError.NotFound> {
        //         client.thirdPartyApi.queryUserByID(userId = "@bob:matrix.org")
        //     }
        //     assertEquals("TODO: Wasn't in spec for some reason.", error.error)
        // }
    }

    @Test
    fun testRequestOpenId() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "access_token": "SomeT0kenHere",
                      "token_type": "Bearer",
                      "matrix_server_name": "example.com",
                      "expires_in": 3600
                    }
                """)
            }
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
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

        val client = MatrixClient(mockEngine, baseUrl)

        run {
            val response = client.userApi.requestOpenIdToken("@alice:example.com")
            assertEquals("SomeT0kenHere", response.accessToken)
            assertEquals("Bearer", response.tokenType)
            assertEquals("example.com", response.matrixServerName)
            assertEquals(3600, response.expiresIn)
        }
        run {
            val error = assertFailsWith<MatrixError.LimitExceeded> {
                client.userApi.requestOpenIdToken("@alice:example.com")
            }
            assertEquals("Too many requests", error.error)
            assertEquals(2000, error.retryAfterMillis)
        }
    }

    @Test
    fun testRoomUpgrade() = runSuspendTest {
        val mockEngine = MockEngine.create {
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "replacement_room": "!newroom:example.org"
                    }
                """)
            }
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "errcode": "M_UNSUPPORTED_ROOM_VERSION",
                      "error": "This server does not support that room version"
                    }
                """, HttpStatusCode.BadRequest)
            }
            addHandler {
            	assertTrue(it.url.toString().startsWith(baseUrl.toString()))
                // language=json
                respondJson("""
                    {
                      "errcode": "M_FORBIDDEN",
                      "error": "You cannot upgrade this room"
                    }
                """, HttpStatusCode.Forbidden)
            }
        }

        val client = MatrixClient(mockEngine, baseUrl)

        run {
            val response = client.roomApi.upgradeRoom("!oldroom:example.org", "2")
            assertEquals("!newroom:example.org", response)
        }
        run {
            val error = assertFailsWith<MatrixError.UnsupportedRoomVersion> {
                client.roomApi.upgradeRoom("!oldroom@example.org", "2")
            }
            assertEquals("This server does not support that room version", error.error)
        }
        run {
            val error = assertFailsWith<MatrixError.Forbidden> {
                client.roomApi.upgradeRoom("!oldroom@example.org", "2")
            }
            assertEquals("You cannot upgrade this room", error.error)
        }
    }
}
