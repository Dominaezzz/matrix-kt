package io.github.matrixkt

import io.github.matrixkt.models.Presence
import io.github.matrixkt.models.events.*
import io.github.matrixkt.models.events.contents.*
import io.github.matrixkt.models.events.contents.RoomKeyRequestContent
import io.github.matrixkt.models.events.contents.call.AnswerContent
import io.github.matrixkt.models.events.contents.call.CandidatesContent
import io.github.matrixkt.models.events.contents.call.HangupContent
import io.github.matrixkt.models.events.contents.call.InviteContent
import io.github.matrixkt.models.events.contents.key.verification.AcceptContent
import io.github.matrixkt.models.events.contents.key.verification.CancelContent
import io.github.matrixkt.models.events.contents.key.verification.KeyContent
import io.github.matrixkt.models.events.contents.key.verification.MacContent
import io.github.matrixkt.models.events.contents.key.verification.RequestContent
import io.github.matrixkt.models.events.contents.key.verification.StartContent
import io.github.matrixkt.models.events.contents.policy.rule.RoomContent
import io.github.matrixkt.models.events.contents.policy.rule.ServerContent
import io.github.matrixkt.models.events.contents.policy.rule.UserContent
import io.github.matrixkt.models.events.contents.room.*
import io.github.matrixkt.models.events.contents.room.message.FeedbackContent
import io.github.matrixkt.utils.MatrixJson
import kotlinx.serialization.json.*
import kotlin.test.Test
import kotlin.test.assertEquals

class EventTypeTests {
    @Test
    fun testAcceptedTermsEvent() {
        // language=json
        val json = """
                {
                    "content": {
                        "accepted": [
                            "https://example.org/somewhere/terms-1.2-en.html",
                            "https://example.org/somewhere/privacy-1.2-en.html"
                        ]
                    },
                    "type": "m.accepted_terms"
                }
                """.trimIndent()

        val serializer = AccountEvent.serializer(AcceptedTermsContent.serializer())
        val event = MatrixJson.decodeFromString(serializer, json)

        assertEquals(2, event.content.accepted.size)
        assertEquals("https://example.org/somewhere/terms-1.2-en.html", event.content.accepted[0])
        assertEquals("https://example.org/somewhere/privacy-1.2-en.html", event.content.accepted[1])
        assertEquals("m.accepted_terms", event.type)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, event))
    }

    @Test
    fun testCallAnswerEvent() {
        // language=json
        val json = """
                {
                    "content": {
                        "version": 0,
                        "call_id": "12345",
                        "lifetime": 60000,
                        "answer": {
                            "type": "answer",
                            "sdp": "v=0\r\no=- 6584580628695956864 2 IN IP4 127.0.0.1[...]"
                        }
                    },
                    "type": "m.call.answer",
                    "event_id": "${'$'}143273582443PhrSn:example.org",
                    "room_id": "!jEsUZKDJdhlrceRyVU:example.org",
                    "sender": "@example:example.org",
                    "origin_server_ts": 1432735824653,
                    "unsigned": {
                        "age": 1234
                    }
                }
                """.trimIndent()

        val serializer = MessageEvent.serializer(AnswerContent.serializer())
        val event = MatrixJson.decodeFromString(serializer, json)

        assertEquals(0, event.content.version)
        assertEquals("12345", event.content.callId)
        // assertEquals(60000, event.content.lifetime)
        assertEquals("answer", event.content.answer.type)
        assertEquals("v=0\r\no=- 6584580628695956864 2 IN IP4 127.0.0.1[...]",
            event.content.answer.sdp)
        assertEquals("m.call.answer", event.type)
        assertEquals("$143273582443PhrSn:example.org", event.eventId)
        assertEquals("!jEsUZKDJdhlrceRyVU:example.org", event.roomId)
        assertEquals("@example:example.org", event.sender)
        assertEquals(1432735824653, event.originServerTimestamp)
        assertEquals(1234, event.unsigned?.age)

        // FIXME assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, event))
        // This fails because of `lifetime` field.
    }

    @Test
    fun testCallCandidatesEvent() {
        // language=json
        val json = """
                {
                    "content": {
                        "version": 0,
                        "call_id": "12345",
                        "candidates": [
                            {
                                "sdpMid": "audio",
                                "sdpMLineIndex": 0,
                                "candidate": "candidate:863018703 1 udp 2122260223 10.9.64.156 43670 typ host generation 0"
                            }
                        ]
                    },
                    "type": "m.call.candidates",
                    "event_id": "${'$'}143273582443PhrSn:example.org",
                    "room_id": "!jEsUZKDJdhlrceRyVU:example.org",
                    "sender": "@example:example.org",
                    "origin_server_ts": 1432735824653,
                    "unsigned": {
                        "age": 1234
                    }
                }
                """.trimIndent()

        val serializer = MessageEvent.serializer(CandidatesContent.serializer())
        val event = MatrixJson.decodeFromString(serializer, json)

        assertEquals(0, event.content.version)
        assertEquals("12345", event.content.callId)
        assertEquals(1, event.content.candidates.size)
        assertEquals("audio", event.content.candidates[0].sdpMid)
        assertEquals(0, event.content.candidates[0].sdpMLineIndex)
        assertEquals("candidate:863018703 1 udp 2122260223 10.9.64.156 43670 typ host generation 0",
            event.content.candidates[0].candidate)
        assertEquals("m.call.candidates", event.type)
        assertEquals("$143273582443PhrSn:example.org", event.eventId)
        assertEquals("!jEsUZKDJdhlrceRyVU:example.org", event.roomId)
        assertEquals("@example:example.org", event.sender)
        assertEquals(1432735824653, event.originServerTimestamp)
        assertEquals(1234, event.unsigned?.age)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, event))
    }

    @Test
    fun testCallHangupEvent() {
        // language=json
        val json = """
                {
                    "content": {
                        "version": 0,
                        "call_id": "12345"
                    },
                    "type": "m.call.hangup",
                    "event_id": "${'$'}143273582443PhrSn:example.org",
                    "room_id": "!jEsUZKDJdhlrceRyVU:example.org",
                    "sender": "@example:example.org",
                    "origin_server_ts": 1432735824653,
                    "unsigned": {
                        "age": 1234
                    }
                }
                """.trimIndent()

        val serializer = MessageEvent.serializer(HangupContent.serializer())
        val event = MatrixJson.decodeFromString(serializer, json)

        assertEquals(0, event.content.version)
        assertEquals("12345", event.content.callId)
        assertEquals("m.call.hangup", event.type)
        assertEquals("$143273582443PhrSn:example.org", event.eventId)
        assertEquals("!jEsUZKDJdhlrceRyVU:example.org", event.roomId)
        assertEquals("@example:example.org", event.sender)
        assertEquals(1432735824653, event.originServerTimestamp)
        assertEquals(1234, event.unsigned?.age)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, event))
    }

    @Test
    fun testCallInviteEvent() {
        // language=json
        val json = """
                {
                    "content": {
                        "version": 0,
                        "call_id": "12345",
                        "lifetime": 60000,
                        "offer": {
                            "type": "offer",
                            "sdp": "v=0\r\no=- 6584580628695956864 2 IN IP4 127.0.0.1[...]"
                        }
                    },
                    "type": "m.call.invite",
                    "event_id": "${'$'}143273582443PhrSn:example.org",
                    "room_id": "!jEsUZKDJdhlrceRyVU:example.org",
                    "sender": "@example:example.org",
                    "origin_server_ts": 1432735824653,
                    "unsigned": {
                        "age": 1234
                    }
                }
                """.trimIndent()

        val serializer = MessageEvent.serializer(InviteContent.serializer())
        val event = MatrixJson.decodeFromString(serializer, json)

        assertEquals(0, event.content.version)
        assertEquals("12345", event.content.callId)
        assertEquals(60000, event.content.lifetime)
        assertEquals("offer", event.content.offer.type)
        assertEquals("v=0\r\no=- 6584580628695956864 2 IN IP4 127.0.0.1[...]",
            event.content.offer.sdp)
        assertEquals("m.call.invite", event.type)
        assertEquals("$143273582443PhrSn:example.org", event.eventId)
        assertEquals("!jEsUZKDJdhlrceRyVU:example.org", event.roomId)
        assertEquals("@example:example.org", event.sender)
        assertEquals(1432735824653, event.originServerTimestamp)
        assertEquals(1234, event.unsigned?.age)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, event))
    }

    @Test
    fun testDirectEvent() {
        // language=json
        val json = """
                {
                    "content": {
                        "@bob:example.com": [
                            "!abcdefgh:example.com",
                            "!hgfedcba:example.com"
                        ]
                    },
                    "type": "m.direct"
                }
                """.trimIndent()

        val serializer = AccountEvent.serializer(DirectContent.serializer())
        val event = MatrixJson.decodeFromString(serializer, json)

        assertEquals(2, event.content["@bob:example.com"]!!.size)
        assertEquals("!abcdefgh:example.com", event.content["@bob:example.com"]!![0])
        assertEquals("!hgfedcba:example.com", event.content["@bob:example.com"]!![1])
        assertEquals("m.direct", event.type)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, event))
    }

    @Test
    fun testDummyEvent() {
        // language=json
        val json = """
                {
                    "content": {
                    },
                    "type": "m.dummy"
                }
                """.trimIndent()

        val serializer = AccountEvent.serializer(DummyContent.serializer())
        val event = MatrixJson.decodeFromString(serializer, json)

        assertEquals("m.dummy", event.type)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, event))
    }

    @Test
    fun testForwardedRoomKeyEvent() {
        // language=json
        val json = """
                {
                    "content": {
                        "algorithm": "m.megolm.v1.aes-sha2",
                        "room_id": "!Cuyf34gef24t:localhost",
                        "session_id": "X3lUlvLELLYxeTx4yOVu6UDpasGEVO0Jbu+QFnm0cKQ",
                        "session_key": "AgAAAADxKHa9uFxcXzwYoNueL5Xqi69IkD4sni8Llf...",
                        "sender_key": "RF3s+E7RkTQTGF2d8Deol0FkQvgII2aJDf3/Jp5mxVU",
                        "sender_claimed_ed25519_key": "aj40p+aw64yPIdsxoog8jhPu9i7l7NcFRecuOQblE3Y",
                        "forwarding_curve25519_key_chain": [
                            "hPQNcabIABgGnx3/ACv/jmMmiQHoeFfuLB17tzWp6Hw"
                        ]
                    },
                    "type": "m.forwarded_room_key"
                }
                """.trimIndent()

        val serializer = AccountEvent.serializer(ForwardedRoomKeyContent.serializer())
        val event = MatrixJson.decodeFromString(serializer, json)

        assertEquals("m.megolm.v1.aes-sha2", event.content.algorithm)
        assertEquals("!Cuyf34gef24t:localhost", event.content.roomId)
        assertEquals("X3lUlvLELLYxeTx4yOVu6UDpasGEVO0Jbu+QFnm0cKQ", event.content.sessionId)
        assertEquals("AgAAAADxKHa9uFxcXzwYoNueL5Xqi69IkD4sni8Llf...", event.content.sessionKey)
        assertEquals("RF3s+E7RkTQTGF2d8Deol0FkQvgII2aJDf3/Jp5mxVU", event.content.senderKey)
        assertEquals("aj40p+aw64yPIdsxoog8jhPu9i7l7NcFRecuOQblE3Y",
            event.content.senderClaimedEd25519Key)
        assertEquals(1, event.content.forwardingCurve25519KeyChain.size)
        assertEquals("hPQNcabIABgGnx3/ACv/jmMmiQHoeFfuLB17tzWp6Hw",
            event.content.forwardingCurve25519KeyChain[0])
        assertEquals("m.forwarded_room_key", event.type)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, event))
    }

    @Test
    fun testFullyReadEvent() {
        // language=json
        val json = """
                {
                    "content": {
                        "event_id": "${'$'}someplace:example.org"
                    },
                    "type": "m.fully_read",
                    "room_id": "!somewhere:example.org"
                }
                """.trimIndent()

        val serializer = AccountEvent.serializer(FullyReadContent.serializer())
        val event = MatrixJson.decodeFromString(serializer, json)

        assertEquals("\$someplace:example.org", event.content.eventId)
        assertEquals("m.fully_read", event.type)
        // FIXME assertEquals("!somewhere:example.org", event.roomId)
        //
        // FIXME assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, event))
        // Needs AccountEvent class to have optional room_id
    }

    @Test
    fun testIdentityServerEvent() {
        // language=json
        val json = """
                {
                    "content": {
                        "base_url": "https://example.org"
                    },
                    "type": "m.identity_server"
                }
                """.trimIndent()

        val serializer = AccountEvent.serializer(IdentityServerContent.serializer())
        val event = MatrixJson.decodeFromString(serializer, json)

        assertEquals("https://example.org", event.content.baseUrl)
        assertEquals("m.identity_server", event.type)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, event))
    }

    @Test
    fun testIgnoredUserListEvent() {
        // language=json
        val json = """
                {
                    "content": {
                        "ignored_users": {
                            "@someone:example.org": {
                            }
                        }
                    },
                    "type": "m.ignored_user_list"
                }
                """.trimIndent()

        val serializer = AccountEvent.serializer(IgnoredUserListContent.serializer())
        val event = MatrixJson.decodeFromString(serializer, json)

        assertEquals("m.ignored_user_list", event.type)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, event))
    }

    @Test
    fun testKeyVerificationAcceptEvent() {
        // language=json
        val json = """
                {
                    "type": "m.key.verification.accept",
                    "content": {
                        "transaction_id": "S0meUniqueAndOpaqueString",
                        "method": "m.sas.v1",
                        "key_agreement_protocol": "curve25519",
                        "hash": "sha256",
                        "message_authentication_code": "hkdf-hmac-sha256",
                        "short_authentication_string": [
                            "decimal",
                            "emoji"
                        ],
                        "commitment": "fQpGIW1Snz+pwLZu6sTy2aHy/DYWWTspTJRPyNp0PKkymfIsNffysMl6ObMMFdIJhk6g6pwlIqZ54rxo8SLmAg"
                    }
                }
                """.trimIndent()

        val serializer = AccountEvent.serializer(AcceptContent.serializer())
        val event = MatrixJson.decodeFromString(serializer, json)

        assertEquals("m.key.verification.accept", event.type)
        assertEquals("S0meUniqueAndOpaqueString", event.content.transactionId)
        val eventContent = event.content as AcceptContent.SasV1 // assertEquals("m.sas.v1", event.content.method)
        assertEquals("curve25519", eventContent.keyAgreementProtocol)
        assertEquals("sha256", eventContent.hash)
        assertEquals("hkdf-hmac-sha256", eventContent.messageAuthenticationCode)
        assertEquals(2, eventContent.shortAuthenticationString.size)
        assertEquals("decimal", eventContent.shortAuthenticationString[0])
        assertEquals("emoji", eventContent.shortAuthenticationString[1])
        assertEquals("fQpGIW1Snz+pwLZu6sTy2aHy/DYWWTspTJRPyNp0PKkymfIsNffysMl6ObMMFdIJhk6g6pwlIqZ54rxo8SLmAg",
            eventContent.commitment)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, event))
    }

    @Test
    fun testKeyVerificationCancelEvent() {
        // language=json
        val json = """
                {
                    "type": "m.key.verification.cancel",
                    "content": {
                        "transaction_id": "S0meUniqueAndOpaqueString",
                        "code": "m.user",
                        "reason": "User rejected the key verification request"
                    }
                }
                """.trimIndent()

        val serializer = AccountEvent.serializer(CancelContent.serializer())
        val event = MatrixJson.decodeFromString(serializer, json)

        assertEquals("m.key.verification.cancel", event.type)
        assertEquals("S0meUniqueAndOpaqueString", event.content.transactionId)
        assertEquals("m.user", event.content.code)
        assertEquals("User rejected the key verification request", event.content.reason)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, event))
    }

    @Test
    fun testKeyVerificationKeyEvent() {
        // language=json
        val json = """
                {
                    "type": "m.key.verification.key",
                    "content": {
                        "transaction_id": "S0meUniqueAndOpaqueString",
                        "key": "fQpGIW1Snz+pwLZu6sTy2aHy/DYWWTspTJRPyNp0PKkymfIsNffysMl6ObMMFdIJhk6g6pwlIqZ54rxo8SLmAg"
                    }
                }
                """.trimIndent()

        val serializer = AccountEvent.serializer(KeyContent.serializer())
        val event = MatrixJson.decodeFromString(serializer, json)

        assertEquals("m.key.verification.key", event.type)
        assertEquals("S0meUniqueAndOpaqueString", event.content.transactionId)
        assertEquals("fQpGIW1Snz+pwLZu6sTy2aHy/DYWWTspTJRPyNp0PKkymfIsNffysMl6ObMMFdIJhk6g6pwlIqZ54rxo8SLmAg",
            event.content.key)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, event))
    }

    @Test
    fun testKeyVerificationMacEvent() {
        // language=json
        val json = """
                {
                    "type": "m.key.verification.mac",
                    "content": {
                        "transaction_id": "S0meUniqueAndOpaqueString",
                        "keys": "2Wptgo4CwmLo/Y8B8qinxApKaCkBG2fjTWB7AbP5Uy+aIbygsSdLOFzvdDjww8zUVKCmI02eP9xtyJxc/cLiBA",
                        "mac": {
                            "ed25519:ABCDEF": "fQpGIW1Snz+pwLZu6sTy2aHy/DYWWTspTJRPyNp0PKkymfIsNffysMl6ObMMFdIJhk6g6pwlIqZ54rxo8SLmAg"
                        }
                    }
                }
                """.trimIndent()

        val serializer = AccountEvent.serializer(MacContent.serializer())
        val event = MatrixJson.decodeFromString(serializer, json)

        assertEquals("m.key.verification.mac", event.type)
        assertEquals("S0meUniqueAndOpaqueString", event.content.transactionId)
        assertEquals("2Wptgo4CwmLo/Y8B8qinxApKaCkBG2fjTWB7AbP5Uy+aIbygsSdLOFzvdDjww8zUVKCmI02eP9xtyJxc/cLiBA",
            event.content.keys)
        assertEquals("fQpGIW1Snz+pwLZu6sTy2aHy/DYWWTspTJRPyNp0PKkymfIsNffysMl6ObMMFdIJhk6g6pwlIqZ54rxo8SLmAg",
            event.content.mac["ed25519:ABCDEF"]!!)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, event))
    }

    @Test
    fun testKeyVerificationRequestEvent() {
        // language=json
        val json = """
                {
                    "type": "m.key.verification.request",
                    "content": {
                        "from_device": "AliceDevice2",
                        "transaction_id": "S0meUniqueAndOpaqueString",
                        "methods": [
                            "m.sas.v1"
                        ],
                        "timestamp": 1559598944869
                    }
                }
                """.trimIndent()

        val serializer = AccountEvent.serializer(RequestContent.serializer())
        val event = MatrixJson.decodeFromString(serializer, json)

        assertEquals("m.key.verification.request", event.type)
        assertEquals("AliceDevice2", event.content.fromDevice)
        assertEquals("S0meUniqueAndOpaqueString", event.content.transactionId)
        assertEquals(1, event.content.methods.size)
        assertEquals("m.sas.v1", event.content.methods[0])
        assertEquals(1559598944869, event.content.timestamp)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, event))
    }

    @Test
    fun testKeyVerificationStartSasV1Event() {
        // language=json
        val json = """
                {
                    "type": "m.key.verification.start",
                    "content": {
                        "from_device": "BobDevice1",
                        "transaction_id": "S0meUniqueAndOpaqueString",
                        "method": "m.sas.v1",
                        "key_agreement_protocols": [
                            "curve25519"
                        ],
                        "hashes": [
                            "sha256"
                        ],
                        "message_authentication_codes": [
                            "hkdf-hmac-sha256"
                        ],
                        "short_authentication_string": [
                            "decimal",
                            "emoji"
                        ]
                    }
                }
                """.trimIndent()

        val serializer = AccountEvent.serializer(StartContent.SasV1.serializer())
        val event = MatrixJson.decodeFromString(serializer, json)

        assertEquals("m.key.verification.start", event.type)
        assertEquals("BobDevice1", event.content.fromDevice)
        assertEquals("S0meUniqueAndOpaqueString", event.content.transactionId)
        // assertEquals("m.sas.v1", event.content.method)
        assertEquals(1, event.content.keyAgreementProtocols.size)
        assertEquals("curve25519", event.content.keyAgreementProtocols[0])
        assertEquals(1, event.content.hashes.size)
        assertEquals("sha256", event.content.hashes[0])
        assertEquals(1, event.content.messageAuthenticationCodes.size)
        assertEquals("hkdf-hmac-sha256", event.content.messageAuthenticationCodes[0])
        assertEquals(2, event.content.shortAuthenticationString.size)
        assertEquals("decimal", event.content.shortAuthenticationString[0])
        assertEquals("emoji", event.content.shortAuthenticationString[1])

        val workaround = AccountEvent.serializer(StartContent.serializer())
        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(workaround, event))
    }

    @Test
    fun testPolicyRuleRoomEvent() {
        // language=json
        val json = """
                {
                    "content": {
                        "entity": "#*:example.org",
                        "recommendation": "m.ban",
                        "reason": "undesirable content"
                    },
                    "type": "m.policy.rule.room",
                    "event_id": "${'$'}143273582443PhrSn:example.org",
                    "room_id": "!jEsUZKDJdhlrceRyVU:example.org",
                    "sender": "@example:example.org",
                    "origin_server_ts": 1432735824653,
                    "unsigned": {
                        "age": 1234
                    },
                    "state_key": "rule:#*:example.org"
                }
                """.trimIndent()

        val serializer = StateEvent.serializer(RoomContent.serializer())
        val event = MatrixJson.decodeFromString(serializer, json)

        assertEquals("#*:example.org", event.content.entity)
        assertEquals("m.ban", event.content.recommendation)
        assertEquals("undesirable content", event.content.reason)
        assertEquals("m.policy.rule.room", event.type)
        assertEquals("$143273582443PhrSn:example.org", event.eventId)
        assertEquals("!jEsUZKDJdhlrceRyVU:example.org", event.roomId)
        assertEquals("@example:example.org", event.sender)
        assertEquals(1432735824653, event.originServerTimestamp)
        assertEquals(1234, event.unsigned?.age)
        assertEquals("rule:#*:example.org", event.stateKey)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, event))
    }

    @Test
    fun testPolicyRuleServerEvent() {
        // language=json
        val json = """
                {
                    "content": {
                        "entity": "*.example.org",
                        "recommendation": "m.ban",
                        "reason": "undesirable engagement"
                    },
                    "type": "m.policy.rule.server",
                    "event_id": "${'$'}143273582443PhrSn:example.org",
                    "room_id": "!jEsUZKDJdhlrceRyVU:example.org",
                    "sender": "@example:example.org",
                    "origin_server_ts": 1432735824653,
                    "unsigned": {
                        "age": 1234
                    },
                    "state_key": "rule:*.example.org"
                }
                """.trimIndent()

        val serializer = StateEvent.serializer(ServerContent.serializer())
        val event = MatrixJson.decodeFromString(serializer, json)

        assertEquals("*.example.org", event.content.entity)
        assertEquals("m.ban", event.content.recommendation)
        assertEquals("undesirable engagement", event.content.reason)
        assertEquals("m.policy.rule.server", event.type)
        assertEquals("$143273582443PhrSn:example.org", event.eventId)
        assertEquals("!jEsUZKDJdhlrceRyVU:example.org", event.roomId)
        assertEquals("@example:example.org", event.sender)
        assertEquals(1432735824653, event.originServerTimestamp)
        assertEquals(1234, event.unsigned?.age)
        assertEquals("rule:*.example.org", event.stateKey)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, event))
    }

    @Test
    fun testPolicyRuleUserEvent() {
        // language=json
        val json = """
                {
                    "content": {
                        "entity": "@alice*:example.org",
                        "recommendation": "m.ban",
                        "reason": "undesirable behaviour"
                    },
                    "type": "m.policy.rule.user",
                    "event_id": "${'$'}143273582443PhrSn:example.org",
                    "room_id": "!jEsUZKDJdhlrceRyVU:example.org",
                    "sender": "@example:example.org",
                    "origin_server_ts": 1432735824653,
                    "unsigned": {
                        "age": 1234
                    },
                    "state_key": "rule:@alice*:example.org"
                }
                """.trimIndent()

        val serializer = StateEvent.serializer(UserContent.serializer())
        val event = MatrixJson.decodeFromString(serializer, json)

        assertEquals("@alice*:example.org", event.content.entity)
        assertEquals("m.ban", event.content.recommendation)
        assertEquals("undesirable behaviour", event.content.reason)
        assertEquals("m.policy.rule.user", event.type)
        assertEquals("$143273582443PhrSn:example.org", event.eventId)
        assertEquals("!jEsUZKDJdhlrceRyVU:example.org", event.roomId)
        assertEquals("@example:example.org", event.sender)
        assertEquals(1432735824653, event.originServerTimestamp)
        assertEquals(1234, event.unsigned?.age)
        assertEquals("rule:@alice*:example.org", event.stateKey)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, event))
    }

    @Test
    fun testPresenceEvent() {
        // language=json
        val json = """
                {
                    "content": {
                        "avatar_url": "mxc://localhost:wefuiwegh8742w",
                        "last_active_ago": 2478593,
                        "presence": "online",
                        "currently_active": false,
                        "status_msg": "Making cupcakes"
                    },
                    "type": "m.presence",
                    "sender": "@example:localhost"
                }
                """.trimIndent()

        val serializer = EphemeralEvent.serializer(PresenceContent.serializer())
        val event = MatrixJson.decodeFromString(serializer, json)

        assertEquals("mxc://localhost:wefuiwegh8742w", event.content.avatarUrl)
        assertEquals(2478593, event.content.lastActiveAgo)
        assertEquals(Presence.ONLINE, event.content.presence)
        assertEquals(false, event.content.currentlyActive)
        assertEquals("Making cupcakes", event.content.statusMessage)
        assertEquals("m.presence", event.type)
        assertEquals("@example:localhost", event.sender)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, event))
    }

    @Test
    fun testPushRulesEvent() {
        // language=json
        val json = """
                {
                    "content": {
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
                                    "conditions": [
                                    ],
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
                            "room": [
                            ],
                            "sender": [
                            ],
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
                                            "kind": "room_member_count",
                                            "is": "2"
                                        },
                                        {
                                            "kind": "event_match",
                                            "key": "type",
                                            "pattern": "m.room.message"
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
                    },
                    "type": "m.push_rules"
                }
                """.trimIndent()

        val serializer = AccountEvent.serializer(PushRulesContent.serializer())
        val event = MatrixJson.decodeFromString(serializer, json)

        assertEquals(1, event.content.global!!.content.size)
        assertEquals(3, event.content.global!!.content[0].actions.size)
        assertEquals("notify", event.content.global!!.content[0].actions[0].jsonPrimitive.content)
        assertEquals("sound", event.content.global!!.content[0].actions[1].jsonObject["set_tweak"]!!.jsonPrimitive.content)
        assertEquals("default", event.content.global!!.content[0].actions[1].jsonObject["value"]!!.jsonPrimitive.content)
        assertEquals("highlight", event.content.global!!.content[0].actions[2].jsonObject["set_tweak"]!!.jsonPrimitive.content)
        assertEquals(true, event.content.global!!.content[0].default)
        assertEquals(true, event.content.global!!.content[0].enabled)
        assertEquals("alice", event.content.global!!.content[0].pattern)
        assertEquals(".m.rule.contains_user_name", event.content.global!!.content[0].ruleId)
        assertEquals(2, event.content.global!!.override.size)
        assertEquals(1, event.content.global!!.override[0].actions.size)
        assertEquals("dont_notify", event.content.global!!.override[0].actions[0].jsonPrimitive.content)
        assertEquals(0, event.content.global!!.override[0].conditions.size)
        assertEquals(true, event.content.global!!.override[0].default)
        assertEquals(false, event.content.global!!.override[0].enabled)
        assertEquals(".m.rule.master", event.content.global!!.override[0].ruleId)
        assertEquals(1, event.content.global!!.override[1].actions.size)
        assertEquals("dont_notify", event.content.global!!.override[1].actions[0].jsonPrimitive.content)
        assertEquals(1, event.content.global!!.override[1].conditions.size)
        assertEquals("content.msgtype", event.content.global!!.override[1].conditions[0].key)
        assertEquals("event_match", event.content.global!!.override[1].conditions[0].kind)
        assertEquals("m.notice", event.content.global!!.override[1].conditions[0].pattern)
        assertEquals(true, event.content.global!!.override[1].default)
        assertEquals(true, event.content.global!!.override[1].enabled)
        assertEquals(".m.rule.suppress_notices", event.content.global!!.override[1].ruleId)
        assertEquals(0, event.content.global!!.room.size)
        assertEquals(0, event.content.global!!.sender.size)
        assertEquals(6, event.content.global!!.underride.size)
        assertEquals(3, event.content.global!!.underride[0].actions.size)
        assertEquals("notify", event.content.global!!.underride[0].actions[0].jsonPrimitive.content)
        assertEquals("sound", event.content.global!!.underride[0].actions[1].jsonObject["set_tweak"]!!.jsonPrimitive.content)
        assertEquals("ring", event.content.global!!.underride[0].actions[1].jsonObject["value"]!!.jsonPrimitive.content)
        assertEquals("highlight", event.content.global!!.underride[0].actions[2].jsonObject["set_tweak"]!!.jsonPrimitive.content)
        assertEquals(false, event.content.global!!.underride[0].actions[2].jsonObject["value"]!!.jsonPrimitive.boolean)
        assertEquals(1, event.content.global!!.underride[0].conditions.size)
        assertEquals("type", event.content.global!!.underride[0].conditions[0].key)
        assertEquals("event_match", event.content.global!!.underride[0].conditions[0].kind)
        assertEquals("m.call.invite", event.content.global!!.underride[0].conditions[0].pattern)
        assertEquals(true, event.content.global!!.underride[0].default)
        assertEquals(true, event.content.global!!.underride[0].enabled)
        assertEquals(".m.rule.call", event.content.global!!.underride[0].ruleId)
        assertEquals(3, event.content.global!!.underride[1].actions.size)
        assertEquals("notify", event.content.global!!.underride[1].actions[0].jsonPrimitive.jsonPrimitive.content)
        assertEquals("sound", event.content.global!!.underride[1].actions[1].jsonObject["set_tweak"]!!.jsonPrimitive.content)
        assertEquals("default", event.content.global!!.underride[1].actions[1].jsonObject["value"]!!.jsonPrimitive.content)
        assertEquals("highlight", event.content.global!!.underride[1].actions[2].jsonObject["set_tweak"]!!.jsonPrimitive.content)
        assertEquals(1, event.content.global!!.underride[1].conditions.size)
        assertEquals("contains_display_name", event.content.global!!.underride[1].conditions[0].kind)
        assertEquals(true, event.content.global!!.underride[1].default)
        assertEquals(true, event.content.global!!.underride[1].enabled)
        assertEquals(".m.rule.contains_display_name", event.content.global!!.underride[1].ruleId)
        assertEquals(3, event.content.global!!.underride[2].actions.size)
        assertEquals("notify", event.content.global!!.underride[2].actions[0].jsonPrimitive.content)
        assertEquals("sound", event.content.global!!.underride[2].actions[1].jsonObject["set_tweak"]!!.jsonPrimitive.content)
        assertEquals("default", event.content.global!!.underride[2].actions[1].jsonObject["value"]!!.jsonPrimitive.content)
        assertEquals("highlight", event.content.global!!.underride[2].actions[2].jsonObject["set_tweak"]!!.jsonPrimitive.content)
        assertEquals(false, event.content.global!!.underride[2].actions[2].jsonObject["value"]!!.jsonPrimitive.boolean)
        assertEquals(2, event.content.global!!.underride[2].conditions.size)
        assertEquals("room_member_count", event.content.global!!.underride[2].conditions[0].kind)
        assertEquals("2", event.content.global!!.underride[2].conditions[0].`is`!!)
        assertEquals("event_match", event.content.global!!.underride[2].conditions[1].kind)
        assertEquals("type", event.content.global!!.underride[2].conditions[1].key)
        assertEquals("m.room.message", event.content.global!!.underride[2].conditions[1].pattern)
        assertEquals(true, event.content.global!!.underride[2].default)
        assertEquals(true, event.content.global!!.underride[2].enabled)
        assertEquals(".m.rule.room_one_to_one", event.content.global!!.underride[2].ruleId)
        assertEquals(3, event.content.global!!.underride[3].actions.size)
        assertEquals("notify", event.content.global!!.underride[3].actions[0].jsonPrimitive.content)
        assertEquals("sound", event.content.global!!.underride[3].actions[1].jsonObject["set_tweak"]!!.jsonPrimitive.content)
        assertEquals("default", event.content.global!!.underride[3].actions[1].jsonObject["value"]!!.jsonPrimitive.content)
        assertEquals("highlight", event.content.global!!.underride[3].actions[2].jsonObject["set_tweak"]!!.jsonPrimitive.content)
        assertEquals(false, event.content.global!!.underride[3].actions[2].jsonObject["value"]!!.jsonPrimitive.boolean)
        assertEquals(3, event.content.global!!.underride[3].conditions.size)
        assertEquals("type", event.content.global!!.underride[3].conditions[0].key)
        assertEquals("event_match", event.content.global!!.underride[3].conditions[0].kind)
        assertEquals("m.room.member", event.content.global!!.underride[3].conditions[0].pattern)
        assertEquals("content.membership", event.content.global!!.underride[3].conditions[1].key)
        assertEquals("event_match", event.content.global!!.underride[3].conditions[1].kind)
        assertEquals("invite", event.content.global!!.underride[3].conditions[1].pattern)
        assertEquals("state_key", event.content.global!!.underride[3].conditions[2].key)
        assertEquals("event_match", event.content.global!!.underride[3].conditions[2].kind)
        assertEquals("@alice:example.com", event.content.global!!.underride[3].conditions[2].pattern)
        assertEquals(true, event.content.global!!.underride[3].default)
        assertEquals(true, event.content.global!!.underride[3].enabled)
        assertEquals(".m.rule.invite_for_me", event.content.global!!.underride[3].ruleId)
        assertEquals(2, event.content.global!!.underride[4].actions.size)
        assertEquals("notify", event.content.global!!.underride[4].actions[0].jsonPrimitive.content)
        assertEquals("highlight", event.content.global!!.underride[4].actions[1].jsonObject["set_tweak"]!!.jsonPrimitive.content)
        assertEquals(false, event.content.global!!.underride[4].actions[1].jsonObject["value"]!!.jsonPrimitive.boolean)
        assertEquals(1, event.content.global!!.underride[4].conditions.size)
        assertEquals("type", event.content.global!!.underride[4].conditions[0].key)
        assertEquals("event_match", event.content.global!!.underride[4].conditions[0].kind)
        assertEquals("m.room.member", event.content.global!!.underride[4].conditions[0].pattern)
        assertEquals(true, event.content.global!!.underride[4].default)
        assertEquals(true, event.content.global!!.underride[4].enabled)
        assertEquals(".m.rule.member_event", event.content.global!!.underride[4].ruleId)
        assertEquals(2, event.content.global!!.underride[5].actions.size)
        assertEquals("notify", event.content.global!!.underride[5].actions[0].jsonPrimitive.content)
        assertEquals("highlight", event.content.global!!.underride[5].actions[1].jsonObject["set_tweak"]!!.jsonPrimitive.content)
        assertEquals(false, event.content.global!!.underride[5].actions[1].jsonObject["value"]!!.jsonPrimitive.boolean)
        assertEquals(1, event.content.global!!.underride[5].conditions.size)
        assertEquals("type", event.content.global!!.underride[5].conditions[0].key)
        assertEquals("event_match", event.content.global!!.underride[5].conditions[0].kind)
        assertEquals("m.room.message", event.content.global!!.underride[5].conditions[0].pattern)
        assertEquals(true, event.content.global!!.underride[5].default)
        assertEquals(true, event.content.global!!.underride[5].enabled)
        assertEquals(".m.rule.message", event.content.global!!.underride[5].ruleId)
        assertEquals("m.push_rules", event.type)

        // FIXME assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, event))
        // The defaults are specified in the raw json.
    }

    @Test
    fun testReceiptEvent() {
        // language=json
        val json = """
                {
                    "content": {
                        "${'$'}1435641916114394fHBLK:matrix.org": {
                            "m.read": {
                                "@rikj:jki.re": {
                                    "ts": 1436451550453
                                }
                            }
                        }
                    },
                    "type": "m.receipt",
                    "room_id": "!jEsUZKDJdhlrceRyVU:example.org"
                }
                """.trimIndent()

        val serializer = AccountEvent.serializer(ReceiptContent.serializer())
        val event = MatrixJson.decodeFromString(serializer, json)

        assertEquals(1436451550453,
            event.content["$1435641916114394fHBLK:matrix.org"]!!.read!!["@rikj:jki.re"]!!.timestamp)
        assertEquals("m.receipt", event.type)
        // FIXME assertEquals("!jEsUZKDJdhlrceRyVU:example.org", event.roomId)
        //
        // FIXME assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, event))
        // Needs EDU class
    }

    @Test
    fun testRoomAliasesEvent() {
        // language=json
        val json = """
                {
                    "content": {
                        "aliases": [
                            "#somewhere:example.org",
                            "#another:example.org"
                        ]
                    },
                    "type": "m.room.aliases",
                    "event_id": "${'$'}143273582443PhrSn:example.org",
                    "room_id": "!jEsUZKDJdhlrceRyVU:example.org",
                    "sender": "@example:example.org",
                    "origin_server_ts": 1432735824653,
                    "unsigned": {
                        "age": 1234
                    },
                    "state_key": "example.org"
                }
                """.trimIndent()

        val serializer = StateEvent.serializer(AliasesContent.serializer())
        val event = MatrixJson.decodeFromString(serializer, json)

        assertEquals(2, event.content.aliases.size)
        assertEquals("#somewhere:example.org", event.content.aliases[0])
        assertEquals("#another:example.org", event.content.aliases[1])
        assertEquals("m.room.aliases", event.type)
        assertEquals("$143273582443PhrSn:example.org", event.eventId)
        assertEquals("!jEsUZKDJdhlrceRyVU:example.org", event.roomId)
        assertEquals("@example:example.org", event.sender)
        assertEquals(1432735824653, event.originServerTimestamp)
        assertEquals(1234, event.unsigned?.age)
        assertEquals("example.org", event.stateKey)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, event))
    }

    @Test
    fun testRoomAvatarEvent() {
        // language=json
        val json = """
                {
                    "content": {
                        "info": {
                            "h": 398,
                            "w": 394,
                            "mimetype": "image/jpeg",
                            "size": 31037
                        },
                        "url": "mxc://example.org/JWEIFJgwEIhweiWJE"
                    },
                    "type": "m.room.avatar",
                    "event_id": "${'$'}143273582443PhrSn:example.org",
                    "room_id": "!jEsUZKDJdhlrceRyVU:example.org",
                    "sender": "@example:example.org",
                    "origin_server_ts": 1432735824653,
                    "unsigned": {
                        "age": 1234
                    },
                    "state_key": ""
                }
                """.trimIndent()

        val serializer = StateEvent.serializer(AvatarContent.serializer())
        val event = MatrixJson.decodeFromString(serializer, json)

        assertEquals(398, event.content.info?.height)
        assertEquals(394, event.content.info?.width)
        assertEquals("image/jpeg", event.content.info?.mimeType)
        assertEquals(31037, event.content.info?.size)
        assertEquals("mxc://example.org/JWEIFJgwEIhweiWJE", event.content.url)
        assertEquals("m.room.avatar", event.type)
        assertEquals("$143273582443PhrSn:example.org", event.eventId)
        assertEquals("!jEsUZKDJdhlrceRyVU:example.org", event.roomId)
        assertEquals("@example:example.org", event.sender)
        assertEquals(1432735824653, event.originServerTimestamp)
        assertEquals(1234, event.unsigned?.age)
        assertEquals("", event.stateKey)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, event))
    }

    @Test
    fun testRoomCanonicalAliasEvent() {
        // language=json
        val json = """
                {
                    "content": {
                        "alias": "#somewhere:localhost",
                        "alt_aliases": [
                            "#somewhere:example.org",
                            "#myroom:example.com"
                        ]
                    },
                    "type": "m.room.canonical_alias",
                    "event_id": "${'$'}143273582443PhrSn:example.org",
                    "room_id": "!jEsUZKDJdhlrceRyVU:example.org",
                    "sender": "@example:example.org",
                    "origin_server_ts": 1432735824653,
                    "unsigned": {
                        "age": 1234
                    },
                    "state_key": ""
                }
                """.trimIndent()

        val serializer = StateEvent.serializer(CanonicalAliasContent.serializer())
        val event = MatrixJson.decodeFromString(serializer, json)

        assertEquals("#somewhere:localhost", event.content.alias)
        assertEquals(2, event.content.altAliases.size)
        assertEquals("#somewhere:example.org", event.content.altAliases[0])
        assertEquals("#myroom:example.com", event.content.altAliases[1])
        assertEquals("m.room.canonical_alias", event.type)
        assertEquals("$143273582443PhrSn:example.org", event.eventId)
        assertEquals("!jEsUZKDJdhlrceRyVU:example.org", event.roomId)
        assertEquals("@example:example.org", event.sender)
        assertEquals(1432735824653, event.originServerTimestamp)
        assertEquals(1234, event.unsigned?.age)
        assertEquals("", event.stateKey)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, event))
    }

    @Test
    fun testRoomCreateEvent() {
        // language=json
        val json = """
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
                    "room_id": "!jEsUZKDJdhlrceRyVU:example.org",
                    "sender": "@example:example.org",
                    "origin_server_ts": 1432735824653,
                    "unsigned": {
                        "age": 1234
                    },
                    "state_key": ""
                }
                """.trimIndent()

        val serializer = StateEvent.serializer(CreateContent.serializer())
        val event = MatrixJson.decodeFromString(serializer, json)

        assertEquals("@example:example.org", event.content.creator)
        assertEquals("1", event.content.roomVersion)
        assertEquals(true, event.content.mFederate!!)
        assertEquals("\$something:example.org", event.content.predecessor?.eventId)
        assertEquals("!oldroom:example.org", event.content.predecessor?.roomId)
        assertEquals("m.room.create", event.type)
        assertEquals("$143273582443PhrSn:example.org", event.eventId)
        assertEquals("!jEsUZKDJdhlrceRyVU:example.org", event.roomId)
        assertEquals("@example:example.org", event.sender)
        assertEquals(1432735824653, event.originServerTimestamp)
        assertEquals(1234, event.unsigned?.age)
        assertEquals("", event.stateKey)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, event))
    }

    @Test
    fun testRoomEncryptedMegolmEvent() {
        // language=json
        val json = """
                {
                    "content": {
                        "algorithm": "m.megolm.v1.aes-sha2",
                        "ciphertext": "AwgAEnACgAkLmt6qF84IK++J7UDH2Za1YVchHyprqTqsg...",
                        "device_id": "RJYKSTBOIE",
                        "sender_key": "IlRMeOPX2e0MurIyfWEucYBRVOEEUMrOHqn/8mLqMjA",
                        "session_id": "X3lUlvLELLYxeTx4yOVu6UDpasGEVO0Jbu+QFnm0cKQ"
                    },
                    "type": "m.room.encrypted",
                    "event_id": "${'$'}143273582443PhrSn:example.org",
                    "room_id": "!jEsUZKDJdhlrceRyVU:example.org",
                    "sender": "@example:example.org",
                    "origin_server_ts": 1432735824653,
                    "unsigned": {
                        "age": 1234
                    }
                }
                """.trimIndent()

        val serializer = MessageEvent.serializer(EncryptedContent.MegolmV1.serializer())
        val event = MatrixJson.decodeFromString(serializer, json)

        // assertEquals("m.megolm.v1.aes-sha2", event.content.algorithm)
        assertEquals("AwgAEnACgAkLmt6qF84IK++J7UDH2Za1YVchHyprqTqsg...", event.content.ciphertext)
        assertEquals("RJYKSTBOIE", event.content.deviceId)
        assertEquals("IlRMeOPX2e0MurIyfWEucYBRVOEEUMrOHqn/8mLqMjA", event.content.senderKey)
        assertEquals("X3lUlvLELLYxeTx4yOVu6UDpasGEVO0Jbu+QFnm0cKQ", event.content.sessionId)
        assertEquals("m.room.encrypted", event.type)
        assertEquals("$143273582443PhrSn:example.org", event.eventId)
        assertEquals("!jEsUZKDJdhlrceRyVU:example.org", event.roomId)
        assertEquals("@example:example.org", event.sender)
        assertEquals(1432735824653, event.originServerTimestamp)
        assertEquals(1234, event.unsigned?.age)

        val workaround = MessageEvent.serializer(EncryptedContent.serializer())
        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(workaround, event))
    }

    @Test
    fun testRoomEncryptedOlmEvent() {
        // language=json
        val json = """
                {
                    "content": {
                        "algorithm": "m.olm.v1.curve25519-aes-sha2",
                        "sender_key": "Szl29ksW/L8yZGWAX+8dY1XyFi+i5wm+DRhTGkbMiwU",
                        "ciphertext": {
                            "7qZcfnBmbEGzxxaWfBjElJuvn7BZx+lSz/SvFrDF/z8": {
                                "type": 0,
                                "body": "AwogGJJzMhf/S3GQFXAOrCZ3iKyGU5ZScVtjI0KypTYrW..."
                            }
                        }
                    },
                    "type": "m.room.encrypted",
                    "event_id": "${'$'}143273582443PhrSn:example.org",
                    "room_id": "!jEsUZKDJdhlrceRyVU:example.org",
                    "sender": "@example:example.org",
                    "origin_server_ts": 1432735824653,
                    "unsigned": {
                        "age": 1234
                    }
                }
                """.trimIndent()

        val serializer = MessageEvent.serializer(EncryptedContent.OlmV1.serializer())
        val event = MatrixJson.decodeFromString(serializer, json)

        // assertEquals("m.olm.v1.curve25519-aes-sha2", event.content.algorithm)
        assertEquals("Szl29ksW/L8yZGWAX+8dY1XyFi+i5wm+DRhTGkbMiwU", event.content.senderKey)
        assertEquals(0, event.content.ciphertext["7qZcfnBmbEGzxxaWfBjElJuvn7BZx+lSz/SvFrDF/z8"]?.type)
        assertEquals("AwogGJJzMhf/S3GQFXAOrCZ3iKyGU5ZScVtjI0KypTYrW...",
            event.content.ciphertext["7qZcfnBmbEGzxxaWfBjElJuvn7BZx+lSz/SvFrDF/z8"]?.body)
        assertEquals("m.room.encrypted", event.type)
        assertEquals("$143273582443PhrSn:example.org", event.eventId)
        assertEquals("!jEsUZKDJdhlrceRyVU:example.org", event.roomId)
        assertEquals("@example:example.org", event.sender)
        assertEquals(1432735824653, event.originServerTimestamp)
        assertEquals(1234, event.unsigned?.age)

        val workaround = MessageEvent.serializer(EncryptedContent.serializer())
        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(workaround, event))
    }

    @Test
    fun testRoomEncryptionEvent() {
        // language=json
        val json = """
                {
                    "content": {
                        "algorithm": "m.megolm.v1.aes-sha2",
                        "rotation_period_ms": 604800000,
                        "rotation_period_msgs": 100
                    },
                    "type": "m.room.encryption",
                    "event_id": "${'$'}143273582443PhrSn:example.org",
                    "room_id": "!jEsUZKDJdhlrceRyVU:example.org",
                    "sender": "@example:example.org",
                    "origin_server_ts": 1432735824653,
                    "unsigned": {
                        "age": 1234
                    },
                    "state_key": ""
                }
                """.trimIndent()

        val serializer = StateEvent.serializer(EncryptionContent.serializer())
        val event = MatrixJson.decodeFromString(serializer, json)

        assertEquals("m.megolm.v1.aes-sha2", event.content.algorithm)
        assertEquals(604800000, event.content.rotationPeriodMs)
        assertEquals(100, event.content.rotationPeriodMsgs)
        assertEquals("m.room.encryption", event.type)
        assertEquals("$143273582443PhrSn:example.org", event.eventId)
        assertEquals("!jEsUZKDJdhlrceRyVU:example.org", event.roomId)
        assertEquals("@example:example.org", event.sender)
        assertEquals(1432735824653, event.originServerTimestamp)
        assertEquals(1234, event.unsigned?.age)
        assertEquals("", event.stateKey)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, event))
    }

    @Test
    fun testRoomGuestAccessEvent() {
        // language=json
        val json = """
                {
                    "content": {
                        "guest_access": "can_join"
                    },
                    "type": "m.room.guest_access",
                    "event_id": "${'$'}143273582443PhrSn:example.org",
                    "room_id": "!jEsUZKDJdhlrceRyVU:example.org",
                    "sender": "@example:example.org",
                    "origin_server_ts": 1432735824653,
                    "unsigned": {
                        "age": 1234
                    },
                    "state_key": ""
                }
                """.trimIndent()

        val serializer = StateEvent.serializer(GuestAccessContent.serializer())
        val event = MatrixJson.decodeFromString(serializer, json)

        assertEquals(GuestAccess.CAN_JOIN, event.content.guestAccess)
        assertEquals("m.room.guest_access", event.type)
        assertEquals("$143273582443PhrSn:example.org", event.eventId)
        assertEquals("!jEsUZKDJdhlrceRyVU:example.org", event.roomId)
        assertEquals("@example:example.org", event.sender)
        assertEquals(1432735824653, event.originServerTimestamp)
        assertEquals(1234, event.unsigned?.age)
        assertEquals("", event.stateKey)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, event))
    }

    @Test
    fun testRoomHistoryVisibilityEvent() {
        // language=json
        val json = """
                {
                    "content": {
                        "history_visibility": "shared"
                    },
                    "type": "m.room.history_visibility",
                    "event_id": "${'$'}143273582443PhrSn:example.org",
                    "room_id": "!jEsUZKDJdhlrceRyVU:example.org",
                    "sender": "@example:example.org",
                    "origin_server_ts": 1432735824653,
                    "unsigned": {
                        "age": 1234
                    },
                    "state_key": ""
                }
                """.trimIndent()

        val serializer = StateEvent.serializer(HistoryVisibilityContent.serializer())
        val event = MatrixJson.decodeFromString(serializer, json)

        assertEquals(HistoryVisibility.SHARED, event.content.historyVisibility)
        assertEquals("m.room.history_visibility", event.type)
        assertEquals("$143273582443PhrSn:example.org", event.eventId)
        assertEquals("!jEsUZKDJdhlrceRyVU:example.org", event.roomId)
        assertEquals("@example:example.org", event.sender)
        assertEquals(1432735824653, event.originServerTimestamp)
        assertEquals(1234, event.unsigned?.age)
        assertEquals("", event.stateKey)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, event))
    }

    @Test
    fun testRoomJoinRulesEvent() {
        // language=json
        val json = """
                {
                    "content": {
                        "join_rule": "public"
                    },
                    "type": "m.room.join_rules",
                    "event_id": "${'$'}143273582443PhrSn:example.org",
                    "room_id": "!jEsUZKDJdhlrceRyVU:example.org",
                    "sender": "@example:example.org",
                    "origin_server_ts": 1432735824653,
                    "unsigned": {
                        "age": 1234
                    },
                    "state_key": ""
                }
                """.trimIndent()

        val serializer = StateEvent.serializer(JoinRulesContent.serializer())
        val event = MatrixJson.decodeFromString(serializer, json)

        assertEquals(JoinRule.PUBLIC, event.content.joinRule)
        assertEquals("m.room.join_rules", event.type)
        assertEquals("$143273582443PhrSn:example.org", event.eventId)
        assertEquals("!jEsUZKDJdhlrceRyVU:example.org", event.roomId)
        assertEquals("@example:example.org", event.sender)
        assertEquals(1432735824653, event.originServerTimestamp)
        assertEquals(1234, event.unsigned?.age)
        assertEquals("", event.stateKey)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, event))
    }

    @Test
    fun testRoomMemberEvent() {
        // language=json
        val json = """
                {
                    "content": {
                        "membership": "join",
                        "avatar_url": "mxc://example.org/SEsfnsuifSDFSSEF",
                        "displayname": "Alice Margatroid"
                    },
                    "type": "m.room.member",
                    "event_id": "${'$'}143273582443PhrSn:example.org",
                    "room_id": "!jEsUZKDJdhlrceRyVU:example.org",
                    "sender": "@example:example.org",
                    "origin_server_ts": 1432735824653,
                    "unsigned": {
                        "age": 1234
                    },
                    "state_key": "@alice:example.org"
                }
                """.trimIndent()

        val serializer = StateEvent.serializer(MemberContent.serializer())
        val event = MatrixJson.decodeFromString(serializer, json)

        assertEquals(Membership.JOIN, event.content.membership)
        assertEquals("mxc://example.org/SEsfnsuifSDFSSEF", event.content.avatarUrl)
        assertEquals("Alice Margatroid", event.content.displayName)
        assertEquals("m.room.member", event.type)
        assertEquals("$143273582443PhrSn:example.org", event.eventId)
        assertEquals("!jEsUZKDJdhlrceRyVU:example.org", event.roomId)
        assertEquals("@example:example.org", event.sender)
        assertEquals(1432735824653, event.originServerTimestamp)
        assertEquals(1234, event.unsigned?.age)
        assertEquals("@alice:example.org", event.stateKey)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, event))
    }

    @Test
    fun testRoomMemberThirdPartyInviteEvent() {
        // language=json
        val json = """
                {
                    "content": {
                        "membership": "invite",
                        "avatar_url": "mxc://example.org/SEsfnsuifSDFSSEF",
                        "displayname": "Alice Margatroid",
                        "third_party_invite": {
                            "display_name": "alice",
                            "signed": {
                                "mxid": "@alice:example.org",
                                "signatures": {
                                    "magic.forest": {
                                        "ed25519:3": "fQpGIW1Snz+pwLZu6sTy2aHy/DYWWTspTJRPyNp0PKkymfIsNffysMl6ObMMFdIJhk6g6pwlIqZ54rxo8SLmAg"
                                    }
                                },
                                "token": "abc123"
                            }
                        }
                    },
                    "type": "m.room.member",
                    "event_id": "${'$'}143273582443PhrSn:example.org",
                    "room_id": "!jEsUZKDJdhlrceRyVU:example.org",
                    "sender": "@example:example.org",
                    "origin_server_ts": 1432735824653,
                    "unsigned": {
                        "age": 1234
                    },
                    "state_key": "@alice:example.org"
                }
                """.trimIndent()

        val serializer = StateEvent.serializer(MemberContent.serializer())
        val event = MatrixJson.decodeFromString(serializer, json)

        assertEquals(Membership.INVITE, event.content.membership)
        assertEquals("mxc://example.org/SEsfnsuifSDFSSEF", event.content.avatarUrl)
        assertEquals("Alice Margatroid", event.content.displayName)
        assertEquals("alice", event.content.thirdPartyInvite?.displayName)
        assertEquals("@alice:example.org", event.content.thirdPartyInvite?.signed?.mxid)
        // FIXME assertEquals("fQpGIW1Snz+pwLZu6sTy2aHy/DYWWTspTJRPyNp0PKkymfIsNffysMl6ObMMFdIJhk6g6pwlIqZ54rxo8SLmAg",
        //     event.content.thirdPartyInvite?.signed?.signatures["magic.forest"]!!["ed25519:3"]!!)
        assertEquals("abc123", event.content.thirdPartyInvite?.signed?.token)
        assertEquals("m.room.member", event.type)
        assertEquals("$143273582443PhrSn:example.org", event.eventId)
        assertEquals("!jEsUZKDJdhlrceRyVU:example.org", event.roomId)
        assertEquals("@example:example.org", event.sender)
        assertEquals(1432735824653, event.originServerTimestamp)
        assertEquals(1234, event.unsigned?.age)
        assertEquals("@alice:example.org", event.stateKey)

        // FIXME assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, event))
        // signatures
    }

    @Test
    fun testRoomMessageAudioEvent() {
        // language=json
        val json = """
                {
                    "content": {
                        "body": "Bee Gees - Stayin' Alive",
                        "url": "mxc://example.org/ffed755USFFxlgbQYZGtryd",
                        "info": {
                            "duration": 2140786,
                            "size": 1563685,
                            "mimetype": "audio/mpeg"
                        },
                        "msgtype": "m.audio"
                    },
                    "type": "m.room.message",
                    "event_id": "${'$'}143273582443PhrSn:example.org",
                    "room_id": "!jEsUZKDJdhlrceRyVU:example.org",
                    "sender": "@example:example.org",
                    "origin_server_ts": 1432735824653,
                    "unsigned": {
                        "age": 1234
                    }
                }
                """.trimIndent()

        val serializer = MessageEvent.serializer(MessageContent.Audio.serializer())
        val event = MatrixJson.decodeFromString(serializer, json)

        assertEquals("Bee Gees - Stayin' Alive", event.content.body)
        assertEquals("mxc://example.org/ffed755USFFxlgbQYZGtryd", event.content.url)
        assertEquals(2140786, event.content.info?.duration)
        assertEquals(1563685, event.content.info?.size)
        assertEquals("audio/mpeg", event.content.info?.mimeType)
        // assertEquals("m.audio", event.content.msgtype)
        assertEquals("m.room.message", event.type)
        assertEquals("$143273582443PhrSn:example.org", event.eventId)
        assertEquals("!jEsUZKDJdhlrceRyVU:example.org", event.roomId)
        assertEquals("@example:example.org", event.sender)
        assertEquals(1432735824653, event.originServerTimestamp)
        assertEquals(1234, event.unsigned?.age)

        val workaround = MessageEvent.serializer(MessageContent.serializer())
        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(workaround, event))
    }

    @Test
    fun testRoomMessageEmoteEvent() {
        // language=json
        val json = """
                {
                    "content": {
                        "body": "thinks this is an example emote",
                        "msgtype": "m.emote",
                        "format": "org.matrix.custom.html",
                        "formatted_body": "thinks <b>this</b> is an example emote"
                    },
                    "type": "m.room.message",
                    "event_id": "${'$'}143273582443PhrSn:example.org",
                    "room_id": "!jEsUZKDJdhlrceRyVU:example.org",
                    "sender": "@example:example.org",
                    "origin_server_ts": 1432735824653,
                    "unsigned": {
                        "age": 1234
                    }
                }
                """.trimIndent()

        val serializer = MessageEvent.serializer(MessageContent.Emote.serializer())
        val event = MatrixJson.decodeFromString(serializer, json)

        assertEquals("thinks this is an example emote", event.content.body)
        // assertEquals("m.emote", event.content.msgtype)
        assertEquals("org.matrix.custom.html", event.content.format)
        assertEquals("thinks <b>this</b> is an example emote", event.content.formattedBody)
        assertEquals("m.room.message", event.type)
        assertEquals("$143273582443PhrSn:example.org", event.eventId)
        assertEquals("!jEsUZKDJdhlrceRyVU:example.org", event.roomId)
        assertEquals("@example:example.org", event.sender)
        assertEquals(1432735824653, event.originServerTimestamp)
        assertEquals(1234, event.unsigned?.age)

        val workaround = MessageEvent.serializer(MessageContent.serializer())
        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(workaround, event))
    }

    @Test
    fun testRoomMessageFileEvent() {
        // language=json
        val json = """
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
                    "event_id": "${'$'}143273582443PhrSn:example.org",
                    "room_id": "!jEsUZKDJdhlrceRyVU:example.org",
                    "sender": "@example:example.org",
                    "origin_server_ts": 1432735824653,
                    "unsigned": {
                        "age": 1234
                    }
                }
                """.trimIndent()

        val serializer = MessageEvent.serializer(MessageContent.File.serializer())
        val event = MatrixJson.decodeFromString(serializer, json)

        assertEquals("something-important.doc", event.content.body)
        assertEquals("something-important.doc", event.content.filename)
        assertEquals("application/msword", event.content.info?.mimeType)
        assertEquals(46144, event.content.info?.size)
        // assertEquals("m.file", event.content.msgtype)
        assertEquals("mxc://example.org/FHyPlCeYUSFFxlgbQYZmoEoe", event.content.url)
        assertEquals("m.room.message", event.type)
        assertEquals("$143273582443PhrSn:example.org", event.eventId)
        assertEquals("!jEsUZKDJdhlrceRyVU:example.org", event.roomId)
        assertEquals("@example:example.org", event.sender)
        assertEquals(1432735824653, event.originServerTimestamp)
        assertEquals(1234, event.unsigned?.age)

        val workaround = MessageEvent.serializer(MessageContent.serializer())
        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(workaround, event))
    }

    @Test
    fun testRoomMessageImageEvent() {
        // language=json
        val json = """
                {
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
                    "event_id": "${'$'}143273582443PhrSn:example.org",
                    "room_id": "!jEsUZKDJdhlrceRyVU:example.org",
                    "sender": "@example:example.org",
                    "origin_server_ts": 1432735824653,
                    "unsigned": {
                        "age": 1234
                    }
                }
                """.trimIndent()

        val serializer = MessageEvent.serializer(MessageContent.Image.serializer())
        val event = MatrixJson.decodeFromString(serializer, json)

        assertEquals("filename.jpg", event.content.body)
        assertEquals(398, event.content.info?.height)
        assertEquals(394, event.content.info?.width)
        assertEquals("image/jpeg", event.content.info?.mimeType)
        assertEquals(31037, event.content.info?.size)
        assertEquals("mxc://example.org/JWEIFJgwEIhweiWJE", event.content.url)
        // assertEquals("m.image", event.content.msgtype)
        assertEquals("m.room.message", event.type)
        assertEquals("$143273582443PhrSn:example.org", event.eventId)
        assertEquals("!jEsUZKDJdhlrceRyVU:example.org", event.roomId)
        assertEquals("@example:example.org", event.sender)
        assertEquals(1432735824653, event.originServerTimestamp)
        assertEquals(1234, event.unsigned?.age)

        val workaround = MessageEvent.serializer(MessageContent.serializer())
        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(workaround, event))
    }

    @Test
    fun testRoomMessageLocationEvent() {
        // language=json
        val json = """
                {
                    "content": {
                        "body": "Big Ben, London, UK",
                        "geo_uri": "geo:51.5008,0.1247",
                        "info": {
                            "thumbnail_url": "mxc://example.org/FHyPlCeYUSFFxlgbQYZmoEoe",
                            "thumbnail_info": {
                                "mimetype": "image/jpeg",
                                "size": 46144,
                                "w": 300,
                                "h": 300
                            }
                        },
                        "msgtype": "m.location"
                    },
                    "type": "m.room.message",
                    "event_id": "${'$'}143273582443PhrSn:example.org",
                    "room_id": "!jEsUZKDJdhlrceRyVU:example.org",
                    "sender": "@example:example.org",
                    "origin_server_ts": 1432735824653,
                    "unsigned": {
                        "age": 1234
                    }
                }
                """.trimIndent()

        val serializer = MessageEvent.serializer(MessageContent.Location.serializer())
        val event = MatrixJson.decodeFromString(serializer, json)

        assertEquals("Big Ben, London, UK", event.content.body)
        assertEquals("geo:51.5008,0.1247", event.content.geoUri)
        assertEquals("mxc://example.org/FHyPlCeYUSFFxlgbQYZmoEoe", event.content.info?.thumbnailUrl)
        assertEquals("image/jpeg", event.content.info?.thumbnailInfo?.mimeType)
        assertEquals(46144, event.content.info?.thumbnailInfo?.size)
        assertEquals(300, event.content.info?.thumbnailInfo?.width)
        assertEquals(300, event.content.info?.thumbnailInfo?.height)
        // assertEquals("m.location", event.content.msgtype)
        assertEquals("m.room.message", event.type)
        assertEquals("$143273582443PhrSn:example.org", event.eventId)
        assertEquals("!jEsUZKDJdhlrceRyVU:example.org", event.roomId)
        assertEquals("@example:example.org", event.sender)
        assertEquals(1432735824653, event.originServerTimestamp)
        assertEquals(1234, event.unsigned?.age)

        val workaround = MessageEvent.serializer(MessageContent.serializer())
        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(workaround, event))
    }

    @Test
    fun testRoomMessageNoticeEvent() {
        // language=json
        val json = """
                {
                    "content": {
                        "body": "This is an example notice",
                        "msgtype": "m.notice",
                        "format": "org.matrix.custom.html",
                        "formatted_body": "This is an <strong>example</strong> notice"
                    },
                    "type": "m.room.message",
                    "event_id": "${'$'}143273582443PhrSn:example.org",
                    "room_id": "!jEsUZKDJdhlrceRyVU:example.org",
                    "sender": "@example:example.org",
                    "origin_server_ts": 1432735824653,
                    "unsigned": {
                        "age": 1234
                    }
                }
                """.trimIndent()

        val serializer = MessageEvent.serializer(MessageContent.Notice.serializer())
        val event = MatrixJson.decodeFromString(serializer, json)

        assertEquals("This is an example notice", event.content.body)
        // assertEquals("m.notice", event.content.msgtype)
        assertEquals("org.matrix.custom.html", event.content.format)
        assertEquals("This is an <strong>example</strong> notice", event.content.formattedBody)
        assertEquals("m.room.message", event.type)
        assertEquals("$143273582443PhrSn:example.org", event.eventId)
        assertEquals("!jEsUZKDJdhlrceRyVU:example.org", event.roomId)
        assertEquals("@example:example.org", event.sender)
        assertEquals(1432735824653, event.originServerTimestamp)
        assertEquals(1234, event.unsigned?.age)

        val workaround = MessageEvent.serializer(MessageContent.serializer())
        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(workaround, event))
    }

    @Test
    fun testRoomMessageServerNoticeEvent() {
        // language=json
        val json = """
                {
                    "content": {
                        "body": "Human-readable message to explain the notice",
                        "msgtype": "m.server_notice",
                        "server_notice_type": "m.server_notice.usage_limit_reached",
                        "admin_contact": "mailto:server.admin@example.org",
                        "limit_type": "monthly_active_user"
                    },
                    "type": "m.room.message",
                    "event_id": "${'$'}143273582443PhrSn:example.org",
                    "room_id": "!jEsUZKDJdhlrceRyVU:example.org",
                    "sender": "@example:example.org",
                    "origin_server_ts": 1432735824653,
                    "unsigned": {
                        "age": 1234
                    }
                }
                """.trimIndent()

        val serializer = MessageEvent.serializer(MessageContent.ServerNotice.serializer())
        val event = MatrixJson.decodeFromString(serializer, json)

        assertEquals("Human-readable message to explain the notice", event.content.body)
        // assertEquals("m.server_notice", event.content.msgtype)
        assertEquals("m.server_notice.usage_limit_reached", event.content.serverNoticeType)
        assertEquals("mailto:server.admin@example.org", event.content.adminContact)
        assertEquals("monthly_active_user", event.content.limitType)
        assertEquals("m.room.message", event.type)
        assertEquals("$143273582443PhrSn:example.org", event.eventId)
        assertEquals("!jEsUZKDJdhlrceRyVU:example.org", event.roomId)
        assertEquals("@example:example.org", event.sender)
        assertEquals(1432735824653, event.originServerTimestamp)
        assertEquals(1234, event.unsigned?.age)

        val workaround = MessageEvent.serializer(MessageContent.serializer())
        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(workaround, event))
    }

    @Test
    fun testRoomMessageTextEvent() {
        // language=json
        val json = """
                {
                    "content": {
                        "body": "This is an example text message",
                        "msgtype": "m.text",
                        "format": "org.matrix.custom.html",
                        "formatted_body": "<b>This is an example text message</b>"
                    },
                    "type": "m.room.message",
                    "event_id": "${'$'}143273582443PhrSn:example.org",
                    "room_id": "!jEsUZKDJdhlrceRyVU:example.org",
                    "sender": "@example:example.org",
                    "origin_server_ts": 1432735824653,
                    "unsigned": {
                        "age": 1234
                    }
                }
                """.trimIndent()

        val serializer = MessageEvent.serializer(MessageContent.Text.serializer())
        val event = MatrixJson.decodeFromString(serializer, json)

        assertEquals("This is an example text message", event.content.body)
        // assertEquals("m.text", event.content.msgtype)
        assertEquals("org.matrix.custom.html", event.content.format)
        assertEquals("<b>This is an example text message</b>", event.content.formattedBody)
        assertEquals("m.room.message", event.type)
        assertEquals("$143273582443PhrSn:example.org", event.eventId)
        assertEquals("!jEsUZKDJdhlrceRyVU:example.org", event.roomId)
        assertEquals("@example:example.org", event.sender)
        assertEquals(1432735824653, event.originServerTimestamp)
        assertEquals(1234, event.unsigned?.age)

        val workaround = MessageEvent.serializer(MessageContent.serializer())
        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(workaround, event))
    }

    @Test
    fun testRoomMessageVideoEvent() {
        // language=json
        val json = """
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
                    "event_id": "${'$'}143273582443PhrSn:example.org",
                    "room_id": "!jEsUZKDJdhlrceRyVU:example.org",
                    "sender": "@example:example.org",
                    "origin_server_ts": 1432735824653,
                    "unsigned": {
                        "age": 1234
                    }
                }
                """.trimIndent()

        val serializer = MessageEvent.serializer(MessageContent.Video.serializer())
        val event = MatrixJson.decodeFromString(serializer, json)

        assertEquals("Gangnam Style", event.content.body)
        assertEquals("mxc://example.org/a526eYUSFFxlgbQYZmo442", event.content.url)
        assertEquals("mxc://example.org/FHyPlCeYUSFFxlgbQYZmoEoe", event.content.info?.thumbnailUrl)
        assertEquals("image/jpeg", event.content.info?.thumbnailInfo?.mimeType)
        assertEquals(46144, event.content.info?.thumbnailInfo?.size)
        assertEquals(300, event.content.info?.thumbnailInfo?.width)
        assertEquals(300, event.content.info?.thumbnailInfo?.height)
        assertEquals(480, event.content.info?.width)
        assertEquals(320, event.content.info?.height)
        assertEquals(2140786, event.content.info?.duration)
        assertEquals(1563685, event.content.info?.size)
        assertEquals("video/mp4", event.content.info?.mimeType)
        // assertEquals("m.video", event.content.msgtype)
        assertEquals("m.room.message", event.type)
        assertEquals("$143273582443PhrSn:example.org", event.eventId)
        assertEquals("!jEsUZKDJdhlrceRyVU:example.org", event.roomId)
        assertEquals("@example:example.org", event.sender)
        assertEquals(1432735824653, event.originServerTimestamp)
        assertEquals(1234, event.unsigned?.age)

        val workaround = MessageEvent.serializer(MessageContent.serializer())
        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(workaround, event))
    }

    @Test
    fun testRoomMessageFeedbackEvent() {
        // language=json
        val json = """
                {
                    "content": {
                        "type": "delivered",
                        "target_event_id": "${'$'}WEIGFHFW:localhost"
                    },
                    "type": "m.room.message.feedback",
                    "event_id": "${'$'}143273582443PhrSn:example.org",
                    "room_id": "!jEsUZKDJdhlrceRyVU:example.org",
                    "sender": "@example:example.org",
                    "origin_server_ts": 1432735824653,
                    "unsigned": {
                        "age": 1234
                    }
                }
                """.trimIndent()

        val serializer = MessageEvent.serializer(FeedbackContent.serializer())
        val event = MatrixJson.decodeFromString(serializer, json)

        assertEquals("delivered", event.content.type)
        assertEquals("\$WEIGFHFW:localhost", event.content.targetEventId)
        assertEquals("m.room.message.feedback", event.type)
        assertEquals("$143273582443PhrSn:example.org", event.eventId)
        assertEquals("!jEsUZKDJdhlrceRyVU:example.org", event.roomId)
        assertEquals("@example:example.org", event.sender)
        assertEquals(1432735824653, event.originServerTimestamp)
        assertEquals(1234, event.unsigned?.age)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, event))
    }

    @Test
    fun testRoomNameEvent() {
        // language=json
        val json = """
                {
                    "content": {
                        "name": "The room name"
                    },
                    "type": "m.room.name",
                    "event_id": "${'$'}143273582443PhrSn:example.org",
                    "room_id": "!jEsUZKDJdhlrceRyVU:example.org",
                    "sender": "@example:example.org",
                    "origin_server_ts": 1432735824653,
                    "unsigned": {
                        "age": 1234
                    },
                    "state_key": ""
                }
                """.trimIndent()

        val serializer = StateEvent.serializer(NameContent.serializer())
        val event = MatrixJson.decodeFromString(serializer, json)

        assertEquals("The room name", event.content.name)
        assertEquals("m.room.name", event.type)
        assertEquals("$143273582443PhrSn:example.org", event.eventId)
        assertEquals("!jEsUZKDJdhlrceRyVU:example.org", event.roomId)
        assertEquals("@example:example.org", event.sender)
        assertEquals(1432735824653, event.originServerTimestamp)
        assertEquals(1234, event.unsigned?.age)
        assertEquals("", event.stateKey)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, event))
    }

    @Test
    fun testRoomPinnedEventsEvent() {
        // language=json
        val json = """
                {
                    "content": {
                        "pinned": [
                            "${'$'}someevent:example.org"
                        ]
                    },
                    "type": "m.room.pinned_events",
                    "event_id": "${'$'}143273582443PhrSn:example.org",
                    "room_id": "!jEsUZKDJdhlrceRyVU:example.org",
                    "sender": "@example:example.org",
                    "origin_server_ts": 1432735824653,
                    "unsigned": {
                        "age": 1234
                    },
                    "state_key": ""
                }
                """.trimIndent()

        val serializer = StateEvent.serializer(PinnedEventsContent.serializer())
        val event = MatrixJson.decodeFromString(serializer, json)

        assertEquals(1, event.content.pinned.size)
        assertEquals("\$someevent:example.org", event.content.pinned[0])
        assertEquals("m.room.pinned_events", event.type)
        assertEquals("$143273582443PhrSn:example.org", event.eventId)
        assertEquals("!jEsUZKDJdhlrceRyVU:example.org", event.roomId)
        assertEquals("@example:example.org", event.sender)
        assertEquals(1432735824653, event.originServerTimestamp)
        assertEquals(1234, event.unsigned?.age)
        assertEquals("", event.stateKey)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, event))
    }

    @Test
    fun testRoomPowerLevelsEvent() {
        // language=json
        val json = """
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
                    "room_id": "!jEsUZKDJdhlrceRyVU:example.org",
                    "sender": "@example:example.org",
                    "origin_server_ts": 1432735824653,
                    "unsigned": {
                        "age": 1234
                    },
                    "state_key": ""
                }
                """.trimIndent()

        val serializer = StateEvent.serializer(PowerLevelsContent.serializer())
        val event = MatrixJson.decodeFromString(serializer, json)

        assertEquals(50, event.content.ban)
        assertEquals(2, event.content.events.size)
        assertEquals(100, event.content.events["m.room.name"])
        assertEquals(100, event.content.events["m.room.power_levels"])
        assertEquals(0, event.content.eventsDefault)
        assertEquals(50, event.content.invite)
        assertEquals(50, event.content.kick)
        assertEquals(50, event.content.redact)
        assertEquals(50, event.content.stateDefault)
        assertEquals(100, event.content.users["@example:localhost"])
        assertEquals(0, event.content.usersDefault)
        assertEquals(20, event.content.notifications?.room)
        assertEquals("m.room.power_levels", event.type)
        assertEquals("$143273582443PhrSn:example.org", event.eventId)
        assertEquals("!jEsUZKDJdhlrceRyVU:example.org", event.roomId)
        assertEquals("@example:example.org", event.sender)
        assertEquals(1432735824653, event.originServerTimestamp)
        assertEquals(1234, event.unsigned?.age)
        assertEquals("", event.stateKey)

        // Default values are being ignored.
        // assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, event))
    }

    @Test
    fun testRoomRedactionEvent() {
        // language=json
        val json = """
                {
                    "content": {
                        "reason": "Spamming"
                    },
                    "type": "m.room.redaction",
                    "event_id": "${'$'}143273582443PhrSn:example.org",
                    "room_id": "!jEsUZKDJdhlrceRyVU:example.org",
                    "sender": "@example:example.org",
                    "origin_server_ts": 1432735824653,
                    "unsigned": {
                        "age": 1234
                    },
                    "redacts": "${'$'}fukweghifu23:localhost"
                }
                """.trimIndent()

        val serializer = MessageEvent.serializer(RedactionContent.serializer())
        val event = MatrixJson.decodeFromString(serializer, json)

        assertEquals("Spamming", event.content.reason)
        assertEquals("m.room.redaction", event.type)
        assertEquals("$143273582443PhrSn:example.org", event.eventId)
        assertEquals("!jEsUZKDJdhlrceRyVU:example.org", event.roomId)
        assertEquals("@example:example.org", event.sender)
        assertEquals(1432735824653, event.originServerTimestamp)
        assertEquals(1234, event.unsigned?.age)
        // assertEquals("\$fukweghifu23:localhost", event.redacts) WHY is this field here?!?!?!

        // FIXME assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, event))
        // The spec is quite annoying here.
    }

    @Test
    fun testRoomServerAclEvent() {
        // language=json
        val json = """
                {
                    "age": 242352,
                    "content": {
                        "allow_ip_literals": false,
                        "allow": [
                            "*"
                        ],
                        "deny": [
                            "*.evil.com",
                            "evil.com"
                        ]
                    },
                    "state_key": "",
                    "origin_server_ts": 1431961217939,
                    "event_id": "${'$'}WLGTSEFSEF:localhost",
                    "type": "m.room.server_acl",
                    "room_id": "!Cuyf34gef24t:localhost",
                    "sender": "@example:localhost"
                }
                """.trimIndent()

        val serializer = StateEvent.serializer(ServerAclContent.serializer())
        val event = MatrixJson.decodeFromString(serializer, json)

        // assertEquals(242352, event.age)
        assertEquals(false, event.content.allowIpLiterals)
        assertEquals(1, event.content.allow.size)
        assertEquals("*", event.content.allow[0])
        assertEquals(2, event.content.deny.size)
        assertEquals("*.evil.com", event.content.deny[0])
        assertEquals("evil.com", event.content.deny[1])
        assertEquals("", event.stateKey)
        assertEquals(1431961217939, event.originServerTimestamp)
        assertEquals("\$WLGTSEFSEF:localhost", event.eventId)
        assertEquals("m.room.server_acl", event.type)
        assertEquals("!Cuyf34gef24t:localhost", event.roomId)
        assertEquals("@example:localhost", event.sender)

        // FIXME assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, event))
        // age
    }

    @Test
    fun testRoomThirdPartyInviteEvent() {
        // language=json
        val json = """
                {
                    "content": {
                        "display_name": "Alice Margatroid",
                        "key_validity_url": "https://magic.forest/verifykey",
                        "public_key": "abc123",
                        "public_keys": [
                            {
                                "public_key": "def456",
                                "key_validity_url": "https://magic.forest/verifykey"
                            }
                        ]
                    },
                    "type": "m.room.third_party_invite",
                    "event_id": "${'$'}143273582443PhrSn:example.org",
                    "room_id": "!jEsUZKDJdhlrceRyVU:example.org",
                    "sender": "@example:example.org",
                    "origin_server_ts": 1432735824653,
                    "unsigned": {
                        "age": 1234
                    },
                    "state_key": "pc98"
                }
                """.trimIndent()

        val serializer = StateEvent.serializer(ThirdPartyInviteContent.serializer())
        val event = MatrixJson.decodeFromString(serializer, json)

        assertEquals("Alice Margatroid", event.content.displayName)
        assertEquals("https://magic.forest/verifykey", event.content.keyValidityUrl)
        assertEquals("abc123", event.content.publicKey)
        assertEquals(1, event.content.publicKeys.size)
        assertEquals("def456", event.content.publicKeys[0].publicKey)
        assertEquals("https://magic.forest/verifykey", event.content.publicKeys[0].keyValidityUrl)
        assertEquals("m.room.third_party_invite", event.type)
        assertEquals("$143273582443PhrSn:example.org", event.eventId)
        assertEquals("!jEsUZKDJdhlrceRyVU:example.org", event.roomId)
        assertEquals("@example:example.org", event.sender)
        assertEquals(1432735824653, event.originServerTimestamp)
        assertEquals(1234, event.unsigned?.age)
        assertEquals("pc98", event.stateKey)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, event))
    }

    @Test
    fun testRoomTombstoneEvent() {
        // language=json
        val json = """
                {
                    "content": {
                        "body": "This room has been replaced",
                        "replacement_room": "!newroom:example.org"
                    },
                    "type": "m.room.tombstone",
                    "event_id": "${'$'}143273582443PhrSn:example.org",
                    "room_id": "!jEsUZKDJdhlrceRyVU:example.org",
                    "sender": "@example:example.org",
                    "origin_server_ts": 1432735824653,
                    "unsigned": {
                        "age": 1234
                    },
                    "state_key": ""
                }
                """.trimIndent()

        val serializer = StateEvent.serializer(TombstoneContent.serializer())
        val event = MatrixJson.decodeFromString(serializer, json)

        assertEquals("This room has been replaced", event.content.body)
        assertEquals("!newroom:example.org", event.content.replacementRoom)
        assertEquals("m.room.tombstone", event.type)
        assertEquals("$143273582443PhrSn:example.org", event.eventId)
        assertEquals("!jEsUZKDJdhlrceRyVU:example.org", event.roomId)
        assertEquals("@example:example.org", event.sender)
        assertEquals(1432735824653, event.originServerTimestamp)
        assertEquals(1234, event.unsigned?.age)
        assertEquals("", event.stateKey)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, event))
    }

    @Test
    fun testRoomTopicEvent() {
        // language=json
        val json = """
                {
                    "content": {
                        "topic": "A room topic"
                    },
                    "type": "m.room.topic",
                    "event_id": "${'$'}143273582443PhrSn:example.org",
                    "room_id": "!jEsUZKDJdhlrceRyVU:example.org",
                    "sender": "@example:example.org",
                    "origin_server_ts": 1432735824653,
                    "unsigned": {
                        "age": 1234
                    },
                    "state_key": ""
                }
                """.trimIndent()

        val serializer = StateEvent.serializer(TopicContent.serializer())
        val event = MatrixJson.decodeFromString(serializer, json)

        assertEquals("A room topic", event.content.topic)
        assertEquals("m.room.topic", event.type)
        assertEquals("$143273582443PhrSn:example.org", event.eventId)
        assertEquals("!jEsUZKDJdhlrceRyVU:example.org", event.roomId)
        assertEquals("@example:example.org", event.sender)
        assertEquals(1432735824653, event.originServerTimestamp)
        assertEquals(1234, event.unsigned?.age)
        assertEquals("", event.stateKey)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, event))
    }

    @Test
    fun testRoomKeyEvent() {
        // language=json
        val json = """
                {
                    "content": {
                        "algorithm": "m.megolm.v1.aes-sha2",
                        "room_id": "!Cuyf34gef24t:localhost",
                        "session_id": "X3lUlvLELLYxeTx4yOVu6UDpasGEVO0Jbu+QFnm0cKQ",
                        "session_key": "AgAAAADxKHa9uFxcXzwYoNueL5Xqi69IkD4sni8LlfJL7qNBEY..."
                    },
                    "type": "m.room_key"
                }
                """.trimIndent()

        val serializer = AccountEvent.serializer(RoomKeyContent.serializer())
        val event = MatrixJson.decodeFromString(serializer, json)

        assertEquals("m.megolm.v1.aes-sha2", event.content.algorithm)
        assertEquals("!Cuyf34gef24t:localhost", event.content.roomId)
        assertEquals("X3lUlvLELLYxeTx4yOVu6UDpasGEVO0Jbu+QFnm0cKQ", event.content.sessionId)
        assertEquals("AgAAAADxKHa9uFxcXzwYoNueL5Xqi69IkD4sni8LlfJL7qNBEY...",
            event.content.sessionKey)
        assertEquals("m.room_key", event.type)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, event))
    }

    @Test
    fun testRoomKeyRequestCancelRequestEvent() {
        // language=json
        val json = """
                {
                    "content": {
                        "action": "request_cancellation",
                        "requesting_device_id": "RJYKSTBOIE",
                        "request_id": "1495474790150.19"
                    },
                    "type": "m.room_key_request"
                }
                """.trimIndent()

        val serializer = AccountEvent.serializer(RoomKeyRequestContent.Cancellation.serializer())
        val event = MatrixJson.decodeFromString(serializer, json)

        // assertEquals("request_cancellation", event.content.action)
        assertEquals("RJYKSTBOIE", event.content.requestingDeviceId)
        assertEquals("1495474790150.19", event.content.requestId)
        assertEquals("m.room_key_request", event.type)

        val workaround = AccountEvent.serializer(RoomKeyRequestContent.serializer())
        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(workaround, event))
    }

    @Test
    fun testRoomKeyRequestRequestEvent() {
        // language=json
        val json = """
                {
                    "content": {
                        "body": {
                            "algorithm": "m.megolm.v1.aes-sha2",
                            "room_id": "!Cuyf34gef24t:localhost",
                            "session_id": "X3lUlvLELLYxeTx4yOVu6UDpasGEVO0Jbu+QFnm0cKQ",
                            "sender_key": "RF3s+E7RkTQTGF2d8Deol0FkQvgII2aJDf3/Jp5mxVU"
                        },
                        "action": "request",
                        "requesting_device_id": "RJYKSTBOIE",
                        "request_id": "1495474790150.19"
                    },
                    "type": "m.room_key_request"
                }
                """.trimIndent()

        val serializer = AccountEvent.serializer(RoomKeyRequestContent.Request.serializer())
        val event = MatrixJson.decodeFromString(serializer, json)

        assertEquals("m.megolm.v1.aes-sha2", event.content.body.algorithm)
        assertEquals("!Cuyf34gef24t:localhost", event.content.body.roomId)
        assertEquals("X3lUlvLELLYxeTx4yOVu6UDpasGEVO0Jbu+QFnm0cKQ", event.content.body.sessionId)
        assertEquals("RF3s+E7RkTQTGF2d8Deol0FkQvgII2aJDf3/Jp5mxVU", event.content.body.senderKey)
        // assertEquals("request", event.content.action)
        assertEquals("RJYKSTBOIE", event.content.requestingDeviceId)
        assertEquals("1495474790150.19", event.content.requestId)
        assertEquals("m.room_key_request", event.type)

        val workaround = AccountEvent.serializer(RoomKeyRequestContent.serializer())
        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(workaround, event))
    }

    @Test
    fun testStickerEvent() {
        // language=json
        val json = """
                {
                    "content": {
                        "body": "Landing",
                        "info": {
                            "mimetype": "image/png",
                            "thumbnail_info": {
                                "mimetype": "image/png",
                                "h": 200,
                                "w": 140,
                                "size": 73602
                            },
                            "h": 200,
                            "thumbnail_url": "mxc://matrix.org/sHhqkFCvSkFwtmvtETOtKnLP",
                            "w": 140,
                            "size": 73602
                        },
                        "url": "mxc://matrix.org/sHhqkFCvSkFwtmvtETOtKnLP"
                    },
                    "type": "m.sticker",
                    "event_id": "${'$'}143273582443PhrSn:example.org",
                    "room_id": "!jEsUZKDJdhlrceRyVU:example.org",
                    "sender": "@example:example.org",
                    "origin_server_ts": 1432735824653,
                    "unsigned": {
                        "age": 1234
                    }
                }
                """.trimIndent()

        val serializer = MessageEvent.serializer(StickerContent.serializer())
        val event = MatrixJson.decodeFromString(serializer, json)

        assertEquals("Landing", event.content.body)
        assertEquals("image/png", event.content.info.mimeType)
        assertEquals("image/png", event.content.info.thumbnailInfo?.mimeType)
        assertEquals(200, event.content.info.thumbnailInfo?.height)
        assertEquals(140, event.content.info.thumbnailInfo?.width)
        assertEquals(73602, event.content.info.thumbnailInfo?.size)
        assertEquals(200, event.content.info.height)
        assertEquals("mxc://matrix.org/sHhqkFCvSkFwtmvtETOtKnLP", event.content.info.thumbnailUrl)
        assertEquals(140, event.content.info.width)
        assertEquals(73602, event.content.info.size)
        assertEquals("mxc://matrix.org/sHhqkFCvSkFwtmvtETOtKnLP", event.content.url)
        assertEquals("m.sticker", event.type)
        assertEquals("$143273582443PhrSn:example.org", event.eventId)
        assertEquals("!jEsUZKDJdhlrceRyVU:example.org", event.roomId)
        assertEquals("@example:example.org", event.sender)
        assertEquals(1432735824653, event.originServerTimestamp)
        assertEquals(1234, event.unsigned?.age)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, event))
    }

    @Test
    fun testTagEvent() {
        // language=json
        val json = """
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
                """.trimIndent()

        val serializer = AccountEvent.serializer(TagContent.serializer())
        val event = MatrixJson.decodeFromString(serializer, json)

        assertEquals(0.9, event.content.tags["u.work"]?.order)
        assertEquals("m.tag", event.type)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, event))
    }

    @Test
    fun testTypingEvent() {
        // language=json
        val json = """
                {
                    "content": {
                        "user_ids": [
                            "@alice:matrix.org",
                            "@bob:example.com"
                        ]
                    },
                    "type": "m.typing",
                    "room_id": "!jEsUZKDJdhlrceRyVU:example.org"
                }
                """.trimIndent()

        val serializer = AccountEvent.serializer(TypingContent.serializer())
        val event = MatrixJson.decodeFromString(serializer, json)

        assertEquals(2, event.content.userIds.size)
        assertEquals("@alice:matrix.org", event.content.userIds[0])
        assertEquals("@bob:example.com", event.content.userIds[1])
        assertEquals("m.typing", event.type)
        // FIXME assertEquals("!jEsUZKDJdhlrceRyVU:example.org", event.roomId)
        //
        // FIXME assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, event))
        // Needs EDU class
    }
}
