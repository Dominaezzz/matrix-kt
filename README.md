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


## Usage
```kotlin
repositories {
    maven("https://maven.pkg.github.com/Dominaezzz/matrix-kt") {
        credentials {
            username = System.getenv("GITHUB_USER") // Your GitHub username.
            password = System.getenv("GITHUB_TOKEN") // A GitHub token with `read:packages`.
        }
    }
}

dependencies {
    implementation("io.github.matrixkt:client:$version")
    implementation("io.github.matrixkt:olm:$version")
}
```

## Sample
### Client
```kotlin
val client = HttpClient(Apache) {
    MatrixConfig(baseUrl = Url("matrix.org"))
}
val accessToken = "Super secure Token"

val roomId = "!QtykxKocfZaZOUrTwp:matrix.org"

val response = client.rpc(SendMessage(
    SendMessage.Url(roomId, "m.room.message", "nonce"),
    buildJsonObject {
        put("msgtype", "m.text")
        put("body", "Hello World!")
    }
), accessToken)
val eventId = response.eventId

client.rpc(RedactEvent(
    RedactEvent.Url(roomId, eventId, "nonce2"),
    RedactEvent.Body(reason = "Was a bot!")
), accessToken)
```

### Olm
```kotlin
val account = Account()
upload(account.identityKeys)
val signature = account.sign("""{"key":"super secure key for security things"}""")

val session = Session.createInboundSession(account, "PREKEY message")
val message = session.decrypt(Message.PreKey("oun02024f=ocnaowincd;53tnv024ok/7u"))

session.clear()
account.clear()
```
