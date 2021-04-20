package io.github.matrixkt.utils.resource

import io.ktor.http.*
import kotlinx.serialization.*
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.descriptors.elementDescriptors
import kotlinx.serialization.descriptors.elementNames
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule

@OptIn(ExperimentalSerializationApi::class)
public object ResourcesFormat : SerialFormat {
    override val serializersModule: SerializersModule get() = EmptySerializersModule

    public data class Parameter(
        val name: String,
        val isOptional: Boolean
    )

    public fun <T> encodeToPathPattern(serializer: SerializationStrategy<T>): String {
        val pathBuilder = StringBuilder()

        var current: SerialDescriptor? = serializer.descriptor
        while (current != null) {
            val path = current.annotations.filterIsInstance<Resource>().first().path
            val addSlash = pathBuilder.isNotEmpty() && !pathBuilder.startsWith('/') && !path.endsWith('/')
            if (addSlash) {
                pathBuilder.insert(0, '/')
            }
            pathBuilder.insert(0, path)

            val membersWithAnnotations = current.elementDescriptors.filter { it.annotations.any { it is Resource } }
            check(membersWithAnnotations.size <= 1) {
                "There are multiple parents for resource ${current!!.serialName}"
            }
            current = membersWithAnnotations.firstOrNull()
        }

        if (pathBuilder.startsWith('/')) {
            pathBuilder.deleteAt(0)
        }
        return pathBuilder.toString()
    }

    public fun <T> encodeToQueryParameters(serializer: SerializationStrategy<T>): Set<Parameter> {
        val path = encodeToPathPattern(serializer)

        val allParameters = mutableSetOf<Parameter>()
        collectAllParameters(serializer.descriptor, allParameters)

        return allParameters
            .filterNot { (name, _) ->
                path.contains("{$name}") || path.contains("{$name?}") || path.contains("{$name...}")
            }
            .toSet()
    }

    private fun collectAllParameters(descriptor: SerialDescriptor, result: MutableSet<Parameter>) {
        descriptor.elementNames.forEach { name ->
            val index = descriptor.getElementIndex(name)
            val elementDescriptor = descriptor.getElementDescriptor(index)
            if (elementDescriptor.kind is StructureKind.CLASS) {
                collectAllParameters(elementDescriptor, result)
            } else {
                result.add(Parameter(name, descriptor.isElementOptional(index)))
            }
        }
    }

    public fun <T> encodeToParameters(serializer: SerializationStrategy<T>, value: T): Parameters {
        val encoder = ParametersEncoder(serializersModule)
        encoder.encodeSerializableValue(serializer, value)
        return encoder.parameters
    }
}
