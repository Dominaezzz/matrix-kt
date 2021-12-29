package io.github.matrixkt

import io.github.matrixkt.api.*
import io.github.matrixkt.models.Device
import io.github.matrixkt.models.Invite3pid
import io.github.matrixkt.models.MSISDNValidationRequest
import io.github.matrixkt.models.events.MatrixEvent
import io.github.matrixkt.models.filter.Filter
import io.github.matrixkt.events.contents.TagContent
import io.github.matrixkt.events.push.PushRule
import io.github.matrixkt.models.search.Results
import io.github.matrixkt.models.sync.SyncResponse
import io.github.matrixkt.utils.MatrixJson
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.JsonObject
import kotlin.test.Test

@Suppress("DEPRECATION")
class ApiTests {

    @Test
    fun testGetAccount3PIDsResponse() {
        // language=json
        val json = """ 
            {
                "threepids": [
                    {
                        "added_at": 1535336848756,
                        "address": "monkey@banana.island",
                        "medium": "email",
                        "validated_at": 1535176800000
                    }
                ]
            }
        """

        MatrixJson.decodeFromString<GetAccount3PIDs.Response>(json)
    }

    @Test
    fun testPost3PIDsBody() {
        // language=json
        val json = """ 
            {
                "three_pid_creds": {
                    "client_secret": "d0n'tT3ll",
                    "id_access_token": "abc123_OpaqueString",
                    "id_server": "matrix.org",
                    "sid": "abc123987"
                }
            }
        """

        MatrixJson.decodeFromString<Post3PIDs.Body>(json)
    }

    @Test
    fun testPost3PIDsResponse() {
        // language=json
        val json = """ 
            {
                "submit_url": "https://example.org/path/to/submitToken"
            }
        """

        MatrixJson.decodeFromString<Post3PIDs.Response>(json)
    }

    @Test
    fun testBind3PIDBody() {
        // language=json
        val json = """ 
            {
                "client_secret": "d0n'tT3ll",
                "id_access_token": "abc123_OpaqueString",
                "id_server": "example.org",
                "sid": "abc123987"
            }
        """

        MatrixJson.decodeFromString<Bind3PID.Body>(json)
    }

    @Test
    fun testGetTokenOwnerResponse() {
        // language=json
        val json = """ 
            {
                "user_id": "@joe:example.org"
            }
        """

        MatrixJson.decodeFromString<GetTokenOwner.Response>(json)
    }

    @Test
    fun testGetWhoIsResponse() {
        // language=json
        val json = """ 
            {
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
                },
                "user_id": "@peter:rabbit.rocks"
            }
        """

        MatrixJson.decodeFromString<GetWhoIs.Response>(json)
    }

    @Test
    fun testGetCapabilitiesResponse() {
        // language=json
        val json = """ 
            {
                "capabilities": {
                    "com.example.custom.ratelimit": {
                        "max_requests_per_hour": 600
                    },
                    "m.change_password": {
                        "enabled": false
                    },
                    "m.room_versions": {
                        "available": {
                            "1": "stable",
                            "2": "stable",
                            "3": "unstable",
                            "test-version": "unstable"
                        },
                        "default": "1"
                    }
                }
            }
        """

        MatrixJson.decodeFromString<GetCapabilities.Response>(json)
    }

    @Test
    fun testCreateRoomBody() {
        // language=json
        val json = """ 
            {
                "creation_content": {
                    "m.federate": false
                },
                "name": "The Grand Duke Pub",
                "preset": "public_chat",
                "room_alias_name": "thepub",
                "topic": "All about happy hour"
            }
        """

        MatrixJson.decodeFromString<CreateRoom.Body>(json)
    }

    @Test
    fun testCreateRoomResponse() {
        // language=json
        val json = """ 
            {
                "room_id": "!sefiuhWgwghwWgh:example.com"
            }
        """

        MatrixJson.decodeFromString<CreateRoom.Response>(json)
    }

    @Test
    fun testGetDevicesResponse() {
        // language=json
        val json = """ 
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
        """

        MatrixJson.decodeFromString<GetDevices.Response>(json)
    }

    @Test
    fun testGetDeviceResponse() {
        // language=json
        val json = """ 
            {
                "device_id": "QBUAZIFURK",
                "display_name": "android",
                "last_seen_ip": "1.2.3.4",
                "last_seen_ts": 1474491775024
            }
        """

        MatrixJson.decodeFromString<Device>(json)
    }

    @Test
    fun testGetRoomIdByAliasResponse() {
        // language=json
        val json = """ 
            {
                "room_id": "!abnjk1jdasj98:capuchins.com",
                "servers": [
                    "capuchins.com",
                    "matrix.org",
                    "another.com"
                ]
            }
        """

        MatrixJson.decodeFromString<GetRoomIdByAlias.Response>(json)
    }

    @Test
    fun testSetRoomAliasBody() {
        // language=json
        val json = """ 
            {
                "room_id": "!abnjk1jdasj98:capuchins.com"
            }
        """

        MatrixJson.decodeFromString<SetRoomAlias.Body>(json)
    }

    @Test
    fun testGetEventsResponse() {
        // language=json
        val json = """ 
            {
                "chunk": [
                    {
                        "content": {
                            "body": "This is an example text message",
                            "format": "org.matrix.custom.html",
                            "formatted_body": "<b>This is an example text message</b>",
                            "msgtype": "m.text"
                        },
                        "event_id": "${'$'}143273582443PhrSn:example.org",
                        "origin_server_ts": 1432735824653,
                        "room_id": "!jEsUZKDJdhlrceRyVU:example.org",
                        "sender": "@example:example.org",
                        "type": "m.room.message",
                        "unsigned": {
                            "age": 1234
                        }
                    }
                ],
                "end": "s3457_9_0",
                "start": "s3456_9_0"
            }
        """

        MatrixJson.decodeFromString<GetEvents.Response>(json)
    }

    @Test
    fun testGetOneEventResponse() {
        // language=json
        val json = """ 
            {
                "content": {
                    "body": "This is an example text message",
                    "format": "org.matrix.custom.html",
                    "formatted_body": "<b>This is an example text message</b>",
                    "msgtype": "m.text"
                },
                "event_id": "${'$'}143273582443PhrSn:example.org",
                "origin_server_ts": 1432735824653,
                "room_id": "!jEsUZKDJdhlrceRyVU:example.org",
                "sender": "@example:example.org",
                "type": "m.room.message",
                "unsigned": {
                    "age": 1234
                }
            }
        """

        MatrixJson.decodeFromString<MatrixEvent>(json)
    }

    @Test
    fun testInitialSyncResponse() {
        // language=json
        val json = """ 
            {
                "account_data": [
                    {
                        "content": {
                            "custom_config_key": "custom_config_value"
                        },
                        "type": "org.example.custom.config"
                    }
                ],
                "end": "s3456_9_0",
                "presence": [
                    {
                        "content": {
                            "avatar_url": "mxc://localhost:wefuiwegh8742w",
                            "currently_active": false,
                            "last_active_ago": 2478593,
                            "presence": "online",
                            "status_msg": "Making cupcakes"
                        },
                        "sender": "@example:localhost",
                        "type": "m.presence"
                    }
                ],
                "rooms": [
                    {
                        "account_data": [
                            {
                                "content": {
                                    "tags": {
                                        "work": {
                                            "order": 1
                                        }
                                    }
                                },
                                "type": "m.tag"
                            },
                            {
                                "content": {
                                    "custom_config_key": "custom_config_value"
                                },
                                "type": "org.example.custom.room.config"
                            }
                        ],
                        "membership": "join",
                        "messages": {
                            "chunk": [
                                {
                                    "content": {
                                        "body": "This is an example text message",
                                        "format": "org.matrix.custom.html",
                                        "formatted_body": "<b>This is an example text message</b>",
                                        "msgtype": "m.text"
                                    },
                                    "event_id": "${'$'}143273582443PhrSn:example.org",
                                    "origin_server_ts": 1432735824653,
                                    "room_id": "!TmaZBKYIFrIPVGoUYp:localhost",
                                    "sender": "@example:example.org",
                                    "type": "m.room.message",
                                    "unsigned": {
                                        "age": 1234
                                    }
                                },
                                {
                                    "content": {
                                        "body": "Gangnam Style",
                                        "info": {
                                            "duration": 2140786,
                                            "h": 320,
                                            "mimetype": "video/mp4",
                                            "size": 1563685,
                                            "thumbnail_info": {
                                                "h": 300,
                                                "mimetype": "image/jpeg",
                                                "size": 46144,
                                                "w": 300
                                            },
                                            "thumbnail_url": "mxc://example.org/FHyPlCeYUSFFxlgbQYZmoEoe",
                                            "w": 480
                                        },
                                        "msgtype": "m.video",
                                        "url": "mxc://example.org/a526eYUSFFxlgbQYZmo442"
                                    },
                                    "event_id": "${'$'}143273582443PhrSn:example.org",
                                    "origin_server_ts": 1432735824653,
                                    "room_id": "!TmaZBKYIFrIPVGoUYp:localhost",
                                    "sender": "@example:example.org",
                                    "type": "m.room.message",
                                    "unsigned": {
                                        "age": 1234
                                    }
                                }
                            ],
                            "end": "s3456_9_0",
                            "start": "t44-3453_9_0"
                        },
                        "room_id": "!TmaZBKYIFrIPVGoUYp:localhost",
                        "state": [
                            {
                                "content": {
                                    "join_rule": "public"
                                },
                                "event_id": "${'$'}143273582443PhrSn:example.org",
                                "origin_server_ts": 1432735824653,
                                "room_id": "!TmaZBKYIFrIPVGoUYp:localhost",
                                "sender": "@example:example.org",
                                "state_key": "",
                                "type": "m.room.join_rules",
                                "unsigned": {
                                    "age": 1234
                                }
                            },
                            {
                                "content": {
                                    "avatar_url": "mxc://example.org/SEsfnsuifSDFSSEF",
                                    "displayname": "Alice Margatroid",
                                    "membership": "join"
                                },
                                "event_id": "${'$'}143273582443PhrSn:example.org",
                                "origin_server_ts": 1432735824653,
                                "room_id": "!TmaZBKYIFrIPVGoUYp:localhost",
                                "sender": "@example:example.org",
                                "state_key": "@alice:example.org",
                                "type": "m.room.member",
                                "unsigned": {
                                    "age": 1234
                                }
                            },
                            {
                                "content": {
                                    "creator": "@example:example.org",
                                    "m.federate": true,
                                    "predecessor": {
                                        "event_id": "${'$'}something:example.org",
                                        "room_id": "!oldroom:example.org"
                                    },
                                    "room_version": "1"
                                },
                                "event_id": "${'$'}143273582443PhrSn:example.org",
                                "origin_server_ts": 1432735824653,
                                "room_id": "!TmaZBKYIFrIPVGoUYp:localhost",
                                "sender": "@example:example.org",
                                "state_key": "",
                                "type": "m.room.create",
                                "unsigned": {
                                    "age": 1234
                                }
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
                                    "notifications": {
                                        "room": 20
                                    },
                                    "redact": 50,
                                    "state_default": 50,
                                    "users": {
                                        "@example:localhost": 100
                                    },
                                    "users_default": 0
                                },
                                "event_id": "${'$'}143273582443PhrSn:example.org",
                                "origin_server_ts": 1432735824653,
                                "room_id": "!TmaZBKYIFrIPVGoUYp:localhost",
                                "sender": "@example:example.org",
                                "state_key": "",
                                "type": "m.room.power_levels",
                                "unsigned": {
                                    "age": 1234
                                }
                            }
                        ],
                        "visibility": "private"
                    }
                ]
            }
        """

        MatrixJson.decodeFromString<InitialSync.Response>(json)
    }

    @Test
    fun testJoinRoomResponse() {
        // language=json
        val json = """ 
            {
                "room_id": "!d41d8cd:matrix.org"
            }
        """

        MatrixJson.decodeFromString<JoinRoom.Response>(json)
    }

    @Test
    fun testGetJoinedRoomsResponse() {
        // language=json
        val json = """ 
            {
                "joined_rooms": [
                    "!foo:example.com"
                ]
            }
        """

        MatrixJson.decodeFromString<GetJoinedRooms.Response>(json)
    }

    @Test
    fun testGetLoginFlowsResponse() {
        // language=json
        val json = """ 
            {
                "flows": [
                    {
                        "type": "m.login.password"
                    }
                ]
            }
        """

        MatrixJson.decodeFromString<GetLoginFlows.Response>(json)
    }

    @Test
    fun testLoginBody() {
        // language=json
        val json = """ 
            {
                "identifier": {
                    "type": "m.id.user",
                    "user": "cheeky_monkey"
                },
                "initial_device_display_name": "Jungle Phone",
                "password": "ilovebananas",
                "type": "m.login.password"
            }
        """

        MatrixJson.decodeFromString(Login.Body.serializer(), json)
    }

    @Test
    fun testLoginResponse() {
        // language=json
        val json = """ 
            {
                "access_token": "abc123",
                "device_id": "GHTYAJCE",
                "user_id": "@cheeky_monkey:matrix.org",
                "well_known": {
                    "m.homeserver": {
                        "base_url": "https://example.org"
                    },
                    "m.identity_server": {
                        "base_url": "https://id.example.org"
                    }
                }
            }
        """

        MatrixJson.decodeFromString<Login.Response>(json)
    }

    @Test
    fun testGetNotificationsResponse() {
        // language=json
        val json = """ 
            {
                "next_token": "abcdef",
                "notifications": [
                    {
                        "actions": [
                            "notify"
                        ],
                        "event": {
                            "content": {
                                "body": "This is an example text message",
                                "format": "org.matrix.custom.html",
                                "formatted_body": "<b>This is an example text message</b>",
                                "msgtype": "m.text"
                            },
                            "event_id": "${'$'}143273582443PhrSn:example.org",
                            "origin_server_ts": 1432735824653,
                            "room_id": "!jEsUZKDJdhlrceRyVU:example.org",
                            "sender": "@example:example.org",
                            "type": "m.room.message",
                            "unsigned": {
                                "age": 1234
                            }
                        },
                        "profile_tag": "hcbvkzxhcvb",
                        "read": true,
                        "room_id": "!abcdefg:example.com",
                        "ts": 1475508881945
                    }
                ]
            }
        """

        MatrixJson.decodeFromString<GetNotifications.Response>(json)
    }

    @Test
    fun testGetPresenceResponse() {
        // language=json
        val json = """ 
            {
                "last_active_ago": 420845,
                "presence": "unavailable"
            }
        """

        MatrixJson.decodeFromString<GetPresence.Response>(json)
    }

    @Test
    fun testSetPresenceBody() {
        // language=json
        val json = """ 
            {
                "presence": "online",
                "status_msg": "I am here."
            }
        """

        MatrixJson.decodeFromString<SetPresence.Body>(json)
    }

    @Test
    fun testGetUserProfileResponse() {
        // language=json
        val json = """ 
            {
                "avatar_url": "mxc://matrix.org/SDGdghriugerRg",
                "displayname": "Alice Margatroid"
            }
        """

        MatrixJson.decodeFromString<GetUserProfile.Response>(json)
    }

    @Test
    fun testGetAvatarUrlResponse() {
        // language=json
        val json = """ 
            {
                "avatar_url": "mxc://matrix.org/SDGdghriugerRg"
            }
        """

        MatrixJson.decodeFromString<GetAvatarUrl.Response>(json)
    }

    @Test
    fun testSetAvatarUrlBody() {
        // language=json
        val json = """ 
            {
                "avatar_url": "mxc://matrix.org/wefh34uihSDRGhw34"
            }
        """

        MatrixJson.decodeFromString<SetAvatarUrl.Body>(json)
    }

    @Test
    fun testGetDisplayNameResponse() {
        // language=json
        val json = """ 
            {
                "displayname": "Alice Margatroid"
            }
        """

        MatrixJson.decodeFromString<GetDisplayName.Response>(json)
    }

    @Test
    fun testSetDisplayNameBody() {
        // language=json
        val json = """ 
            {
                "displayname": "Alice Margatroid"
            }
        """

        MatrixJson.decodeFromString<SetDisplayName.Body>(json)
    }

    @Test
    fun testQueryPublicRoomsBody() {
        // language=json
        val json = """ 
            {
                "filter": {
                    "generic_search_term": "foo"
                },
                "include_all_networks": false,
                "limit": 10,
                "third_party_instance_id": "irc"
            }
        """

        MatrixJson.decodeFromString<QueryPublicRooms.Body>(json)
    }

    @Test
    fun testQueryPublicRoomsResponse() {
        // language=json
        val json = """ 
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

        MatrixJson.decodeFromString<QueryPublicRooms.Response>(json)
    }

    @Test
    fun testGetPushersResponse() {
        // language=json
        val json = """ 
            {
                "pushers": [
                    {
                        "app_display_name": "Appy McAppface",
                        "app_id": "face.mcapp.appy.prod",
                        "data": {
                            "url": "https://example.com/_matrix/push/v1/notify"
                        },
                        "device_display_name": "Alice's Phone",
                        "kind": "http",
                        "lang": "en-US",
                        "profile_tag": "xyz",
                        "pushkey": "Xp/MzCt8/9DcSNE9cuiaoT5Ac55job3TdLSSmtmYl4A="
                    }
                ]
            }
        """

        MatrixJson.decodeFromString<GetPushers.Response>(json)
    }

    @Test
    fun testPostPusherBody() {
        // language=json
        val json = """ 
            {
                "app_display_name": "Mat Rix",
                "app_id": "com.example.app.ios",
                "append": false,
                "data": {
                    "format": "event_id_only",
                    "url": "https://push-gateway.location.here/_matrix/push/v1/notify"
                },
                "device_display_name": "iPhone 9",
                "kind": "http",
                "lang": "en",
                "profile_tag": "xxyyzz",
                "pushkey": "APA91bHPRgkF3JUikC4ENAHEeMrd41Zxv3hVZjC9KtT8OvPVGJ-hQMRKRrZuJAEcl7B338qju59zJMjw2DELjzEvxwYv7hH5Ynpc1ODQ0aT4U4OFEeco8ohsN5PjL1iC2dNtk2BAokeMCg2ZXKqpc8FXKmhX94kIxQ"
            }
        """

        MatrixJson.decodeFromString<PostPusher.Body>(json)
    }

    @Test
    fun testGetPushRulesResponse() {
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
                                    "is": "2",
                                    "kind": "room_member_count"
                                },
                                {
                                    "key": "type",
                                    "kind": "event_match",
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
        """

        MatrixJson.decodeFromString<GetPushRules.Response>(json)
    }

    @Test
    fun testGetPushRuleResponse() {
        // language=json
        val json = """ 
            {
                "actions": [
                    "dont_notify"
                ],
                "default": false,
                "enabled": true,
                "pattern": "cake*lie",
                "rule_id": "nocake"
            }
        """

        MatrixJson.decodeFromString<PushRule>(json)
    }

    @Test
    fun testSetPushRuleBody() {
        // language=json
        val json = """ 
            {
                "actions": [
                    "notify"
                ],
                "pattern": "cake*lie"
            }
        """

        MatrixJson.decodeFromString<SetPushRule.Body>(json)
    }

    @Test
    fun testGetPushRuleActionsResponse() {
        // language=json
        val json = """ 
            {
                "actions": [
                    "notify"
                ]
            }
        """

        MatrixJson.decodeFromString<GetPushRuleActions.Response>(json)
    }

    @Test
    fun testSetPushRuleActionsBody() {
        // language=json
        val json = """ 
            {
                "actions": [
                    "notify"
                ]
            }
        """

        MatrixJson.decodeFromString<SetPushRuleActions.Body>(json)
    }

    @Test
    fun testIsPushRuleEnabledResponse() {
        // language=json
        val json = """ 
            {
                "enabled": true
            }
        """

        MatrixJson.decodeFromString<IsPushRuleEnabled.Response>(json)
    }

    @Test
    fun testSetPushRuleEnabledBody() {
        // language=json
        val json = """ 
            {
                "enabled": true
            }
        """

        MatrixJson.decodeFromString<SetPushRuleEnabled.Body>(json)
    }

    @Test
    fun testRegisterResponse() {
        // language=json
        val json = """ 
            {
                "access_token": "abc123",
                "device_id": "GHTYAJCE",
                "user_id": "@cheeky_monkey:matrix.org"
            }
        """

        MatrixJson.decodeFromString<Register.Response>(json)
    }

    @Test
    fun testCheckUsernameAvailabilityResponse() {
        // language=json
        val json = """ 
            {
                "available": true
            }
        """

        MatrixJson.decodeFromString<CheckUsernameAvailability.Response>(json)
    }

    @Test
    fun testGetLocalAliasesResponse() {
        // language=json
        val json = """ 
            {
                "aliases": [
                    "#somewhere:example.com",
                    "#another:example.com",
                    "#hat_trick:example.com"
                ]
            }
        """

        MatrixJson.decodeFromString<GetLocalAliases.Response>(json)
    }

    @Test
    fun testBanBody() {
        // language=json
        val json = """ 
            {
                "reason": "Telling unfunny jokes",
                "user_id": "@cheeky_monkey:matrix.org"
            }
        """

        MatrixJson.decodeFromString<Ban.Body>(json)
    }

    @Test
    fun testGetEventContextResponse() {
        // language=json
        val json = """ 
            {
                "end": "t29-57_2_0_2",
                "event": {
                    "content": {
                        "body": "filename.jpg",
                        "info": {
                            "h": 398,
                            "mimetype": "image/jpeg",
                            "size": 31037,
                            "w": 394
                        },
                        "msgtype": "m.image",
                        "url": "mxc://example.org/JWEIFJgwEIhweiWJE"
                    },
                    "event_id": "${'$'}f3h4d129462ha:example.com",
                    "origin_server_ts": 1432735824653,
                    "room_id": "!636q39766251:example.com",
                    "sender": "@example:example.org",
                    "type": "m.room.message",
                    "unsigned": {
                        "age": 1234
                    }
                },
                "events_after": [
                    {
                        "content": {
                            "body": "This is an example text message",
                            "format": "org.matrix.custom.html",
                            "formatted_body": "<b>This is an example text message</b>",
                            "msgtype": "m.text"
                        },
                        "event_id": "${'$'}143273582443PhrSn:example.org",
                        "origin_server_ts": 1432735824653,
                        "room_id": "!636q39766251:example.com",
                        "sender": "@example:example.org",
                        "type": "m.room.message",
                        "unsigned": {
                            "age": 1234
                        }
                    }
                ],
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
                        "event_id": "${'$'}143273582443PhrSn:example.org",
                        "origin_server_ts": 1432735824653,
                        "room_id": "!636q39766251:example.com",
                        "sender": "@example:example.org",
                        "type": "m.room.message",
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
                            "m.federate": true,
                            "predecessor": {
                                "event_id": "${'$'}something:example.org",
                                "room_id": "!oldroom:example.org"
                            },
                            "room_version": "1"
                        },
                        "event_id": "${'$'}143273582443PhrSn:example.org",
                        "origin_server_ts": 1432735824653,
                        "room_id": "!636q39766251:example.com",
                        "sender": "@example:example.org",
                        "state_key": "",
                        "type": "m.room.create",
                        "unsigned": {
                            "age": 1234
                        }
                    },
                    {
                        "content": {
                            "avatar_url": "mxc://example.org/SEsfnsuifSDFSSEF",
                            "displayname": "Alice Margatroid",
                            "membership": "join"
                        },
                        "event_id": "${'$'}143273582443PhrSn:example.org",
                        "origin_server_ts": 1432735824653,
                        "room_id": "!636q39766251:example.com",
                        "sender": "@example:example.org",
                        "state_key": "@alice:example.org",
                        "type": "m.room.member",
                        "unsigned": {
                            "age": 1234
                        }
                    }
                ]
            }
        """

        MatrixJson.decodeFromString<GetEventContext.Response>(json)
    }

    @Test
    fun testGetOneRoomEventResponse() {
        // language=json
        val json = """ 
            {
                "content": {
                    "body": "This is an example text message",
                    "format": "org.matrix.custom.html",
                    "formatted_body": "<b>This is an example text message</b>",
                    "msgtype": "m.text"
                },
                "event_id": "${'$'}143273582443PhrSn:example.org",
                "origin_server_ts": 1432735824653,
                "room_id": "!636q39766251:matrix.org",
                "sender": "@example:example.org",
                "type": "m.room.message",
                "unsigned": {
                    "age": 1234
                }
            }
        """

        MatrixJson.decodeFromString<MatrixEvent>(json)
    }

    @Test
    fun testRoomInitialSyncResponse() {
        // language=json
        val json = """ 
            {
                "account_data": [
                    {
                        "content": {
                            "tags": {
                                "work": {
                                    "order": "1"
                                }
                            }
                        },
                        "type": "m.tag"
                    }
                ],
                "membership": "join",
                "messages": {
                    "chunk": [
                        {
                            "content": {
                                "body": "This is an example text message",
                                "format": "org.matrix.custom.html",
                                "formatted_body": "<b>This is an example text message</b>",
                                "msgtype": "m.text"
                            },
                            "event_id": "${'$'}143273582443PhrSn:example.org",
                            "origin_server_ts": 1432735824653,
                            "room_id": "!636q39766251:example.com",
                            "sender": "@example:example.org",
                            "type": "m.room.message",
                            "unsigned": {
                                "age": 1234
                            }
                        },
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
                            "event_id": "${'$'}143273582443PhrSn:example.org",
                            "origin_server_ts": 1432735824653,
                            "room_id": "!636q39766251:example.com",
                            "sender": "@example:example.org",
                            "type": "m.room.message",
                            "unsigned": {
                                "age": 1234
                            }
                        }
                    ],
                    "end": "s3456_9_0",
                    "start": "t44-3453_9_0"
                },
                "room_id": "!636q39766251:example.com",
                "state": [
                    {
                        "content": {
                            "join_rule": "public"
                        },
                        "event_id": "${'$'}143273582443PhrSn:example.org",
                        "origin_server_ts": 1432735824653,
                        "room_id": "!636q39766251:example.com",
                        "sender": "@example:example.org",
                        "state_key": "",
                        "type": "m.room.join_rules",
                        "unsigned": {
                            "age": 1234
                        }
                    },
                    {
                        "content": {
                            "avatar_url": "mxc://example.org/SEsfnsuifSDFSSEF",
                            "displayname": "Alice Margatroid",
                            "membership": "join"
                        },
                        "event_id": "${'$'}143273582443PhrSn:example.org",
                        "origin_server_ts": 1432735824653,
                        "room_id": "!636q39766251:example.com",
                        "sender": "@example:example.org",
                        "state_key": "@alice:example.org",
                        "type": "m.room.member",
                        "unsigned": {
                            "age": 1234
                        }
                    },
                    {
                        "content": {
                            "creator": "@example:example.org",
                            "m.federate": true,
                            "predecessor": {
                                "event_id": "${'$'}something:example.org",
                                "room_id": "!oldroom:example.org"
                            },
                            "room_version": "1"
                        },
                        "event_id": "${'$'}143273582443PhrSn:example.org",
                        "origin_server_ts": 1432735824653,
                        "room_id": "!636q39766251:example.com",
                        "sender": "@example:example.org",
                        "state_key": "",
                        "type": "m.room.create",
                        "unsigned": {
                            "age": 1234
                        }
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
                            "notifications": {
                                "room": 20
                            },
                            "redact": 50,
                            "state_default": 50,
                            "users": {
                                "@example:localhost": 100
                            },
                            "users_default": 0
                        },
                        "event_id": "${'$'}143273582443PhrSn:example.org",
                        "origin_server_ts": 1432735824653,
                        "room_id": "!636q39766251:example.com",
                        "sender": "@example:example.org",
                        "state_key": "",
                        "type": "m.room.power_levels",
                        "unsigned": {
                            "age": 1234
                        }
                    }
                ],
                "visibility": "private"
            }
        """

        MatrixJson.decodeFromString<RoomInitialSync.RoomInfo>(json)
    }

    @Test
    fun testInviteBy3PIDBody() {
        // language=json
        val json = """ 
            {
                "address": "cheeky@monkey.com",
                "id_access_token": "abc123_OpaqueString",
                "id_server": "matrix.org",
                "medium": "email"
            }
        """

        MatrixJson.decodeFromString<Invite3pid>(json)
    }

    @Test
    fun testInviteUserBody() {
        // language=json
        val json = """ 
            {
                "user_id": "@cheeky_monkey:matrix.org"
            }
        """

        MatrixJson.decodeFromString<InviteUser.Body>(json)
    }

    @Test
    fun testJoinRoomByIdResponse() {
        // language=json
        val json = """ 
            {
                "room_id": "!d41d8cd:matrix.org"
            }
        """

        MatrixJson.decodeFromString<JoinRoomById.Response>(json)
    }

    @Test
    fun testGetJoinedMembersByRoomResponse() {
        // language=json
        val json = """ 
            {
                "joined": {
                    "@bar:example.com": {
                        "avatar_url": "mxc://riot.ovh/printErCATzZijQsSDWorRaK",
                        "display_name": "Bar"
                    }
                }
            }
        """

        MatrixJson.decodeFromString<GetJoinedMembersByRoom.Response>(json)
    }

    @Test
    fun testKickBody() {
        // language=json
        val json = """ 
            {
                "reason": "Telling unfunny jokes",
                "user_id": "@cheeky_monkey:matrix.org"
            }
        """

        MatrixJson.decodeFromString<Kick.Body>(json)
    }

    @Test
    fun testGetMembersByRoomResponse() {
        // language=json
        val json = """ 
            {
                "chunk": [
                    {
                        "content": {
                            "avatar_url": "mxc://example.org/SEsfnsuifSDFSSEF",
                            "displayname": "Alice Margatroid",
                            "membership": "join"
                        },
                        "event_id": "${'$'}143273582443PhrSn:example.org",
                        "origin_server_ts": 1432735824653,
                        "room_id": "!636q39766251:example.com",
                        "sender": "@example:example.org",
                        "state_key": "@alice:example.org",
                        "type": "m.room.member",
                        "unsigned": {
                            "age": 1234
                        }
                    }
                ]
            }
        """

        MatrixJson.decodeFromString<GetMembersByRoom.Response>(json)
    }

    @Test
    fun testGetRoomEventsResponse() {
        // language=json
        val json = """ 
            {
                "chunk": [
                    {
                        "content": {
                            "body": "This is an example text message",
                            "format": "org.matrix.custom.html",
                            "formatted_body": "<b>This is an example text message</b>",
                            "msgtype": "m.text"
                        },
                        "event_id": "${'$'}143273582443PhrSn:example.org",
                        "origin_server_ts": 1432735824653,
                        "room_id": "!636q39766251:example.com",
                        "sender": "@example:example.org",
                        "type": "m.room.message",
                        "unsigned": {
                            "age": 1234
                        }
                    },
                    {
                        "content": {
                            "name": "The room name"
                        },
                        "event_id": "${'$'}143273582443PhrSn:example.org",
                        "origin_server_ts": 1432735824653,
                        "room_id": "!636q39766251:example.com",
                        "sender": "@example:example.org",
                        "state_key": "",
                        "type": "m.room.name",
                        "unsigned": {
                            "age": 1234
                        }
                    },
                    {
                        "content": {
                            "body": "Gangnam Style",
                            "info": {
                                "duration": 2140786,
                                "h": 320,
                                "mimetype": "video/mp4",
                                "size": 1563685,
                                "thumbnail_info": {
                                    "h": 300,
                                    "mimetype": "image/jpeg",
                                    "size": 46144,
                                    "w": 300
                                },
                                "thumbnail_url": "mxc://example.org/FHyPlCeYUSFFxlgbQYZmoEoe",
                                "w": 480
                            },
                            "msgtype": "m.video",
                            "url": "mxc://example.org/a526eYUSFFxlgbQYZmo442"
                        },
                        "event_id": "${'$'}143273582443PhrSn:example.org",
                        "origin_server_ts": 1432735824653,
                        "room_id": "!636q39766251:example.com",
                        "sender": "@example:example.org",
                        "type": "m.room.message",
                        "unsigned": {
                            "age": 1234
                        }
                    }
                ],
                "end": "t47409-4357353_219380_26003_2265",
                "start": "t47429-4392820_219380_26003_2265"
            }
        """

        MatrixJson.decodeFromString<GetRoomEvents.Response>(json)
    }

    @Test
    fun testRedactEventBody() {
        // language=json
        val json = """ 
            {
                "reason": "Indecent material"
            }
        """

        MatrixJson.decodeFromString<RedactEvent.Body>(json)
    }

    @Test
    fun testRedactEventResponse() {
        // language=json
        val json = """ 
            {
                "event_id": "${'$'}YUwQidLecu:example.com"
            }
        """

        MatrixJson.decodeFromString<RedactEvent.Response>(json)
    }

    @Test
    fun testReportContentBody() {
        // language=json
        val json = """ 
            {
                "reason": "this makes me sad",
                "score": -100
            }
        """

        MatrixJson.decodeFromString<ReportContent.Body>(json)
    }

    @Test
    fun testSendMessageBody() {
        // language=json
        val json = """ 
            {
                "body": "hello",
                "msgtype": "m.text"
            }
        """

        MatrixJson.decodeFromString<JsonObject>(json)
    }

    @Test
    fun testSendMessageResponse() {
        // language=json
        val json = """ 
            {
                "event_id": "${'$'}YUwRidLecu:example.com"
            }
        """

        MatrixJson.decodeFromString<SendMessage.Response>(json)
    }

    @Test
    fun testGetRoomStateResponse() {
        // language=json
        val json = """ 
            [
                {
                    "content": {
                        "join_rule": "public"
                    },
                    "event_id": "${'$'}143273582443PhrSn:example.org",
                    "origin_server_ts": 1432735824653,
                    "room_id": "!636q39766251:example.com",
                    "sender": "@example:example.org",
                    "state_key": "",
                    "type": "m.room.join_rules",
                    "unsigned": {
                        "age": 1234
                    }
                },
                {
                    "content": {
                        "avatar_url": "mxc://example.org/SEsfnsuifSDFSSEF",
                        "displayname": "Alice Margatroid",
                        "membership": "join"
                    },
                    "event_id": "${'$'}143273582443PhrSn:example.org",
                    "origin_server_ts": 1432735824653,
                    "room_id": "!636q39766251:example.com",
                    "sender": "@example:example.org",
                    "state_key": "@alice:example.org",
                    "type": "m.room.member",
                    "unsigned": {
                        "age": 1234
                    }
                },
                {
                    "content": {
                        "creator": "@example:example.org",
                        "m.federate": true,
                        "predecessor": {
                            "event_id": "${'$'}something:example.org",
                            "room_id": "!oldroom:example.org"
                        },
                        "room_version": "1"
                    },
                    "event_id": "${'$'}143273582443PhrSn:example.org",
                    "origin_server_ts": 1432735824653,
                    "room_id": "!636q39766251:example.com",
                    "sender": "@example:example.org",
                    "state_key": "",
                    "type": "m.room.create",
                    "unsigned": {
                        "age": 1234
                    }
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
                        "notifications": {
                            "room": 20
                        },
                        "redact": 50,
                        "state_default": 50,
                        "users": {
                            "@example:localhost": 100
                        },
                        "users_default": 0
                    },
                    "event_id": "${'$'}143273582443PhrSn:example.org",
                    "origin_server_ts": 1432735824653,
                    "room_id": "!636q39766251:example.com",
                    "sender": "@example:example.org",
                    "state_key": "",
                    "type": "m.room.power_levels",
                    "unsigned": {
                        "age": 1234
                    }
                }
            ]
        """

        MatrixJson.decodeFromString<List<MatrixEvent>>(json)
    }

    @Test
    fun testSetRoomStateWithKeyResponse() {
        // language=json
        val json = """ 
            {
                "event_id": "${'$'}YUwRidLecu:example.com"
            }
        """

        MatrixJson.decodeFromString<SetRoomStateWithKey.Response>(json)
    }

    @Test
    fun testSetTypingBody() {
        // language=json
        val json = """ 
            {
                "timeout": 30000,
                "typing": true
            }
        """

        MatrixJson.decodeFromString<SetTyping.Body>(json)
    }

    @Test
    fun testUnbanBody() {
        // language=json
        val json = """ 
            {
                "user_id": "@cheeky_monkey:matrix.org"
            }
        """

        MatrixJson.decodeFromString<Unban.Body>(json)
    }

    @Test
    fun testUpgradeRoomBody() {
        // language=json
        val json = """ 
            {
                "new_version": "2"
            }
        """

        MatrixJson.decodeFromString<UpgradeRoom.Body>(json)
    }

    @Test
    fun testUpgradeRoomResponse() {
        // language=json
        val json = """ 
            {
                "replacement_room": "!newroom:example.org"
            }
        """

        MatrixJson.decodeFromString<UpgradeRoom.Response>(json)
    }

    @Test
    fun testSearchBody() {
        // language=json
        val json = """ 
            {
                "search_categories": {
                    "room_events": {
                        "groupings": {
                            "group_by": [
                                {
                                    "key": "room_id"
                                }
                            ]
                        },
                        "keys": [
                            "content.body"
                        ],
                        "order_by": "recent",
                        "search_term": "martians and men"
                    }
                }
            }
        """

        MatrixJson.decodeFromString<Search.Body>(json)
    }

    @Test
    fun testSearchResponse() {
        // language=json
        val json = """ 
            {
                "search_categories": {
                    "room_events": {
                        "count": 1224,
                        "groups": {
                            "room_id": {
                                "!qPewotXpIctQySfjSy:localhost": {
                                    "next_batch": "BdgFsdfHSf-dsFD",
                                    "order": 1,
                                    "results": [
                                        "${'$'}144429830826TWwbB:localhost"
                                    ]
                                }
                            }
                        },
                        "highlights": [
                            "martians",
                            "men"
                        ],
                        "next_batch": "5FdgFsd234dfgsdfFD",
                        "results": [
                            {
                                "rank": 0.00424866,
                                "result": {
                                    "content": {
                                        "body": "This is an example text message",
                                        "format": "org.matrix.custom.html",
                                        "formatted_body": "<b>This is an example text message</b>",
                                        "msgtype": "m.text"
                                    },
                                    "event_id": "${'$'}144429830826TWwbB:localhost",
                                    "origin_server_ts": 1432735824653,
                                    "room_id": "!qPewotXpIctQySfjSy:localhost",
                                    "sender": "@example:example.org",
                                    "type": "m.room.message",
                                    "unsigned": {
                                        "age": 1234
                                    }
                                }
                            }
                        ]
                    }
                }
            }
        """

        MatrixJson.decodeFromString<Results>(json)
    }

    @Test
    fun testSendToDeviceResponse() {
        // language=json
        val json = """ 
            {
            }
        """

        MatrixJson.decodeFromString<Unit>(json)
    }

    @Test
    fun testSyncResponse() {
        // language=json
        val json = """ 
            {
                "account_data": {
                    "events": [
                        {
                            "content": {
                                "custom_config_key": "custom_config_value"
                            },
                            "type": "org.example.custom.config"
                        }
                    ]
                },
                "next_batch": "s72595_4483_1934",
                "presence": {
                    "events": [
                        {
                            "content": {
                                "avatar_url": "mxc://localhost:wefuiwegh8742w",
                                "currently_active": false,
                                "last_active_ago": 2478593,
                                "presence": "online",
                                "status_msg": "Making cupcakes"
                            },
                            "sender": "@example:localhost",
                            "type": "m.presence"
                        }
                    ]
                },
                "rooms": {
                    "invite": {
                        "!696r7674:example.com": {
                            "invite_state": {
                                "events": [
                                    {
                                        "content": {
                                            "name": "My Room Name"
                                        },
                                        "sender": "@alice:example.com",
                                        "state_key": "",
                                        "type": "m.room.name"
                                    },
                                    {
                                        "content": {
                                            "membership": "invite"
                                        },
                                        "sender": "@alice:example.com",
                                        "state_key": "@bob:example.com",
                                        "type": "m.room.member"
                                    }
                                ]
                            }
                        }
                    },
                    "join": {
                        "!726s6s6q:example.com": {
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
                                    },
                                    {
                                        "content": {
                                            "custom_config_key": "custom_config_value"
                                        },
                                        "type": "org.example.custom.room.config"
                                    }
                                ]
                            },
                            "ephemeral": {
                                "events": [
                                    {
                                        "content": {
                                            "user_ids": [
                                                "@alice:matrix.org",
                                                "@bob:example.com"
                                            ]
                                        },
                                        "room_id": "!jEsUZKDJdhlrceRyVU:example.org",
                                        "type": "m.typing"
                                    }
                                ]
                            },
                            "state": {
                                "events": [
                                    {
                                        "content": {
                                            "avatar_url": "mxc://example.org/SEsfnsuifSDFSSEF",
                                            "displayname": "Alice Margatroid",
                                            "membership": "join"
                                        },
                                        "event_id": "${'$'}143273582443PhrSn:example.org",
                                        "origin_server_ts": 1432735824653,
                                        "room_id": "!726s6s6q:example.com",
                                        "sender": "@example:example.org",
                                        "state_key": "@alice:example.org",
                                        "type": "m.room.member",
                                        "unsigned": {
                                            "age": 1234
                                        }
                                    }
                                ]
                            },
                            "summary": {
                                "m.heroes": [
                                    "@alice:example.com",
                                    "@bob:example.com"
                                ],
                                "m.invited_member_count": 0,
                                "m.joined_member_count": 2
                            },
                            "timeline": {
                                "events": [
                                    {
                                        "content": {
                                            "avatar_url": "mxc://example.org/SEsfnsuifSDFSSEF",
                                            "displayname": "Alice Margatroid",
                                            "membership": "join"
                                        },
                                        "event_id": "${'$'}143273582443PhrSn:example.org",
                                        "origin_server_ts": 1432735824653,
                                        "room_id": "!726s6s6q:example.com",
                                        "sender": "@example:example.org",
                                        "state_key": "@alice:example.org",
                                        "type": "m.room.member",
                                        "unsigned": {
                                            "age": 1234
                                        }
                                    },
                                    {
                                        "content": {
                                            "body": "This is an example text message",
                                            "format": "org.matrix.custom.html",
                                            "formatted_body": "<b>This is an example text message</b>",
                                            "msgtype": "m.text"
                                        },
                                        "event_id": "${'$'}143273582443PhrSn:example.org",
                                        "origin_server_ts": 1432735824653,
                                        "room_id": "!726s6s6q:example.com",
                                        "sender": "@example:example.org",
                                        "type": "m.room.message",
                                        "unsigned": {
                                            "age": 1234
                                        }
                                    }
                                ],
                                "limited": true,
                                "prev_batch": "t34-23535_0_0"
                            }
                        }
                    },
                    "leave": {
                    }
                }
            }
        """

        MatrixJson.decodeFromString<SyncResponse>(json)
    }

    @Test
    fun testSetAccountDataBody() {
        // language=json
        val json = """ 
            {
                "custom_account_data_key": "custom_config_value"
            }
        """

        MatrixJson.decodeFromString<JsonObject>(json)
    }

    @Test
    fun testGetFilterResponse() {
        // language=json
        val json = """ 
            {
                "event_fields": [
                    "type",
                    "content",
                    "sender"
                ],
                "event_format": "client",
                "presence": {
                    "not_senders": [
                        "@alice:example.com"
                    ],
                    "types": [
                        "m.presence"
                    ]
                },
                "room": {
                    "ephemeral": {
                        "not_rooms": [
                            "!726s6s6q:example.com"
                        ],
                        "not_senders": [
                            "@spam:example.com"
                        ],
                        "types": [
                            "m.receipt",
                            "m.typing"
                        ]
                    },
                    "state": {
                        "not_rooms": [
                            "!726s6s6q:example.com"
                        ],
                        "types": [
                            "m.room.*"
                        ]
                    },
                    "timeline": {
                        "limit": 10,
                        "not_rooms": [
                            "!726s6s6q:example.com"
                        ],
                        "not_senders": [
                            "@spam:example.com"
                        ],
                        "types": [
                            "m.room.message"
                        ]
                    }
                }
            }
        """

        MatrixJson.decodeFromString<Filter>(json)
    }

    @Test
    fun testRequestOpenIdTokenBody() {
        // language=json
        val json = """ 
            {
            }
        """

        MatrixJson.decodeFromString<RequestOpenIdToken.Body>(json)
    }

    @Test
    fun testRequestOpenIdTokenResponse() {
        // language=json
        val json = """ 
            {
                "access_token": "SomeT0kenHere",
                "expires_in": 3600,
                "matrix_server_name": "example.com",
                "token_type": "Bearer"
            }
        """

        MatrixJson.decodeFromString<RequestOpenIdToken.Response>(json)
    }

    @Test
    fun testSetAccountDataPerRoomBody() {
        // language=json
        val json = """ 
            {
                "custom_account_data_key": "custom_account_data_value"
            }
        """

        MatrixJson.decodeFromString<JsonObject>(json)
    }

    @Test
    fun testGetRoomTagsResponse() {
        // language=json
        val json = """ 
            {
                "tags": {
                    "m.favourite": {
                        "order": 0.1
                    },
                    "u.Customers": {
                    },
                    "u.Work": {
                        "order": 0.7
                    }
                }
            }
        """

        MatrixJson.decodeFromString<TagContent>(json)
    }

    @Test
    fun testSetRoomTagBody() {
        // language=json
        val json = """ 
            {
                "order": 0.25
            }
        """

        MatrixJson.decodeFromString<TagContent.Tag>(json)
    }

    @Test
    fun testSearchUserDirectoryResponse() {
        // language=json
        val json = """ 
            {
                "limited": false,
                "results": [
                    {
                        "avatar_url": "mxc://bar.com/foo",
                        "display_name": "Foo",
                        "user_id": "@foo:bar.com"
                    }
                ]
            }
        """

        MatrixJson.decodeFromString<SearchUserDirectory.Response>(json)
    }

    @Test
    fun testGetTurnServerResponse() {
        // language=json
        val json = """ 
            {
                "password": "JlKfBy1QwLrO20385QyAtEyIv0=",
                "ttl": 86400,
                "uris": [
                    "turn:turn.example.com:3478?transport=udp",
                    "turn:10.20.30.40:3478?transport=tcp",
                    "turns:10.20.30.40:443?transport=tcp"
                ],
                "username": "1443779631:@user:example.com"
            }
        """

        MatrixJson.decodeFromString<GetTurnServer.Response>(json)
    }

    @Test
    fun testGetVersionsResponse() {
        // language=json
        val json = """ 
            {
                "unstable_features": {
                    "org.example.my_feature": true
                },
                "versions": [
                    "r0.0.1"
                ]
            }
        """

        MatrixJson.decodeFromString<GetVersions.Response>(json)
    }

    @Test
    fun testGetConfigResponse() {
        // language=json
        val json = """ 
            {
                "m.upload.size": 50000000
            }
        """

        MatrixJson.decodeFromString<GetConfig.Response>(json)
    }

    @Test
    fun testGetUrlPreviewResponse() {
        // language=json
        val json = """ 
            {
                "matrix:image:size": 102400,
                "og:description": "This is a really cool blog post from matrix.org",
                "og:image": "mxc://example.com/ascERGshawAWawugaAcauga",
                "og:image:height": 48,
                "og:image:type": "image/png",
                "og:image:width": 48,
                "og:title": "Matrix Blog Post"
            }
        """

        MatrixJson.decodeFromString<GetUrlPreview.Response>(json)
    }

    @Test
    fun testUploadContentResponse() {
        // language=json
        val json = """ 
            {
                "content_uri": "mxc://example.com/AQwafuaFswefuhsfAFAgsw"
            }
        """

        MatrixJson.decodeFromString<UploadContent.Response>(json)
    }

    @Test
    fun testAdd3PIDBody() {
        // language=json
        val json = """ 
            {
                "client_secret": "d0n'tT3ll",
                "sid": "abc123987"
            }
        """

        MatrixJson.decodeFromString<Add3PID.Body>(json)
    }

    @Test
    fun testRequestTokenToResetPasswordMSISDNBody() {
        // language=json
        val json = """ 
            {
                "client_secret": "monkeys_are_GREAT",
                "country": "GB",
                "phone_number": "07700900001",
                "send_attempt": 1
            }
        """

        MatrixJson.decodeFromString<MSISDNValidationRequest>(json)
    }

    @Test
    fun testGetRoomVisibilityOnDirectoryResponse() {
        // language=json
        val json = """ 
            {
                "visibility": "public"
            }
        """

        MatrixJson.decodeFromString<GetRoomVisibilityOnDirectory.Response>(json)
    }

    @Test
    fun testSetRoomVisibilityOnDirectoryBody() {
        // language=json
        val json = """ 
            {
                "visibility": "public"
            }
        """

        MatrixJson.decodeFromString<SetRoomVisibilityOnDirectory.Body>(json)
    }
}
