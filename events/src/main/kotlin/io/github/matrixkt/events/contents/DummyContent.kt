package io.github.matrixkt.events.contents

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This event type is used to indicate new Olm sessions for end-to-end encryption.
 * Typically it is encrypted as an m.room.encrypted event, then sent as a [to-device](https://matrix.org/docs/spec/client_server/r0.6.0#to-device) event.
 * The event does not have any content associated with it.
 * The sending client is expected to send a key share request shortly after this message,
 * causing the receiving client to process this `m.dummy` event as the most recent event and using the keyshare request to set up the session.
 * The keyshare request and `m.dummy` combination should result in the original sending client receiving keys over the newly established session.
 */
@SerialName("m.dummy")
@Serializable
public class DummyContent
