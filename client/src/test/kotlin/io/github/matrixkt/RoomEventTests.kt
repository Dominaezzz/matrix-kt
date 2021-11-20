package io.github.matrixkt

import io.github.matrixkt.models.events.*
import io.github.matrixkt.models.events.contents.call.HangupContent
import io.github.matrixkt.models.events.contents.room.CanonicalAliasContent
import io.github.matrixkt.utils.MatrixJson
import kotlinx.serialization.json.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RoomEventTests {
    @Test
    fun testSerializationOfRoomEvent() {
        val json = buildJsonObject {
            put("type", "m.room.canonical_alias")
            putJsonObject("content") {
                put("alias", "#somewhere:localhost")
                putJsonArray("alt_aliases") {
                    add("#somewhere:example.org")
                    add("#myroom:example.com")
                }
            }
            put("event_id", "${'$'}143273582443PhrSn:example.org")
            put("room_id", "!jEsUZKDJdhlrceRyVU:example.org")
            put("sender", "@example:example.org")
            put("origin_server_ts", 1432735824653)
            putJsonObject("unsigned") {
                put("age", 1234)
            }
            put("state_key", "")
            putJsonObject("prev_content") {
                put("alias", "#somewhere:localhost")
            }
        }

        val event = RoomEvent(
            type = "m.room.canonical_alias",
            content = CanonicalAliasContent(
                alias = "#somewhere:localhost",
                altAliases = listOf(
                    "#somewhere:example.org",
                    "#myroom:example.com"
                )
            ),
            eventId = "${'$'}143273582443PhrSn:example.org",
            roomId = "!jEsUZKDJdhlrceRyVU:example.org",
            sender = "@example:example.org",
            originServerTimestamp = 1432735824653,
            unsigned = UnsignedData(age = 1234),
            stateKey = "",
            prevContent = CanonicalAliasContent(
                alias = "#somewhere:localhost",
            )
        )

        assertEquals(json, MatrixJson.encodeToJsonElement(event))
    }

    @Test
    fun testDeserializationOfMessageEvent() {
        val json = buildJsonObject {
            put("type", "m.call.hangup")
            putJsonObject("content") {
                put("version", 0)
                put("call_id", "12345")
            }
            put("event_id", "${'$'}143273582443PhrSn:example.org")
            put("room_id", "!jEsUZKDJdhlrceRyVU:example.org")
            put("sender", "@example:example.org")
            put("origin_server_ts", 1432735824653)
            putJsonObject("unsigned") {
                put("age", 1234)
            }
            put("state_key", JsonNull)
            put("prev_content", JsonNull)
        }

        val event = MatrixJson.decodeFromJsonElement<RoomEvent<HangupContent, UnsignedData>>(json)
        assertEquals("m.call.hangup", event.type)
        assertEquals(HangupContent(version = 0, callId = "12345"), event.content)
        assertEquals("${'$'}143273582443PhrSn:example.org", event.eventId)
        assertEquals("@example:example.org", event.sender)
        assertEquals(1432735824653, event.originServerTimestamp)
        assertEquals(UnsignedData(age = 1234), event.unsigned)
        assertTrue(event !is StateEvent)
    }

    @Test
    fun testDeserializationOfStateEvent() {
        val json = buildJsonObject {
            put("type", "org.example.custom.event")
            putJsonObject("content") {
                put("key", "value")
            }
            put("event_id", "${'$'}143273582443PhrSn:example.org")
            put("room_id", "!jEsUZKDJdhlrceRyVU:example.org")
            put("sender", "@example:example.org")
            put("origin_server_ts", 1432735824653)
            putJsonObject("unsigned") {
                put("age", 1234)
            }
            put("state_key", "ArbitraryString")
            putJsonObject("prev_content") {
                put("key", "old_value")
            }
        }

        val event = MatrixJson.decodeFromJsonElement<RoomEvent<JsonObject, JsonObject>>(json)
        assertEquals("org.example.custom.event", event.type)
        assertEquals(buildJsonObject { put("key", "value") }, event.content)
        assertEquals("${'$'}143273582443PhrSn:example.org", event.eventId)
        assertEquals("@example:example.org", event.sender)
        assertEquals(1432735824653, event.originServerTimestamp)
        assertEquals(buildJsonObject { put("age", 1234) }, event.unsigned)
        assertTrue(event is StateEvent)
        assertEquals("ArbitraryString", event.stateKey)
        assertEquals(buildJsonObject { put("key", "old_value") }, event.prevContent)
    }
}
