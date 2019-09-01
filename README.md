[![GitHub license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](https://www.apache.org/licenses/LICENSE-2.0)

# Matrix in Kotlin

Kotlin multiplatform libraries for [Matrix](https://matrix.org/).
- [ ] Http Client [WIP]
- [ ] SDK
- [ ] olm

# Supported platforms
- JVM
- Android
- JavaScript
- LinuxX64
- MingwX64
- MacosX64
- IosArm32
- IosArm64
- IosX64

## Sample
```kotlin
val client = MatrixClient(Apache.create(), "matrix.org")
client.accessToken = "Super secure Token"

val roomId = "!QtykxKocfZaZOUrTwp:matrix.org"

val content = json {
    "msgtype" to "m.text"
    "body" to "Hello World!"
}
val eventId = client.roomApi.sendMessage(roomId, "m.room.message", "nonce", content)

client.roomApi.redactEvent(roomId, eventId, "nonce2", "Was a bot!")
```
