package io.github.matrixkt.olm

import org.khronos.webgl.Uint8Array


public actual class PkSigning actual constructor(seed: ByteArray) {
    private val ptr = JsOlm.PkSigning()

    public actual val publicKey: String

    init {
        try {
            this.publicKey = ptr.init_with_seed(Uint8Array(seed.toTypedArray()))
        } catch (e: Exception) {
            clear()
            throw e
        }
    }

    public actual fun clear() {
        ptr.free()
    }

    public actual fun sign(message: String): String {
        return ptr.sign(message)
    }

    public actual companion object {
        public actual val seedLength: Long
          get() {
              val seedRandom = JsOlm.PkSigning().generate_seed()
              return seedRandom.length.toLong()
        }
    }
}
