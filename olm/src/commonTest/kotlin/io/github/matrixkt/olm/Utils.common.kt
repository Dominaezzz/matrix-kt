package io.github.matrixkt.olm

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

expect inline fun withOlmInit(crossinline block: () -> Unit)

@OptIn(ExperimentalContracts::class)
inline fun <T> withAccount(block: (Account) -> T): T {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }

    val account = Account()
    return try {
        block(account)
    } finally {
        account.clear()
    }
}
