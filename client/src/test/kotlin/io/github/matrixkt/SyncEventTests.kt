package io.github.matrixkt

import io.github.matrixkt.models.events.SyncEvent
import io.github.matrixkt.models.events.SyncStateEvent
import io.github.matrixkt.utils.MatrixJson
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SyncEventTests {
    @Test
    fun testSerialization() {
        val json = buildJsonObject {
            put("type", "org.example.custom.event")
            putJsonObject("content") {
                put("key", "value")
            }
            put("event_id", "${'$'}143273582443PhrSn:example.org")
            put("sender", "@example:example.org")
            put("origin_server_ts", 1432735824653)
            putJsonObject("unsigned") {
                put("age", 1234)
            }
            put("state_key", JsonNull)
            put("prev_content", JsonNull)
            // put("state_key", "ArbitraryString")
            // putJsonObject("prev_content") {
            //     put("key", "old_value")
            // }
        }

        val event = SyncEvent(
            type = "org.example.custom.event",
            content = buildJsonObject { put("key", "value") },
            eventId = "${'$'}143273582443PhrSn:example.org",
            sender = "@example:example.org",
            originServerTimestamp = 1432735824653,
            unsigned = buildJsonObject { put("age", 1234) }
        )

        assertEquals(json, MatrixJson.encodeToJsonElement(event))
    }

    @Test
    fun testDeserializationOfMessageEvent() {
        // language=json
        val json = """
            {
                "type": "org.example.custom.event",
                "content": {
                    "key": "value"
                },
                "event_id": "${'$'}143273582443PhrSn:example.org",
                "sender": "@example:example.org",
                "origin_server_ts": 1432735824653,
                "unsigned": {
                  "age": 1234
                }
            }
        """.trimIndent()

        val event = MatrixJson.decodeFromString<SyncEvent>(json)
        assertEquals("org.example.custom.event", event.type)
        assertEquals(buildJsonObject { put("key", "value") }, event.content)
        assertEquals("${'$'}143273582443PhrSn:example.org", event.eventId)
        assertEquals("@example:example.org", event.sender)
        assertEquals(1432735824653, event.originServerTimestamp)
        assertEquals(buildJsonObject { put("age", 1234) }, event.unsigned)
        assertTrue(event !is SyncStateEvent)
    }

    @Test
    fun testDeserializationOfStateEvent() {
        // language=json
        val json = """
            {
                "type": "org.example.custom.event",
                "content": {
                    "key": "value"
                },
                "event_id": "${'$'}143273582443PhrSn:example.org",
                "room_id": "!jEsUZKDJdhlrceRyVU:example.org",
                "sender": "@example:example.org",
                "origin_server_ts": 1432735824653,
                "unsigned": {
                  "age": 1234
                },
                "state_key": "ArbitraryString"
                "prev_content": {
                    "key": "old_value"
                }
            }
        """.trimIndent()

        val event = MatrixJson.decodeFromString<SyncEvent>(json)
        assertEquals("org.example.custom.event", event.type)
        assertEquals(buildJsonObject { put("key", "value") }, event.content)
        assertEquals("${'$'}143273582443PhrSn:example.org", event.eventId)
        assertEquals("@example:example.org", event.sender)
        assertEquals(1432735824653, event.originServerTimestamp)
        assertEquals(buildJsonObject { put("age", 1234) }, event.unsigned)
        assertTrue(event is SyncStateEvent)
        assertEquals("ArbitraryString", event.stateKey)
        assertEquals(buildJsonObject { put("key", "old_value") }, event.prevContent)
    }
}
