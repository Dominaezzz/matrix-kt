package io.github.matrixkt.olm

import org.khronos.webgl.Uint8Array
import org.khronos.webgl.get
import kotlin.js.Promise

public external interface Options {
    @JsName("locateFile")
    public var locateFile: () -> String;
}

public external interface DecryptedMessage {
    public var plaintext: String
    public var message_index: Int
}

public external interface EncryptedMessage {
    public var body: String
    public var type: Int
}

public external interface OlmPkMessage {
    public var ephemeral: String
    public var mac: String
    public var ciphertext: String
}

@JsModule("@matrix-org/olm")
@JsNonModule
public external class JsOlm {

    public class Account {
        public constructor();
        public fun create();
        public fun identity_keys(): String;
        public fun mark_keys_as_published();
        public fun max_number_of_one_time_keys(): Int;
        public fun generate_one_time_keys(number_of_keys: Long);
        public fun one_time_keys(): String;
        public fun remove_one_time_keys(session: Session);
        public fun generate_fallback_key();
        public fun fallback_key(): String;
        public fun pickle(key: Uint8Array): String;
        public fun pickle(key: String): String;
        public fun free();
        public fun sign(message: String): String;
        public fun sign(message: Uint8Array): String;
        public fun unpickle(key: Uint8Array, pickle: String);
        public fun unpickle(key: String, pickle: String);
    }

    public class Session {
        public constructor();
        public fun free();
        public fun pickle(key: ByteArray): String;
        public fun pickle(key: String): String;
        public fun unpickle(key: ByteArray, pickle: String);
        public fun unpickle(key: String, pickle: String);
        public fun create_outbound(account: Account, their_identity_key: String, their_one_time_key: String);
        public fun create_inbound(account: Account, one_time_key_message: String);
        public fun create_inbound_from(account: Account, identity_key: String, one_time_key_message: String);
        public fun session_id(): String;
        public fun has_received_message(): Boolean;
        public fun matches_inbound(one_time_key_message: String): Boolean;
        public fun matches_inbound_from(identity_key: String, one_time_key_message: String): Boolean;
        public fun encrypt(plaintext: String): EncryptedMessage;
        public fun decrypt(message_type: Int, message: String): String;
        public fun describe(): String;
    }

    public class PkSigning {
        public constructor();
        public fun free();
        public fun init_with_seed(seed: Uint8Array): String;
        public fun generate_seed(): Uint8Array;
        public fun sign(message: String): String;
    }

    public companion object {
        public fun init(options: Options = definedExternally): Promise<Unit>;
        public val PRIVATE_KEY_LENGTH: Int;
    }

    public class InboundGroupSession {
        public constructor();
        public fun pickle(key: String): String;
        public fun pickle(key: Uint8Array): String;
        public fun create(session_key: String): String;
        public fun session_id(): String;
        public fun first_known_index(): Long;
        public fun free();
        public fun import_session(session_key: String): String;

        public fun export_session(message_index: Long): String;
        public fun decrypt(message: String): DecryptedMessage;
        public fun unpickle(key: Uint8Array, pickle: String);
        public fun unpickle(key: String, pickle: String);
    }

    public class OutboundGroupSession {
        public constructor();
        public fun free();
        public fun pickle(key: Uint8Array): String;
        public fun pickle(key: String): String;
        public fun unpickle(key: Uint8Array, pickle: String);
        public fun unpickle(key: String, pickle: String);
        public fun create();
        public fun encrypt(plaintext: String): String;
        public fun session_id(): String;
        public fun session_key(): String;
        public fun message_index(): Int;
    }

    public class PkDecryption {
        public fun constructor();
        public fun free();
        public fun init_with_private_key(key: Uint8Array): String;
        public fun generate_key(): String;
        public fun get_private_key(): Uint8Array;
        public fun pickle(key: Uint8Array): String;
        public fun pickle(key: String): String;
        public fun unpickle(key: Uint8Array, pickle: String): String;
        public fun unpickle(key: String, pickle: String): String;
        public fun decrypt(ephemeral_key: String, mac: String, ciphertext: String): String;
    }

    public class PkEncryption {
        public fun constructor();
        public fun free();
        public fun set_recipient_key(key: String);
        public fun encrypt(plaintext: String): OlmPkMessage;
    }

    public class SAS {
        public fun free();
        public fun get_pubkey(): String;
        public fun set_their_key(their_key: String);
        public fun generate_bytes(info: String, length: Int): Uint8Array;
        public fun calculate_mac(input: String, info: String): String;
        public fun calculate_mac_long_kdf(input: String, info: String): String;
        public fun is_their_key_set(): Boolean;
    }

    public class Utility {
        public constructor();
        public fun free();
        public fun sha256(input: String): String;
        public fun sha256(input: Uint8Array): String;
        public fun ed25519_verify(key: String, message: String, signature: String);
        public fun ed25519_verify(key: String, message: Uint8Array, signature: String);
    }

}

public fun Uint8Array.toByteArray() : ByteArray {
    return ByteArray(this.byteLength) { this[it] }
}