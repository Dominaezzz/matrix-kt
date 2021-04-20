package io.github.matrixkt.utils

import io.ktor.http.*
import kotlin.reflect.KClass

public sealed class RpcMethod {
    public object Get : RpcMethod()
    public object Post : RpcMethod()
    public object Put : RpcMethod()
    public object Patch : RpcMethod()
    public object Delete : RpcMethod()
    public object Head : RpcMethod()
    public object Options : RpcMethod()

    public companion object {
        public inline fun <reified T : RpcMethod> fromType(): HttpMethod {
            return fromType(T::class)
        }

        public fun <T : RpcMethod> fromType(klass: KClass<T>): HttpMethod {
            return when (klass) {
                Get::class -> HttpMethod.Get
                Post::class -> HttpMethod.Post
                Put::class -> HttpMethod.Put
                Patch::class -> HttpMethod.Patch
                Delete::class -> HttpMethod.Delete
                Head::class -> HttpMethod.Head
                Options::class -> HttpMethod.Options
                else -> throw NoSuchElementException()
            }
        }
    }
}
