@file:Suppress("FunctionName")

package colm.internal

import com.sun.jna.Library
import com.sun.jna.Native
import com.sun.jna.NativeLibrary
import com.sun.jna.Pointer
import com.sun.jna.ptr.IntByReference
import java.nio.ByteBuffer

object OlmLibrary : Library {
    const val JNA_LIBRARY_NAME = "olm"
    val JNA_NATIVE_LIB = NativeLibrary.getInstance(JNA_LIBRARY_NAME)
    const val OLM_MESSAGE_TYPE_PRE_KEY: Long = 0
    const val OLM_MESSAGE_TYPE_MESSAGE: Long = 1

    external fun olm_inbound_group_session_size(): NativeSize

    external fun olm_inbound_group_session(memory: Pointer?): OlmInboundGroupSession?

    external fun olm_inbound_group_session_last_error(session: OlmInboundGroupSession?): String?

    external fun olm_clear_inbound_group_session(session: OlmInboundGroupSession?): NativeSize

    external fun olm_pickle_inbound_group_session_length(session: OlmInboundGroupSession?): NativeSize

    external fun olm_pickle_inbound_group_session(
        session: OlmInboundGroupSession?,
        key: Pointer?,
        key_length: NativeSize,
        pickled: Pointer?,
        pickled_length: NativeSize
    ): NativeSize

    external fun olm_unpickle_inbound_group_session(
        session: OlmInboundGroupSession?,
        key: Pointer?,
        key_length: NativeSize,
        pickled: Pointer?,
        pickled_length: NativeSize
    ): NativeSize

    external fun olm_init_inbound_group_session(
        session: OlmInboundGroupSession?,
        session_key: Pointer?,
        session_key_length: NativeSize
    ): NativeSize

    external fun olm_import_inbound_group_session(
        session: OlmInboundGroupSession?,
        session_key: Pointer?,
        session_key_length: NativeSize
    ): NativeSize

    external fun olm_group_decrypt_max_plaintext_length(
        session: OlmInboundGroupSession?,
        message: Pointer?,
        message_length: NativeSize
    ): NativeSize

    external fun olm_group_decrypt(
        session: OlmInboundGroupSession?,
        message: Pointer?,
        message_length: NativeSize,
        plaintext: Pointer?,
        max_plaintext_length: NativeSize,
        message_index: IntByReference?
    ): NativeSize

    external fun olm_inbound_group_session_id_length(session: OlmInboundGroupSession?): NativeSize

    external fun olm_inbound_group_session_id(
        session: OlmInboundGroupSession?,
        id: Pointer?,
        id_length: NativeSize
    ): NativeSize

    external fun olm_inbound_group_session_first_known_index(session: OlmInboundGroupSession?): Int

    external fun olm_inbound_group_session_is_verified(session: OlmInboundGroupSession?): Int

    external fun olm_export_inbound_group_session_length(session: OlmInboundGroupSession?): NativeSize

    external fun olm_export_inbound_group_session(
        session: OlmInboundGroupSession?,
        key: Pointer?,
        key_length: NativeSize,
        message_index: Int
    ): NativeSize

    external fun olm_outbound_group_session_size(): NativeSize

    external fun olm_outbound_group_session(memory: Pointer?): OlmOutboundGroupSession?

    external fun olm_outbound_group_session_last_error(session: OlmOutboundGroupSession?): String?

    external fun olm_clear_outbound_group_session(session: OlmOutboundGroupSession?): NativeSize

    external fun olm_pickle_outbound_group_session_length(session: OlmOutboundGroupSession?): NativeSize

    external fun olm_pickle_outbound_group_session(
        session: OlmOutboundGroupSession?,
        key: Pointer?,
        key_length: NativeSize,
        pickled: Pointer?,
        pickled_length: NativeSize
    ): NativeSize

    external fun olm_unpickle_outbound_group_session(
        session: OlmOutboundGroupSession?,
        key: Pointer?,
        key_length: NativeSize,
        pickled: Pointer?,
        pickled_length: NativeSize
    ): NativeSize

    external fun olm_init_outbound_group_session_random_length(session: OlmOutboundGroupSession?): NativeSize

    external fun olm_init_outbound_group_session(
        session: OlmOutboundGroupSession?,
        random: Pointer?,
        random_length: NativeSize
    ): NativeSize

    external fun olm_group_encrypt_message_length(
        session: OlmOutboundGroupSession?,
        plaintext_length: NativeSize
    ): NativeSize

    external fun olm_group_encrypt(
        session: OlmOutboundGroupSession?,
        plaintext: Pointer?,
        plaintext_length: NativeSize,
        message: Pointer?,
        message_length: NativeSize
    ): NativeSize

    external fun olm_outbound_group_session_id_length(session: OlmOutboundGroupSession?): NativeSize

    external fun olm_outbound_group_session_id(
        session: OlmOutboundGroupSession?,
        id: Pointer?,
        id_length: NativeSize
    ): NativeSize

    external fun olm_outbound_group_session_message_index(session: OlmOutboundGroupSession?): Int

    external fun olm_outbound_group_session_key_length(session: OlmOutboundGroupSession?): NativeSize

    external fun olm_outbound_group_session_key(
        session: OlmOutboundGroupSession?,
        key: Pointer?,
        key_length: NativeSize
    ): NativeSize

    external fun olm_get_library_version(
        major: ByteBuffer?,
        minor: ByteBuffer?,
        patch: ByteBuffer?
    )

    external fun olm_account_size(): NativeSize

    external fun olm_session_size(): NativeSize

    external fun olm_utility_size(): NativeSize

    external fun olm_account(memory: Pointer?): OlmAccount?

    external fun olm_session(memory: Pointer?): OlmSession?

    external fun olm_utility(memory: Pointer?): OlmUtility?

    external fun olm_error(): NativeSize

    external fun olm_account_last_error(account: OlmAccount?): String?

    external fun olm_session_last_error(session: OlmSession?): String?

    external fun olm_utility_last_error(utility: OlmUtility?): String?

    external fun olm_clear_account(account: OlmAccount?): NativeSize

    external fun olm_clear_session(session: OlmSession?): NativeSize

    external fun olm_clear_utility(utility: OlmUtility?): NativeSize

    external fun olm_pickle_account_length(account: OlmAccount?): NativeSize

    external fun olm_pickle_session_length(session: OlmSession?): NativeSize

    external fun olm_pickle_account(
        account: OlmAccount?,
        key: Pointer?,
        key_length: NativeSize,
        pickled: Pointer?,
        pickled_length: NativeSize
    ): NativeSize

    external fun olm_pickle_session(
        session: OlmSession?,
        key: Pointer?,
        key_length: NativeSize,
        pickled: Pointer?,
        pickled_length: NativeSize
    ): NativeSize

    external fun olm_unpickle_account(
        account: OlmAccount?,
        key: Pointer?,
        key_length: NativeSize,
        pickled: Pointer?,
        pickled_length: NativeSize
    ): NativeSize

    external fun olm_unpickle_session(
        session: OlmSession?,
        key: Pointer?,
        key_length: NativeSize,
        pickled: Pointer?,
        pickled_length: NativeSize
    ): NativeSize

    external fun olm_create_account_random_length(account: OlmAccount?): NativeSize

    external fun olm_create_account(
        account: OlmAccount?,
        random: Pointer?,
        random_length: NativeSize
    ): NativeSize

    external fun olm_account_identity_keys_length(account: OlmAccount?): NativeSize

    external fun olm_account_identity_keys(
        account: OlmAccount?,
        identity_keys: Pointer?,
        identity_key_length: NativeSize
    ): NativeSize

    external fun olm_account_signature_length(account: OlmAccount?): NativeSize

    external fun olm_account_sign(
        account: OlmAccount?,
        message: Pointer?,
        message_length: NativeSize,
        signature: Pointer?,
        signature_length: NativeSize
    ): NativeSize

    external fun olm_account_one_time_keys_length(account: OlmAccount?): NativeSize

    external fun olm_account_one_time_keys(
        account: OlmAccount?,
        one_time_keys: Pointer?,
        one_time_keys_length: NativeSize
    ): NativeSize

    external fun olm_account_mark_keys_as_published(account: OlmAccount?): NativeSize

    external fun olm_account_max_number_of_one_time_keys(account: OlmAccount?): NativeSize

    external fun olm_account_generate_one_time_keys_random_length(
        account: OlmAccount?,
        number_of_keys: NativeSize
    ): NativeSize

    external fun olm_account_generate_one_time_keys(
        account: OlmAccount?,
        number_of_keys: NativeSize,
        random: Pointer?,
        random_length: NativeSize
    ): NativeSize

    external fun olm_create_outbound_session_random_length(session: OlmSession?): NativeSize

    external fun olm_create_outbound_session(
        session: OlmSession?,
        account: OlmAccount?,
        their_identity_key: Pointer?,
        their_identity_key_length: NativeSize,
        their_one_time_key: Pointer?,
        their_one_time_key_length: NativeSize,
        random: Pointer?,
        random_length: NativeSize
    ): NativeSize

    external fun olm_create_inbound_session(
        session: OlmSession?,
        account: OlmAccount?,
        one_time_key_message: Pointer?,
        message_length: NativeSize
    ): NativeSize

    external fun olm_create_inbound_session_from(
        session: OlmSession?,
        account: OlmAccount?,
        their_identity_key: Pointer?,
        their_identity_key_length: NativeSize,
        one_time_key_message: Pointer?,
        message_length: NativeSize
    ): NativeSize

    external fun olm_session_id_length(session: OlmSession?): NativeSize

    external fun olm_session_id(
        session: OlmSession?,
        id: Pointer?,
        id_length: NativeSize
    ): NativeSize

    external fun olm_session_has_received_message(session: OlmSession?): Int

    external fun olm_session_describe(
        session: OlmSession?,
        buf: Pointer?,
        buflen: NativeSize
    )

    external fun olm_matches_inbound_session(
        session: OlmSession?,
        one_time_key_message: Pointer?,
        message_length: NativeSize
    ): NativeSize

    external fun olm_matches_inbound_session_from(
        session: OlmSession?,
        their_identity_key: Pointer?,
        their_identity_key_length: NativeSize,
        one_time_key_message: Pointer?,
        message_length: NativeSize
    ): NativeSize

    external fun olm_remove_one_time_keys(account: OlmAccount?, session: OlmSession?): NativeSize

    external fun olm_encrypt_message_type(session: OlmSession?): NativeSize

    external fun olm_encrypt_random_length(session: OlmSession?): NativeSize

    external fun olm_encrypt_message_length(session: OlmSession?, plaintext_length: NativeSize): NativeSize

    external fun olm_encrypt(
        session: OlmSession?,
        plaintext: Pointer?,
        plaintext_length: NativeSize,
        random: Pointer?,
        random_length: NativeSize,
        message: Pointer?,
        message_length: NativeSize
    ): NativeSize

    external fun olm_decrypt_max_plaintext_length(
        session: OlmSession?,
        message_type: NativeSize,
        message: Pointer?,
        message_length: NativeSize
    ): NativeSize

    external fun olm_decrypt(
        session: OlmSession?,
        message_type: NativeSize,
        message: Pointer?,
        message_length: NativeSize,
        plaintext: Pointer?,
        max_plaintext_length: NativeSize
    ): NativeSize

    external fun olm_sha256_length(utility: OlmUtility?): NativeSize

    external fun olm_sha256(
        utility: OlmUtility?,
        input: Pointer?,
        input_length: NativeSize,
        output: Pointer?,
        output_length: NativeSize
    ): NativeSize

    external fun olm_ed25519_verify(
        utility: OlmUtility?,
        key: Pointer?,
        key_length: NativeSize,
        message: Pointer?,
        message_length: NativeSize,
        signature: Pointer?,
        signature_length: NativeSize
    ): NativeSize

    external fun olm_sas_last_error(sas: OlmSAS?): String?

    external fun olm_sas_size(): NativeSize

    external fun olm_sas(memory: Pointer?): OlmSAS?

    external fun olm_clear_sas(sas: OlmSAS?): NativeSize

    external fun olm_create_sas_random_length(sas: OlmSAS?): NativeSize

    external fun olm_create_sas(
        sas: OlmSAS?,
        random: Pointer?,
        random_length: NativeSize
    ): NativeSize

    external fun olm_sas_pubkey_length(sas: OlmSAS?): NativeSize

    external fun olm_sas_get_pubkey(
        sas: OlmSAS?,
        pubkey: Pointer?,
        pubkey_length: NativeSize
    ): NativeSize

    external fun olm_sas_set_their_key(
        sas: OlmSAS?,
        their_key: Pointer?,
        their_key_length: NativeSize
    ): NativeSize

    external fun olm_sas_generate_bytes(
        sas: OlmSAS?,
        info: Pointer?,
        info_length: NativeSize,
        output: Pointer?,
        output_length: NativeSize
    ): NativeSize

    external fun olm_sas_mac_length(sas: OlmSAS?): NativeSize

    external fun olm_sas_calculate_mac(
        sas: OlmSAS?,
        input: Pointer?,
        input_length: NativeSize,
        info: Pointer?,
        info_length: NativeSize,
        mac: Pointer?,
        mac_length: NativeSize
    ): NativeSize

    external fun olm_sas_calculate_mac_long_kdf(
        sas: OlmSAS?,
        input: Pointer?,
        input_length: NativeSize,
        info: Pointer?,
        info_length: NativeSize,
        mac: Pointer?,
        mac_length: NativeSize
    ): NativeSize

    external fun olm_pk_encryption_size(): NativeSize

    external fun olm_pk_encryption(memory: Pointer?): OlmPkEncryption?

    external fun olm_pk_encryption_last_error(encryption: OlmPkEncryption?): String?

    external fun olm_clear_pk_encryption(encryption: OlmPkEncryption?): NativeSize

    external fun olm_pk_encryption_set_recipient_key(
        encryption: OlmPkEncryption?,
        public_key: Pointer?,
        public_key_length: NativeSize
    ): NativeSize

    external fun olm_pk_ciphertext_length(encryption: OlmPkEncryption?, plaintext_length: NativeSize): NativeSize

    external fun olm_pk_mac_length(encryption: OlmPkEncryption?): NativeSize

    external fun olm_pk_key_length(): NativeSize

    external fun olm_pk_encrypt_random_length(encryption: OlmPkEncryption?): NativeSize

    external fun olm_pk_encrypt(
        encryption: OlmPkEncryption?,
        plaintext: Pointer?,
        plaintext_length: NativeSize,
        ciphertext: Pointer?,
        ciphertext_length: NativeSize,
        mac: Pointer?,
        mac_length: NativeSize,
        ephemeral_key: Pointer?,
        ephemeral_key_size: NativeSize,
        random: Pointer?,
        random_length: NativeSize
    ): NativeSize

    external fun olm_pk_decryption_size(): NativeSize

    external fun olm_pk_decryption(memory: Pointer?): OlmPkDecryption?

    external fun olm_pk_decryption_last_error(decryption: OlmPkDecryption?): String?

    external fun olm_clear_pk_decryption(decryption: OlmPkDecryption?): NativeSize

    external fun olm_pk_private_key_length(): NativeSize

    external fun olm_pk_generate_key_random_length(): NativeSize

    external fun olm_pk_key_from_private(
        decryption: OlmPkDecryption?,
        pubkey: Pointer?,
        pubkey_length: NativeSize,
        privkey: Pointer?,
        privkey_length: NativeSize
    ): NativeSize

    external fun olm_pk_generate_key(
        decryption: OlmPkDecryption?,
        pubkey: Pointer?,
        pubkey_length: NativeSize,
        privkey: Pointer?,
        privkey_length: NativeSize
    ): NativeSize

    external fun olm_pickle_pk_decryption_length(decryption: OlmPkDecryption?): NativeSize

    external fun olm_pickle_pk_decryption(
        decryption: OlmPkDecryption?,
        key: Pointer?,
        key_length: NativeSize,
        pickled: Pointer?,
        pickled_length: NativeSize
    ): NativeSize

    external fun olm_unpickle_pk_decryption(
        decryption: OlmPkDecryption?,
        key: Pointer?,
        key_length: NativeSize,
        pickled: Pointer?,
        pickled_length: NativeSize,
        pubkey: Pointer?,
        pubkey_length: NativeSize
    ): NativeSize

    external fun olm_pk_max_plaintext_length(
        decryption: OlmPkDecryption?,
        ciphertext_length: NativeSize
    ): NativeSize

    external fun olm_pk_decrypt(
        decryption: OlmPkDecryption?,
        ephemeral_key: Pointer?,
        ephemeral_key_length: NativeSize,
        mac: Pointer?,
        mac_length: NativeSize,
        ciphertext: Pointer?,
        ciphertext_length: NativeSize,
        plaintext: Pointer?,
        max_plaintext_length: NativeSize
    ): NativeSize

    external fun olm_pk_get_private_key(
        decryption: OlmPkDecryption?,
        private_key: Pointer?,
        private_key_length: NativeSize
    ): NativeSize

    external fun olm_pk_signing_size(): NativeSize

    external fun olm_pk_signing(memory: Pointer?): OlmPkSigning?

    external fun olm_pk_signing_last_error(sign: OlmPkSigning?): String?

    external fun olm_clear_pk_signing(sign: OlmPkSigning?): NativeSize

    external fun olm_pk_signing_key_from_seed(
        sign: OlmPkSigning?,
        pubkey: Pointer?,
        pubkey_length: NativeSize,
        seed: Pointer?,
        seed_length: NativeSize
    ): NativeSize

    external fun olm_pk_signing_seed_length(): NativeSize

    external fun olm_pk_signing_public_key_length(): NativeSize

    external fun olm_pk_signature_length(): NativeSize

    external fun olm_pk_sign(
        sign: OlmPkSigning?,
        message: Pointer?,
        message_length: NativeSize,
        signature: Pointer?,
        signature_length: NativeSize
    ): NativeSize

    init {
        Native.register(OlmLibrary::class.java, JNA_NATIVE_LIB)
    }
}
