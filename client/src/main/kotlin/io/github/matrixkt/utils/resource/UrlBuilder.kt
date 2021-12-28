package io.github.matrixkt.utils.resource

import io.ktor.http.*
import io.ktor.util.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.serializer

/**
 * Constructs the url for [resource].
 *
 * The class of [resource] instance **must** be annotated with [Resource].
 */
@OptIn(ExperimentalSerializationApi::class)
public inline fun <reified T> href(
    resource: T,
    urlBuilder: URLBuilder
) {
    val serializer = serializer<T>()
    val parameters = ResourcesFormat.encodeToParameters(serializer, resource)
    val pathPattern = ResourcesFormat.encodeToPathPattern(serializer)

    val usedForPathParameterNames = mutableSetOf<String>()
    val pathParts = pathPattern.split("/")

    val updatedParts = pathParts.flatMap {
        if (!it.startsWith('{') || !it.endsWith('}')) return@flatMap listOf(it)

        val part = it.substring(1, it.lastIndex)
        when {
            part.endsWith('?') -> {
                val values = parameters.getAll(part.dropLast(1)) ?: return@flatMap emptyList()
                check(values.size <= 1) {
                    "Expect zero or one parameter with name: ${part.dropLast(1)}, but found ${values.size}"
                }
                usedForPathParameterNames += part.dropLast(1)
                values
            }
            part.endsWith("...") -> {
                usedForPathParameterNames += part.dropLast(3)
                parameters.getAll(part.dropLast(3)) ?: emptyList()
            }
            else -> {
                val values = parameters.getAll(part)
                check(!(values == null || values.size != 1)) {
                    "Expect exactly one parameter with name: $part, but found ${values?.size ?: 0}"
                }
                usedForPathParameterNames += part
                values
            }
        }
    }

    urlBuilder.path(*updatedParts.toTypedArray())

    val queryArgs = parameters.filter { key, _ -> !usedForPathParameterNames.contains(key) }
    urlBuilder.parameters.appendAll(queryArgs)
}

/**
 * Constructs the url for [resource].
 *
 * The class of [resource] instance **must** be annotated with [Resource].
 */
@OptIn(ExperimentalSerializationApi::class)
public inline fun <reified T> href(
    resource: T,
): String {
    val urlBuilder = URLBuilder()
    href(resource, urlBuilder)
    return urlBuilder.build().fullPath
}
