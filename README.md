[![GitHub license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](https://www.apache.org/licenses/LICENSE-2.0)
[![](https://github.com/Dominaezzz/matrix-kt/workflows/Build/badge.svg)](https://github.com/Dominaezzz/matrix-kt/actions)

# Matrix in Kotlin

Kotlin multiplatform libraries for [Matrix](https://matrix.org/).
- [x] Http Client
- [x] olm
- [ ] SDK

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
### Client
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

### Olm
```kotlin
val account = Account()
upload(account.identityKeys)
val signature = account.sign("""{"key":"super secure key for security things"}""")

val session = Sessions.createInboundSession(account, "PREKEY message")
val message = session.decrypt(Message("oun02024f=ocnaowincd;53tnv024ok/7u", 0))

session.clear()
account.clear()
```
