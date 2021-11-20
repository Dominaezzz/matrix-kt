package io.github.matrixkt

import io.github.matrixkt.models.Presence
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
import kotlin.test.assertIs

class EventTypeTests {
    @Test
    fun testAcceptedTermsEvent() {
        // language=json
        val json = """
            {
                "accepted": [
                    "https://example.org/somewhere/terms-1.2-en.html",
                    "https://example.org/somewhere/privacy-1.2-en.html"
                ]
            }
        """.trimIndent()

        val serializer = AcceptedTermsContent.serializer()
        val content = MatrixJson.decodeFromString(serializer, json)

        assertEquals(2, content.accepted.size)
        assertEquals("https://example.org/somewhere/terms-1.2-en.html", content.accepted[0])
        assertEquals("https://example.org/somewhere/privacy-1.2-en.html", content.accepted[1])

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, content))
    }

    @Test
    fun testCallAnswerEvent() {
        // language=json
        val json = """
            {
                "version": 0,
                "call_id": "12345",
                "lifetime": 60000,
                "answer": {
                    "type": "answer",
                    "sdp": "v=0\r\no=- 6584580628695956864 2 IN IP4 127.0.0.1[...]"
                }
            }
        """.trimIndent()

        val serializer = AnswerContent.serializer()
        val content = MatrixJson.decodeFromString(serializer, json)

        assertEquals(0, content.version)
        assertEquals("12345", content.callId)
        // assertEquals(60000, content.lifetime)
        assertEquals("answer", content.answer.type)
        assertEquals("v=0\r\no=- 6584580628695956864 2 IN IP4 127.0.0.1[...]", content.answer.sdp)

        // FIXME assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, content))
        // This fails because of `lifetime` field.
    }

    @Test
    fun testCallCandidatesEvent() {
        // language=json
        val json = """
            {
                "version": 0,
                "call_id": "12345",
                "candidates": [
                    {
                        "sdpMid": "audio",
                        "sdpMLineIndex": 0,
                        "candidate": "candidate:863018703 1 udp 2122260223 10.9.64.156 43670 typ host generation 0"
                    }
                ]
            }
        """.trimIndent()

        val serializer = CandidatesContent.serializer()
        val content = MatrixJson.decodeFromString(serializer, json)

        assertEquals(0, content.version)
        assertEquals("12345", content.callId)
        assertEquals(1, content.candidates.size)
        assertEquals("audio", content.candidates[0].sdpMid)
        assertEquals(0, content.candidates[0].sdpMLineIndex)
        assertEquals("candidate:863018703 1 udp 2122260223 10.9.64.156 43670 typ host generation 0",
            content.candidates[0].candidate)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, content))
    }

    @Test
    fun testCallHangupEvent() {
        // language=json
        val json = """
            {
                "version": 0,
                "call_id": "12345"
            }
        """.trimIndent()

        val serializer = HangupContent.serializer()
        val content = MatrixJson.decodeFromString(serializer, json)

        assertEquals(0, content.version)
        assertEquals("12345", content.callId)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, content))
    }

    @Test
    fun testCallInviteEvent() {
        // language=json
        val json = """
            {
                "version": 0,
                "call_id": "12345",
                "lifetime": 60000,
                "offer": {
                    "type": "offer",
                    "sdp": "v=0\r\no=- 6584580628695956864 2 IN IP4 127.0.0.1[...]"
                }
            }
        """.trimIndent()

        val serializer = InviteContent.serializer()
        val content = MatrixJson.decodeFromString(serializer, json)

        assertEquals(0, content.version)
        assertEquals("12345", content.callId)
        assertEquals(60000, content.lifetime)
        assertEquals("offer", content.offer.type)
        assertEquals("v=0\r\no=- 6584580628695956864 2 IN IP4 127.0.0.1[...]",
            content.offer.sdp)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, content))
    }

    @Test
    fun testDirectEvent() {
        // language=json
        val json = """
            {
                "@bob:example.com": [
                    "!abcdefgh:example.com",
                    "!hgfedcba:example.com"
                ]
            }
        """.trimIndent()

        val serializer = DirectContent.serializer()
        val content = MatrixJson.decodeFromString(serializer, json)

        assertEquals(2, content["@bob:example.com"]!!.size)
        assertEquals("!abcdefgh:example.com", content["@bob:example.com"]!![0])
        assertEquals("!hgfedcba:example.com", content["@bob:example.com"]!![1])

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, content))
    }

    @Test
    fun testDummyEvent() {
        // language=json
        val json = """
            {
            }
        """.trimIndent()

        val serializer = DummyContent.serializer()
        val content = MatrixJson.decodeFromString(serializer, json)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, content))
    }

    @Test
    fun testForwardedRoomKeyEvent() {
        // language=json
        val json = """
            {
                "algorithm": "m.megolm.v1.aes-sha2",
                "room_id": "!Cuyf34gef24t:localhost",
                "session_id": "X3lUlvLELLYxeTx4yOVu6UDpasGEVO0Jbu+QFnm0cKQ",
                "session_key": "AgAAAADxKHa9uFxcXzwYoNueL5Xqi69IkD4sni8Llf...",
                "sender_key": "RF3s+E7RkTQTGF2d8Deol0FkQvgII2aJDf3/Jp5mxVU",
                "sender_claimed_ed25519_key": "aj40p+aw64yPIdsxoog8jhPu9i7l7NcFRecuOQblE3Y",
                "forwarding_curve25519_key_chain": [
                    "hPQNcabIABgGnx3/ACv/jmMmiQHoeFfuLB17tzWp6Hw"
                ]
            }
        """.trimIndent()

        val serializer = ForwardedRoomKeyContent.serializer()
        val content = MatrixJson.decodeFromString(serializer, json)

        assertEquals("m.megolm.v1.aes-sha2", content.algorithm)
        assertEquals("!Cuyf34gef24t:localhost", content.roomId)
        assertEquals("X3lUlvLELLYxeTx4yOVu6UDpasGEVO0Jbu+QFnm0cKQ", content.sessionId)
        assertEquals("AgAAAADxKHa9uFxcXzwYoNueL5Xqi69IkD4sni8Llf...", content.sessionKey)
        assertEquals("RF3s+E7RkTQTGF2d8Deol0FkQvgII2aJDf3/Jp5mxVU", content.senderKey)
        assertEquals("aj40p+aw64yPIdsxoog8jhPu9i7l7NcFRecuOQblE3Y",
            content.senderClaimedEd25519Key)
        assertEquals(1, content.forwardingCurve25519KeyChain.size)
        assertEquals("hPQNcabIABgGnx3/ACv/jmMmiQHoeFfuLB17tzWp6Hw",
            content.forwardingCurve25519KeyChain[0])

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, content))
    }

    @Test
    fun testFullyReadEvent() {
        // language=json
        val json = """
            {
                "event_id": "${'$'}someplace:example.org"
            }
        """.trimIndent()

        val serializer = FullyReadContent.serializer()
        val content = MatrixJson.decodeFromString(serializer, json)

        assertEquals("\$someplace:example.org", content.eventId)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, content))
    }

    @Test
    fun testIdentityServerEvent() {
        // language=json
        val json = """
            {
                "base_url": "https://example.org"
            }
        """.trimIndent()

        val serializer = IdentityServerContent.serializer()
        val content = MatrixJson.decodeFromString(serializer, json)

        assertEquals("https://example.org", content.baseUrl)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, content))
    }

    @Test
    fun testIgnoredUserListEvent() {
        // language=json
        val json = """
            {
                "ignored_users": {
                    "@someone:example.org": {
                    }
                }
            }
        """.trimIndent()

        val serializer = IgnoredUserListContent.serializer()
        val content = MatrixJson.decodeFromString(serializer, json)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, content))
    }

    @Test
    fun testKeyVerificationAcceptEvent() {
        // language=json
        val json = """
            {
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
        """.trimIndent()

        val serializer = AcceptContent.ToDevice.serializer()
        val content = MatrixJson.decodeFromString(serializer, json)

        assertEquals("S0meUniqueAndOpaqueString", content.transactionId)
        val eventContent = content as AcceptContent.SasV1 // assertEquals("m.sas.v1", content.method)
        assertEquals("curve25519", eventContent.keyAgreementProtocol)
        assertEquals("sha256", eventContent.hash)
        assertEquals("hkdf-hmac-sha256", eventContent.messageAuthenticationCode)
        assertEquals(2, eventContent.shortAuthenticationString.size)
        assertEquals("decimal", eventContent.shortAuthenticationString[0])
        assertEquals("emoji", eventContent.shortAuthenticationString[1])
        assertEquals("fQpGIW1Snz+pwLZu6sTy2aHy/DYWWTspTJRPyNp0PKkymfIsNffysMl6ObMMFdIJhk6g6pwlIqZ54rxo8SLmAg",
            eventContent.commitment)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, content))
    }

    @Test
    fun testKeyVerificationCancelEvent() {
        // language=json
        val json = """
            {
                "transaction_id": "S0meUniqueAndOpaqueString",
                "code": "m.user",
                "reason": "User rejected the key verification request"
            }
        """.trimIndent()

        val serializer = CancelContent.ToDevice.serializer()
        val content = MatrixJson.decodeFromString(serializer, json)

        assertEquals("S0meUniqueAndOpaqueString", content.transactionId)
        assertEquals("m.user", content.code)
        assertEquals("User rejected the key verification request", content.reason)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, content))
    }

    @Test
    fun testKeyVerificationKeyEvent() {
        // language=json
        val json = """
            {
                "transaction_id": "S0meUniqueAndOpaqueString",
                "key": "fQpGIW1Snz+pwLZu6sTy2aHy/DYWWTspTJRPyNp0PKkymfIsNffysMl6ObMMFdIJhk6g6pwlIqZ54rxo8SLmAg"
            }
        """.trimIndent()

        val serializer = KeyContent.ToDevice.serializer()
        val content = MatrixJson.decodeFromString(serializer, json)

        assertEquals("S0meUniqueAndOpaqueString", content.transactionId)
        assertEquals("fQpGIW1Snz+pwLZu6sTy2aHy/DYWWTspTJRPyNp0PKkymfIsNffysMl6ObMMFdIJhk6g6pwlIqZ54rxo8SLmAg",
            content.key)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, content))
    }

    @Test
    fun testKeyVerificationMacEvent() {
        // language=json
        val json = """
            {
                "transaction_id": "S0meUniqueAndOpaqueString",
                "keys": "2Wptgo4CwmLo/Y8B8qinxApKaCkBG2fjTWB7AbP5Uy+aIbygsSdLOFzvdDjww8zUVKCmI02eP9xtyJxc/cLiBA",
                "mac": {
                    "ed25519:ABCDEF": "fQpGIW1Snz+pwLZu6sTy2aHy/DYWWTspTJRPyNp0PKkymfIsNffysMl6ObMMFdIJhk6g6pwlIqZ54rxo8SLmAg"
                }
            }
        """.trimIndent()

        val serializer = MacContent.ToDevice.serializer()
        val content = MatrixJson.decodeFromString(serializer, json)

        assertEquals("S0meUniqueAndOpaqueString", content.transactionId)
        assertEquals("2Wptgo4CwmLo/Y8B8qinxApKaCkBG2fjTWB7AbP5Uy+aIbygsSdLOFzvdDjww8zUVKCmI02eP9xtyJxc/cLiBA",
            content.keys)
        assertEquals("fQpGIW1Snz+pwLZu6sTy2aHy/DYWWTspTJRPyNp0PKkymfIsNffysMl6ObMMFdIJhk6g6pwlIqZ54rxo8SLmAg",
            content.mac["ed25519:ABCDEF"]!!)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, content))
    }

    @Test
    fun testKeyVerificationRequestEvent() {
        // language=json
        val json = """
            {
                "from_device": "AliceDevice2",
                "transaction_id": "S0meUniqueAndOpaqueString",
                "methods": [
                    "m.sas.v1"
                ],
                "timestamp": 1559598944869
            }
        """.trimIndent()

        val serializer = RequestContent.ToDevice.serializer()
        val content = MatrixJson.decodeFromString(serializer, json)

        assertEquals("AliceDevice2", content.fromDevice)
        assertEquals("S0meUniqueAndOpaqueString", content.transactionId)
        assertEquals(1, content.methods.size)
        assertEquals("m.sas.v1", content.methods[0])
        assertEquals(1559598944869, content.timestamp)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, content))
    }

    @Test
    fun testKeyVerificationStartSasV1Event() {
        // language=json
        val json = """
            {
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
        """.trimIndent()

        val serializer = StartContent.SasV1.ToDevice.serializer()
        val content = MatrixJson.decodeFromString(serializer, json)

        assertEquals("BobDevice1", content.fromDevice)
        assertEquals("S0meUniqueAndOpaqueString", content.transactionId)
        // assertEquals("m.sas.v1", content.method)
        assertEquals(1, content.keyAgreementProtocols.size)
        assertEquals("curve25519", content.keyAgreementProtocols[0])
        assertEquals(1, content.hashes.size)
        assertEquals("sha256", content.hashes[0])
        assertEquals(1, content.messageAuthenticationCodes.size)
        assertEquals("hkdf-hmac-sha256", content.messageAuthenticationCodes[0])
        assertEquals(2, content.shortAuthenticationString.size)
        assertEquals("decimal", content.shortAuthenticationString[0])
        assertEquals("emoji", content.shortAuthenticationString[1])

        val workaround = StartContent.ToDevice.serializer()
        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(workaround, content))
    }

    @Test
    fun testPolicyRuleRoomEvent() {
        // language=json
        val json = """
            {
                "entity": "#*:example.org",
                "recommendation": "m.ban",
                "reason": "undesirable content"
            }
        """.trimIndent()

        val serializer = RoomContent.serializer()
        val content = MatrixJson.decodeFromString(serializer, json)

        assertEquals("#*:example.org", content.entity)
        assertEquals("m.ban", content.recommendation)
        assertEquals("undesirable content", content.reason)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, content))
    }

    @Test
    fun testPolicyRuleServerEvent() {
        // language=json
        val json = """
            {
                "entity": "*.example.org",
                "recommendation": "m.ban",
                "reason": "undesirable engagement"
            }
        """.trimIndent()

        val serializer = ServerContent.serializer()
        val content = MatrixJson.decodeFromString(serializer, json)

        assertEquals("*.example.org", content.entity)
        assertEquals("m.ban", content.recommendation)
        assertEquals("undesirable engagement", content.reason)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, content))
    }

    @Test
    fun testPolicyRuleUserEvent() {
        // language=json
        val json = """
            {
                "entity": "@alice*:example.org",
                "recommendation": "m.ban",
                "reason": "undesirable behaviour"
            }
        """.trimIndent()

        val serializer = UserContent.serializer()
        val content = MatrixJson.decodeFromString(serializer, json)

        assertEquals("@alice*:example.org", content.entity)
        assertEquals("m.ban", content.recommendation)
        assertEquals("undesirable behaviour", content.reason)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, content))
    }

    @Test
    fun testPresenceEvent() {
        // language=json
        val json = """
            {
                "avatar_url": "mxc://localhost:wefuiwegh8742w",
                "last_active_ago": 2478593,
                "presence": "online",
                "currently_active": false,
                "status_msg": "Making cupcakes"
            }
        """.trimIndent()

        val serializer = PresenceContent.serializer()
        val content = MatrixJson.decodeFromString(serializer, json)

        assertEquals("mxc://localhost:wefuiwegh8742w", content.avatarUrl)
        assertEquals(2478593, content.lastActiveAgo)
        assertEquals(Presence.ONLINE, content.presence)
        assertEquals(false, content.currentlyActive)
        assertEquals("Making cupcakes", content.statusMessage)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, content))
    }

    @Test
    fun testPushRulesEvent() {
        // language=json
        val json = """
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
            }
        """.trimIndent()

        val serializer = PushRulesContent.serializer()
        val content = MatrixJson.decodeFromString(serializer, json)

        assertEquals(1, content.global!!.content.size)
        assertEquals(3, content.global!!.content[0].actions.size)
        assertEquals("notify", content.global!!.content[0].actions[0].jsonPrimitive.content)
        assertEquals("sound", content.global!!.content[0].actions[1].jsonObject["set_tweak"]!!.jsonPrimitive.content)
        assertEquals("default", content.global!!.content[0].actions[1].jsonObject["value"]!!.jsonPrimitive.content)
        assertEquals("highlight", content.global!!.content[0].actions[2].jsonObject["set_tweak"]!!.jsonPrimitive.content)
        assertEquals(true, content.global!!.content[0].default)
        assertEquals(true, content.global!!.content[0].enabled)
        assertEquals("alice", content.global!!.content[0].pattern)
        assertEquals(".m.rule.contains_user_name", content.global!!.content[0].ruleId)
        assertEquals(2, content.global!!.override.size)
        assertEquals(1, content.global!!.override[0].actions.size)
        assertEquals("dont_notify", content.global!!.override[0].actions[0].jsonPrimitive.content)
        assertEquals(0, content.global!!.override[0].conditions.size)
        assertEquals(true, content.global!!.override[0].default)
        assertEquals(false, content.global!!.override[0].enabled)
        assertEquals(".m.rule.master", content.global!!.override[0].ruleId)
        assertEquals(1, content.global!!.override[1].actions.size)
        assertEquals("dont_notify", content.global!!.override[1].actions[0].jsonPrimitive.content)
        assertEquals(1, content.global!!.override[1].conditions.size)
        assertEquals("content.msgtype", content.global!!.override[1].conditions[0].key)
        assertEquals("event_match", content.global!!.override[1].conditions[0].kind)
        assertEquals("m.notice", content.global!!.override[1].conditions[0].pattern)
        assertEquals(true, content.global!!.override[1].default)
        assertEquals(true, content.global!!.override[1].enabled)
        assertEquals(".m.rule.suppress_notices", content.global!!.override[1].ruleId)
        assertEquals(0, content.global!!.room.size)
        assertEquals(0, content.global!!.sender.size)
        assertEquals(6, content.global!!.underride.size)
        assertEquals(3, content.global!!.underride[0].actions.size)
        assertEquals("notify", content.global!!.underride[0].actions[0].jsonPrimitive.content)
        assertEquals("sound", content.global!!.underride[0].actions[1].jsonObject["set_tweak"]!!.jsonPrimitive.content)
        assertEquals("ring", content.global!!.underride[0].actions[1].jsonObject["value"]!!.jsonPrimitive.content)
        assertEquals("highlight", content.global!!.underride[0].actions[2].jsonObject["set_tweak"]!!.jsonPrimitive.content)
        assertEquals(false, content.global!!.underride[0].actions[2].jsonObject["value"]!!.jsonPrimitive.boolean)
        assertEquals(1, content.global!!.underride[0].conditions.size)
        assertEquals("type", content.global!!.underride[0].conditions[0].key)
        assertEquals("event_match", content.global!!.underride[0].conditions[0].kind)
        assertEquals("m.call.invite", content.global!!.underride[0].conditions[0].pattern)
        assertEquals(true, content.global!!.underride[0].default)
        assertEquals(true, content.global!!.underride[0].enabled)
        assertEquals(".m.rule.call", content.global!!.underride[0].ruleId)
        assertEquals(3, content.global!!.underride[1].actions.size)
        assertEquals("notify", content.global!!.underride[1].actions[0].jsonPrimitive.jsonPrimitive.content)
        assertEquals("sound", content.global!!.underride[1].actions[1].jsonObject["set_tweak"]!!.jsonPrimitive.content)
        assertEquals("default", content.global!!.underride[1].actions[1].jsonObject["value"]!!.jsonPrimitive.content)
        assertEquals("highlight", content.global!!.underride[1].actions[2].jsonObject["set_tweak"]!!.jsonPrimitive.content)
        assertEquals(1, content.global!!.underride[1].conditions.size)
        assertEquals("contains_display_name", content.global!!.underride[1].conditions[0].kind)
        assertEquals(true, content.global!!.underride[1].default)
        assertEquals(true, content.global!!.underride[1].enabled)
        assertEquals(".m.rule.contains_display_name", content.global!!.underride[1].ruleId)
        assertEquals(3, content.global!!.underride[2].actions.size)
        assertEquals("notify", content.global!!.underride[2].actions[0].jsonPrimitive.content)
        assertEquals("sound", content.global!!.underride[2].actions[1].jsonObject["set_tweak"]!!.jsonPrimitive.content)
        assertEquals("default", content.global!!.underride[2].actions[1].jsonObject["value"]!!.jsonPrimitive.content)
        assertEquals("highlight", content.global!!.underride[2].actions[2].jsonObject["set_tweak"]!!.jsonPrimitive.content)
        assertEquals(false, content.global!!.underride[2].actions[2].jsonObject["value"]!!.jsonPrimitive.boolean)
        assertEquals(2, content.global!!.underride[2].conditions.size)
        assertEquals("room_member_count", content.global!!.underride[2].conditions[0].kind)
        assertEquals("2", content.global!!.underride[2].conditions[0].`is`!!)
        assertEquals("event_match", content.global!!.underride[2].conditions[1].kind)
        assertEquals("type", content.global!!.underride[2].conditions[1].key)
        assertEquals("m.room.message", content.global!!.underride[2].conditions[1].pattern)
        assertEquals(true, content.global!!.underride[2].default)
        assertEquals(true, content.global!!.underride[2].enabled)
        assertEquals(".m.rule.room_one_to_one", content.global!!.underride[2].ruleId)
        assertEquals(3, content.global!!.underride[3].actions.size)
        assertEquals("notify", content.global!!.underride[3].actions[0].jsonPrimitive.content)
        assertEquals("sound", content.global!!.underride[3].actions[1].jsonObject["set_tweak"]!!.jsonPrimitive.content)
        assertEquals("default", content.global!!.underride[3].actions[1].jsonObject["value"]!!.jsonPrimitive.content)
        assertEquals("highlight", content.global!!.underride[3].actions[2].jsonObject["set_tweak"]!!.jsonPrimitive.content)
        assertEquals(false, content.global!!.underride[3].actions[2].jsonObject["value"]!!.jsonPrimitive.boolean)
        assertEquals(3, content.global!!.underride[3].conditions.size)
        assertEquals("type", content.global!!.underride[3].conditions[0].key)
        assertEquals("event_match", content.global!!.underride[3].conditions[0].kind)
        assertEquals("m.room.member", content.global!!.underride[3].conditions[0].pattern)
        assertEquals("content.membership", content.global!!.underride[3].conditions[1].key)
        assertEquals("event_match", content.global!!.underride[3].conditions[1].kind)
        assertEquals("invite", content.global!!.underride[3].conditions[1].pattern)
        assertEquals("state_key", content.global!!.underride[3].conditions[2].key)
        assertEquals("event_match", content.global!!.underride[3].conditions[2].kind)
        assertEquals("@alice:example.com", content.global!!.underride[3].conditions[2].pattern)
        assertEquals(true, content.global!!.underride[3].default)
        assertEquals(true, content.global!!.underride[3].enabled)
        assertEquals(".m.rule.invite_for_me", content.global!!.underride[3].ruleId)
        assertEquals(2, content.global!!.underride[4].actions.size)
        assertEquals("notify", content.global!!.underride[4].actions[0].jsonPrimitive.content)
        assertEquals("highlight", content.global!!.underride[4].actions[1].jsonObject["set_tweak"]!!.jsonPrimitive.content)
        assertEquals(false, content.global!!.underride[4].actions[1].jsonObject["value"]!!.jsonPrimitive.boolean)
        assertEquals(1, content.global!!.underride[4].conditions.size)
        assertEquals("type", content.global!!.underride[4].conditions[0].key)
        assertEquals("event_match", content.global!!.underride[4].conditions[0].kind)
        assertEquals("m.room.member", content.global!!.underride[4].conditions[0].pattern)
        assertEquals(true, content.global!!.underride[4].default)
        assertEquals(true, content.global!!.underride[4].enabled)
        assertEquals(".m.rule.member_event", content.global!!.underride[4].ruleId)
        assertEquals(2, content.global!!.underride[5].actions.size)
        assertEquals("notify", content.global!!.underride[5].actions[0].jsonPrimitive.content)
        assertEquals("highlight", content.global!!.underride[5].actions[1].jsonObject["set_tweak"]!!.jsonPrimitive.content)
        assertEquals(false, content.global!!.underride[5].actions[1].jsonObject["value"]!!.jsonPrimitive.boolean)
        assertEquals(1, content.global!!.underride[5].conditions.size)
        assertEquals("type", content.global!!.underride[5].conditions[0].key)
        assertEquals("event_match", content.global!!.underride[5].conditions[0].kind)
        assertEquals("m.room.message", content.global!!.underride[5].conditions[0].pattern)
        assertEquals(true, content.global!!.underride[5].default)
        assertEquals(true, content.global!!.underride[5].enabled)
        assertEquals(".m.rule.message", content.global!!.underride[5].ruleId)

        // FIXME assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, content))
        // The defaults are specified in the raw json.
    }

    @Test
    fun testReceiptEvent() {
        // language=json
        val json = """
            {
                "${'$'}1435641916114394fHBLK:matrix.org": {
                    "m.read": {
                        "@rikj:jki.re": {
                            "ts": 1436451550453
                        }
                    }
                }
            }
        """.trimIndent()

        val serializer = ReceiptContent.serializer()
        val content = MatrixJson.decodeFromString(serializer, json)

        assertEquals(1436451550453,
            content["$1435641916114394fHBLK:matrix.org"]!!.read!!["@rikj:jki.re"]!!.timestamp)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, content))
    }

    @Test
    fun testRoomAliasesEvent() {
        // language=json
        val json = """
            {
                "aliases": [
                    "#somewhere:example.org",
                    "#another:example.org"
                ]
            }
        """.trimIndent()

        val serializer = AliasesContent.serializer()
        val content = MatrixJson.decodeFromString(serializer, json)

        assertEquals(2, content.aliases.size)
        assertEquals("#somewhere:example.org", content.aliases[0])
        assertEquals("#another:example.org", content.aliases[1])

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, content))
    }

    @Test
    fun testRoomAvatarEvent() {
        // language=json
        val json = """
            {
                "info": {
                    "h": 398,
                    "w": 394,
                    "mimetype": "image/jpeg",
                    "size": 31037
                },
                "url": "mxc://example.org/JWEIFJgwEIhweiWJE"
            }
        """.trimIndent()

        val serializer = AvatarContent.serializer()
        val content = MatrixJson.decodeFromString(serializer, json)

        assertEquals(398, content.info?.height)
        assertEquals(394, content.info?.width)
        assertEquals("image/jpeg", content.info?.mimeType)
        assertEquals(31037, content.info?.size)
        assertEquals("mxc://example.org/JWEIFJgwEIhweiWJE", content.url)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, content))
    }

    @Test
    fun testRoomCanonicalAliasEvent() {
        // language=json
        val json = """
            {
                "alias": "#somewhere:localhost",
                "alt_aliases": [
                    "#somewhere:example.org",
                    "#myroom:example.com"
                ]
            }
        """.trimIndent()

        val serializer = CanonicalAliasContent.serializer()
        val content = MatrixJson.decodeFromString(serializer, json)

        assertEquals("#somewhere:localhost", content.alias)
        assertEquals(2, content.altAliases.size)
        assertEquals("#somewhere:example.org", content.altAliases[0])
        assertEquals("#myroom:example.com", content.altAliases[1])

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, content))
    }

    @Test
    fun testRoomCreateEvent() {
        // language=json
        val json = """
            {
                "creator": "@example:example.org",
                "room_version": "1",
                "m.federate": true,
                "predecessor": {
                    "event_id": "${'$'}something:example.org",
                    "room_id": "!oldroom:example.org"
                }
            }
        """.trimIndent()

        val serializer = CreateContent.serializer()
        val content = MatrixJson.decodeFromString(serializer, json)

        assertEquals("@example:example.org", content.creator)
        assertEquals("1", content.roomVersion)
        assertEquals(true, content.mFederate!!)
        assertEquals("\$something:example.org", content.predecessor?.eventId)
        assertEquals("!oldroom:example.org", content.predecessor?.roomId)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, content))
    }

    @Test
    fun testRoomEncryptedMegolmEvent() {
        // language=json
        val json = """
            {
                "algorithm": "m.megolm.v1.aes-sha2",
                "ciphertext": "AwgAEnACgAkLmt6qF84IK++J7UDH2Za1YVchHyprqTqsg...",
                "device_id": "RJYKSTBOIE",
                "sender_key": "IlRMeOPX2e0MurIyfWEucYBRVOEEUMrOHqn/8mLqMjA",
                "session_id": "X3lUlvLELLYxeTx4yOVu6UDpasGEVO0Jbu+QFnm0cKQ"
            }
        """.trimIndent()

        val serializer = EncryptedContent.serializer()
        val content = MatrixJson.decodeFromString(serializer, json)

        assertIs<EncryptedContent.MegolmV1>(content)
        assertEquals("AwgAEnACgAkLmt6qF84IK++J7UDH2Za1YVchHyprqTqsg...", content.ciphertext)
        assertEquals("RJYKSTBOIE", content.deviceId)
        assertEquals("IlRMeOPX2e0MurIyfWEucYBRVOEEUMrOHqn/8mLqMjA", content.senderKey)
        assertEquals("X3lUlvLELLYxeTx4yOVu6UDpasGEVO0Jbu+QFnm0cKQ", content.sessionId)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, content))
    }

    @Test
    fun testRoomEncryptedOlmEvent() {
        // language=json
        val json = """
            {
                "algorithm": "m.olm.v1.curve25519-aes-sha2",
                "sender_key": "Szl29ksW/L8yZGWAX+8dY1XyFi+i5wm+DRhTGkbMiwU",
                "ciphertext": {
                    "7qZcfnBmbEGzxxaWfBjElJuvn7BZx+lSz/SvFrDF/z8": {
                        "type": 0,
                        "body": "AwogGJJzMhf/S3GQFXAOrCZ3iKyGU5ZScVtjI0KypTYrW..."
                    }
                }
            }
        """.trimIndent()

        val serializer = EncryptedContent.serializer()
        val content = MatrixJson.decodeFromString(serializer, json)

        assertIs<EncryptedContent.OlmV1>(content)
        assertEquals("Szl29ksW/L8yZGWAX+8dY1XyFi+i5wm+DRhTGkbMiwU", content.senderKey)
        assertEquals(0, content.ciphertext["7qZcfnBmbEGzxxaWfBjElJuvn7BZx+lSz/SvFrDF/z8"]?.type)
        assertEquals("AwogGJJzMhf/S3GQFXAOrCZ3iKyGU5ZScVtjI0KypTYrW...",
            content.ciphertext["7qZcfnBmbEGzxxaWfBjElJuvn7BZx+lSz/SvFrDF/z8"]?.body)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, content))
    }

    @Test
    fun testRoomEncryptionEvent() {
        // language=json
        val json = """
            {
                "algorithm": "m.megolm.v1.aes-sha2",
                "rotation_period_ms": 604800000,
                "rotation_period_msgs": 100
            }
        """.trimIndent()

        val serializer = EncryptionContent.serializer()
        val content = MatrixJson.decodeFromString(serializer, json)

        assertEquals("m.megolm.v1.aes-sha2", content.algorithm)
        assertEquals(604800000, content.rotationPeriodMs)
        assertEquals(100, content.rotationPeriodMsgs)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, content))
    }

    @Test
    fun testRoomGuestAccessEvent() {
        // language=json
        val json = """
            {
                "guest_access": "can_join"
            }
        """.trimIndent()

        val serializer = GuestAccessContent.serializer()
        val content = MatrixJson.decodeFromString(serializer, json)

        assertEquals(GuestAccess.CAN_JOIN, content.guestAccess)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, content))
    }

    @Test
    fun testRoomHistoryVisibilityEvent() {
        // language=json
        val json = """
            {
                "history_visibility": "shared"
            }
        """.trimIndent()

        val serializer = HistoryVisibilityContent.serializer()
        val content = MatrixJson.decodeFromString(serializer, json)

        assertEquals(HistoryVisibility.SHARED, content.historyVisibility)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, content))
    }

    @Test
    fun testRoomJoinRulesEvent() {
        // language=json
        val json = """
            {
                "join_rule": "public"
            }
        """.trimIndent()

        val serializer = JoinRulesContent.serializer()
        val content = MatrixJson.decodeFromString(serializer, json)

        assertEquals(JoinRule.PUBLIC, content.joinRule)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, content))
    }

    @Test
    fun testRoomMemberEvent() {
        // language=json
        val json = """
            {
                "membership": "join",
                "avatar_url": "mxc://example.org/SEsfnsuifSDFSSEF",
                "displayname": "Alice Margatroid"
            }
        """.trimIndent()

        val serializer = MemberContent.serializer()
        val content = MatrixJson.decodeFromString(serializer, json)

        assertEquals(Membership.JOIN, content.membership)
        assertEquals("mxc://example.org/SEsfnsuifSDFSSEF", content.avatarUrl)
        assertEquals("Alice Margatroid", content.displayName)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, content))
    }

    @Test
    fun testRoomMemberThirdPartyInviteEvent() {
        // language=json
        val json = """
            {
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
            }
        """.trimIndent()

        val serializer = MemberContent.serializer()
        val content = MatrixJson.decodeFromString(serializer, json)

        assertEquals(Membership.INVITE, content.membership)
        assertEquals("mxc://example.org/SEsfnsuifSDFSSEF", content.avatarUrl)
        assertEquals("Alice Margatroid", content.displayName)
        assertEquals("alice", content.thirdPartyInvite?.displayName)
        assertEquals("@alice:example.org", content.thirdPartyInvite?.signed?.mxid)
        // FIXME assertEquals("fQpGIW1Snz+pwLZu6sTy2aHy/DYWWTspTJRPyNp0PKkymfIsNffysMl6ObMMFdIJhk6g6pwlIqZ54rxo8SLmAg",
        //     content.thirdPartyInvite?.signed?.signatures["magic.forest"]!!["ed25519:3"]!!)
        assertEquals("abc123", content.thirdPartyInvite?.signed?.token)

        // FIXME assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, content))
        // signatures
    }

    @Test
    fun testRoomMessageAudioEvent() {
        // language=json
        val json = """
            {
                "body": "Bee Gees - Stayin' Alive",
                "url": "mxc://example.org/ffed755USFFxlgbQYZGtryd",
                "info": {
                    "duration": 2140786,
                    "size": 1563685,
                    "mimetype": "audio/mpeg"
                },
                "msgtype": "m.audio"
            }
        """.trimIndent()

        val serializer = MessageContent.serializer()
        val content = MatrixJson.decodeFromString(serializer, json)

        assertIs<MessageContent.Audio>(content)
        assertEquals("Bee Gees - Stayin' Alive", content.body)
        assertEquals("mxc://example.org/ffed755USFFxlgbQYZGtryd", content.url)
        assertEquals(2140786, content.info?.duration)
        assertEquals(1563685, content.info?.size)
        assertEquals("audio/mpeg", content.info?.mimeType)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, content))
    }

    @Test
    fun testRoomMessageEmoteEvent() {
        // language=json
        val json = """
            {
                "body": "thinks this is an example emote",
                "msgtype": "m.emote",
                "format": "org.matrix.custom.html",
                "formatted_body": "thinks <b>this</b> is an example emote"
            }
        """.trimIndent()

        val serializer = MessageContent.serializer()
        val content = MatrixJson.decodeFromString(serializer, json)

        assertIs<MessageContent.Emote>(content)
        assertEquals("thinks this is an example emote", content.body)
        assertEquals("org.matrix.custom.html", content.format)
        assertEquals("thinks <b>this</b> is an example emote", content.formattedBody)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, content))
    }

    @Test
    fun testRoomMessageFileEvent() {
        // language=json
        val json = """
            {
                "body": "something-important.doc",
                "filename": "something-important.doc",
                "info": {
                    "mimetype": "application/msword",
                    "size": 46144
                },
                "msgtype": "m.file",
                "url": "mxc://example.org/FHyPlCeYUSFFxlgbQYZmoEoe"
            }
        """.trimIndent()

        val serializer = MessageContent.serializer()
        val content = MatrixJson.decodeFromString(serializer, json)

        assertIs<MessageContent.File>(content)
        assertEquals("something-important.doc", content.body)
        assertEquals("something-important.doc", content.filename)
        assertEquals("application/msword", content.info?.mimeType)
        assertEquals(46144, content.info?.size)
        assertEquals("mxc://example.org/FHyPlCeYUSFFxlgbQYZmoEoe", content.url)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, content))
    }

    @Test
    fun testRoomMessageImageEvent() {
        // language=json
        val json = """
            {
                "body": "filename.jpg",
                "info": {
                    "h": 398,
                    "w": 394,
                    "mimetype": "image/jpeg",
                    "size": 31037
                },
                "url": "mxc://example.org/JWEIFJgwEIhweiWJE",
                "msgtype": "m.image"
            }
        """.trimIndent()

        val serializer = MessageContent.serializer()
        val content = MatrixJson.decodeFromString(serializer, json)

        assertIs<MessageContent.Image>(content)
        assertEquals("filename.jpg", content.body)
        assertEquals(398, content.info?.height)
        assertEquals(394, content.info?.width)
        assertEquals("image/jpeg", content.info?.mimeType)
        assertEquals(31037, content.info?.size)
        assertEquals("mxc://example.org/JWEIFJgwEIhweiWJE", content.url)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, content))
    }

    @Test
    fun testRoomMessageLocationEvent() {
        // language=json
        val json = """
            {
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
            }
        """.trimIndent()

        val serializer = MessageContent.serializer()
        val content = MatrixJson.decodeFromString(serializer, json)

        assertIs<MessageContent.Location>(content)
        assertEquals("Big Ben, London, UK", content.body)
        assertEquals("geo:51.5008,0.1247", content.geoUri)
        assertEquals("mxc://example.org/FHyPlCeYUSFFxlgbQYZmoEoe", content.info?.thumbnailUrl)
        assertEquals("image/jpeg", content.info?.thumbnailInfo?.mimeType)
        assertEquals(46144, content.info?.thumbnailInfo?.size)
        assertEquals(300, content.info?.thumbnailInfo?.width)
        assertEquals(300, content.info?.thumbnailInfo?.height)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, content))
    }

    @Test
    fun testRoomMessageNoticeEvent() {
        // language=json
        val json = """
            {
                "body": "This is an example notice",
                "msgtype": "m.notice",
                "format": "org.matrix.custom.html",
                "formatted_body": "This is an <strong>example</strong> notice"
            }
        """.trimIndent()

        val serializer = MessageContent.serializer()
        val content = MatrixJson.decodeFromString(serializer, json)

        assertIs<MessageContent.Notice>(content)
        assertEquals("This is an example notice", content.body)
        assertEquals("org.matrix.custom.html", content.format)
        assertEquals("This is an <strong>example</strong> notice", content.formattedBody)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, content))
    }

    @Test
    fun testRoomMessageServerNoticeEvent() {
        // language=json
        val json = """
            {
                "body": "Human-readable message to explain the notice",
                "msgtype": "m.server_notice",
                "server_notice_type": "m.server_notice.usage_limit_reached",
                "admin_contact": "mailto:server.admin@example.org",
                "limit_type": "monthly_active_user"
            }
        """.trimIndent()

        val serializer = MessageContent.serializer()
        val content = MatrixJson.decodeFromString(serializer, json)

        assertIs<MessageContent.ServerNotice>(content)
        assertEquals("Human-readable message to explain the notice", content.body)
        assertEquals("m.server_notice.usage_limit_reached", content.serverNoticeType)
        assertEquals("mailto:server.admin@example.org", content.adminContact)
        assertEquals("monthly_active_user", content.limitType)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, content))
    }

    @Test
    fun testRoomMessageTextEvent() {
        // language=json
        val json = """
            {
                "body": "This is an example text message",
                "msgtype": "m.text",
                "format": "org.matrix.custom.html",
                "formatted_body": "<b>This is an example text message</b>"
            }
        """.trimIndent()

        val serializer = MessageContent.serializer()
        val content = MatrixJson.decodeFromString(serializer, json)

        assertIs<MessageContent.Text>(content)
        assertEquals("This is an example text message", content.body)
        assertEquals("org.matrix.custom.html", content.format)
        assertEquals("<b>This is an example text message</b>", content.formattedBody)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, content))
    }

    @Test
    fun testRoomMessageVideoEvent() {
        // language=json
        val json = """
            {
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
            }
        """.trimIndent()

        val serializer = MessageContent.serializer()
        val content = MatrixJson.decodeFromString(serializer, json)

        assertIs<MessageContent.Video>(content)
        assertEquals("Gangnam Style", content.body)
        assertEquals("mxc://example.org/a526eYUSFFxlgbQYZmo442", content.url)
        assertEquals("mxc://example.org/FHyPlCeYUSFFxlgbQYZmoEoe", content.info?.thumbnailUrl)
        assertEquals("image/jpeg", content.info?.thumbnailInfo?.mimeType)
        assertEquals(46144, content.info?.thumbnailInfo?.size)
        assertEquals(300, content.info?.thumbnailInfo?.width)
        assertEquals(300, content.info?.thumbnailInfo?.height)
        assertEquals(480, content.info?.width)
        assertEquals(320, content.info?.height)
        assertEquals(2140786, content.info?.duration)
        assertEquals(1563685, content.info?.size)
        assertEquals("video/mp4", content.info?.mimeType)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, content))
    }

    @Test
    fun testRoomMessageFeedbackEvent() {
        // language=json
        val json = """
            {
                "type": "delivered",
                "target_event_id": "${'$'}WEIGFHFW:localhost"
            }
        """.trimIndent()

        val serializer = FeedbackContent.serializer()
        val content = MatrixJson.decodeFromString(serializer, json)

        assertEquals("delivered", content.type)
        assertEquals("\$WEIGFHFW:localhost", content.targetEventId)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, content))
    }

    @Test
    fun testRoomNameEvent() {
        // language=json
        val json = """
            {
                "name": "The room name"
            }
        """.trimIndent()

        val serializer = NameContent.serializer()
        val content = MatrixJson.decodeFromString(serializer, json)

        assertEquals("The room name", content.name)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, content))
    }

    @Test
    fun testRoomPinnedEventsEvent() {
        // language=json
        val json = """
            {
                "pinned": [
                    "${'$'}someevent:example.org"
                ]
            }
        """.trimIndent()

        val serializer = PinnedEventsContent.serializer()
        val content = MatrixJson.decodeFromString(serializer, json)

        assertEquals(1, content.pinned.size)
        assertEquals("\$someevent:example.org", content.pinned[0])

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, content))
    }

    @Test
    fun testRoomPowerLevelsEvent() {
        // language=json
        val json = """
            {
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
            }
        """.trimIndent()

        val serializer = PowerLevelsContent.serializer()
        val content = MatrixJson.decodeFromString(serializer, json)

        assertEquals(50, content.ban)
        assertEquals(2, content.events.size)
        assertEquals(100, content.events["m.room.name"])
        assertEquals(100, content.events["m.room.power_levels"])
        assertEquals(0, content.eventsDefault)
        assertEquals(50, content.invite)
        assertEquals(50, content.kick)
        assertEquals(50, content.redact)
        assertEquals(50, content.stateDefault)
        assertEquals(100, content.users["@example:localhost"])
        assertEquals(0, content.usersDefault)
        assertEquals(20, content.notifications?.room)

        // Default values are being ignored.
        // assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, content))
    }

    @Test
    fun testRoomRedactionEvent() {
        // language=json
        val json = """
            {
                "reason": "Spamming"
            }
        """.trimIndent()

        val serializer = RedactionContent.serializer()
        val content = MatrixJson.decodeFromString(serializer, json)

        assertEquals("Spamming", content.reason)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, content))
    }

    @Test
    fun testRoomServerAclEvent() {
        // language=json
        val json = """
            {
                "allow_ip_literals": false,
                "allow": [
                    "*"
                ],
                "deny": [
                    "*.evil.com",
                    "evil.com"
                ]
            }
        """.trimIndent()

        val serializer = ServerAclContent.serializer()
        val content = MatrixJson.decodeFromString(serializer, json)

        assertEquals(false, content.allowIpLiterals)
        assertEquals(1, content.allow.size)
        assertEquals("*", content.allow[0])
        assertEquals(2, content.deny.size)
        assertEquals("*.evil.com", content.deny[0])
        assertEquals("evil.com", content.deny[1])

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, content))
    }

    @Test
    fun testRoomThirdPartyInviteEvent() {
        // language=json
        val json = """
            {
                "display_name": "Alice Margatroid",
                "key_validity_url": "https://magic.forest/verifykey",
                "public_key": "abc123",
                "public_keys": [
                    {
                        "public_key": "def456",
                        "key_validity_url": "https://magic.forest/verifykey"
                    }
                ]
            }
        """.trimIndent()

        val serializer = ThirdPartyInviteContent.serializer()
        val content = MatrixJson.decodeFromString(serializer, json)

        assertEquals("Alice Margatroid", content.displayName)
        assertEquals("https://magic.forest/verifykey", content.keyValidityUrl)
        assertEquals("abc123", content.publicKey)
        assertEquals(1, content.publicKeys.size)
        assertEquals("def456", content.publicKeys[0].publicKey)
        assertEquals("https://magic.forest/verifykey", content.publicKeys[0].keyValidityUrl)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, content))
    }

    @Test
    fun testRoomTombstoneEvent() {
        // language=json
        val json = """
            {
                "body": "This room has been replaced",
                "replacement_room": "!newroom:example.org"
            }
        """.trimIndent()

        val serializer = TombstoneContent.serializer()
        val content = MatrixJson.decodeFromString(serializer, json)

        assertEquals("This room has been replaced", content.body)
        assertEquals("!newroom:example.org", content.replacementRoom)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, content))
    }

    @Test
    fun testRoomTopicEvent() {
        // language=json
        val json = """
            {
                "topic": "A room topic"
            }
        """.trimIndent()

        val serializer = TopicContent.serializer()
        val content = MatrixJson.decodeFromString(serializer, json)

        assertEquals("A room topic", content.topic)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, content))
    }

    @Test
    fun testRoomKeyEvent() {
        // language=json
        val json = """
            {
                "algorithm": "m.megolm.v1.aes-sha2",
                "room_id": "!Cuyf34gef24t:localhost",
                "session_id": "X3lUlvLELLYxeTx4yOVu6UDpasGEVO0Jbu+QFnm0cKQ",
                "session_key": "AgAAAADxKHa9uFxcXzwYoNueL5Xqi69IkD4sni8LlfJL7qNBEY..."
            }
        """.trimIndent()

        val serializer = RoomKeyContent.serializer()
        val content = MatrixJson.decodeFromString(serializer, json)

        assertEquals("m.megolm.v1.aes-sha2", content.algorithm)
        assertEquals("!Cuyf34gef24t:localhost", content.roomId)
        assertEquals("X3lUlvLELLYxeTx4yOVu6UDpasGEVO0Jbu+QFnm0cKQ", content.sessionId)
        assertEquals("AgAAAADxKHa9uFxcXzwYoNueL5Xqi69IkD4sni8LlfJL7qNBEY...",
            content.sessionKey)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, content))
    }

    @Test
    fun testRoomKeyRequestCancelRequestEvent() {
        // language=json
        val json = """
            {
                "action": "request_cancellation",
                "requesting_device_id": "RJYKSTBOIE",
                "request_id": "1495474790150.19"
            }
        """.trimIndent()

        val serializer = RoomKeyRequestContent.serializer()
        val content = MatrixJson.decodeFromString(serializer, json)

        assertIs<RoomKeyRequestContent.Cancellation>(content)
        assertEquals("RJYKSTBOIE", content.requestingDeviceId)
        assertEquals("1495474790150.19", content.requestId)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, content))
    }

    @Test
    fun testRoomKeyRequestRequestEvent() {
        // language=json
        val json = """
            {
                "body": {
                    "algorithm": "m.megolm.v1.aes-sha2",
                    "room_id": "!Cuyf34gef24t:localhost",
                    "session_id": "X3lUlvLELLYxeTx4yOVu6UDpasGEVO0Jbu+QFnm0cKQ",
                    "sender_key": "RF3s+E7RkTQTGF2d8Deol0FkQvgII2aJDf3/Jp5mxVU"
                },
                "action": "request",
                "requesting_device_id": "RJYKSTBOIE",
                "request_id": "1495474790150.19"
            }
        """.trimIndent()

        val serializer = RoomKeyRequestContent.serializer()
        val content = MatrixJson.decodeFromString(serializer, json)

        assertIs<RoomKeyRequestContent.Request>(content)
        assertEquals("m.megolm.v1.aes-sha2", content.body.algorithm)
        assertEquals("!Cuyf34gef24t:localhost", content.body.roomId)
        assertEquals("X3lUlvLELLYxeTx4yOVu6UDpasGEVO0Jbu+QFnm0cKQ", content.body.sessionId)
        assertEquals("RF3s+E7RkTQTGF2d8Deol0FkQvgII2aJDf3/Jp5mxVU", content.body.senderKey)
        assertEquals("RJYKSTBOIE", content.requestingDeviceId)
        assertEquals("1495474790150.19", content.requestId)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, content))
    }

    @Test
    fun testStickerEvent() {
        // language=json
        val json = """
            {
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
            }
        """.trimIndent()

        val serializer = StickerContent.serializer()
        val content = MatrixJson.decodeFromString(serializer, json)

        assertEquals("Landing", content.body)
        assertEquals("image/png", content.info.mimeType)
        assertEquals("image/png", content.info.thumbnailInfo?.mimeType)
        assertEquals(200, content.info.thumbnailInfo?.height)
        assertEquals(140, content.info.thumbnailInfo?.width)
        assertEquals(73602, content.info.thumbnailInfo?.size)
        assertEquals(200, content.info.height)
        assertEquals("mxc://matrix.org/sHhqkFCvSkFwtmvtETOtKnLP", content.info.thumbnailUrl)
        assertEquals(140, content.info.width)
        assertEquals(73602, content.info.size)
        assertEquals("mxc://matrix.org/sHhqkFCvSkFwtmvtETOtKnLP", content.url)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, content))
    }

    @Test
    fun testTagEvent() {
        // language=json
        val json = """
            {
                "tags": {
                    "u.work": {
                        "order": 0.9
                    }
                }
            }
        """.trimIndent()

        val serializer = TagContent.serializer()
        val content = MatrixJson.decodeFromString(serializer, json)

        assertEquals(0.9, content.tags["u.work"]?.order)

        assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, content))
    }

    @Test
    fun testTypingEvent() {
        // language=json
        val json = """
            {
                "user_ids": [
                    "@alice:matrix.org",
                    "@bob:example.com"
                ]
            }
        """.trimIndent()

        val serializer = TypingContent.serializer()
        val content = MatrixJson.decodeFromString(serializer, json)

        assertEquals(2, content.userIds.size)
        assertEquals("@alice:matrix.org", content.userIds[0])
        assertEquals("@bob:example.com", content.userIds[1])

        // FIXME assertEquals(MatrixJson.parseToJsonElement(json), MatrixJson.encodeToJsonElement(serializer, content))
        // Needs EDU class
    }
}
