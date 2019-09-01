package io.github.matrixkt.utils

import kotlinx.serialization.*
import kotlinx.serialization.internal.SerialClassDescImpl
import kotlinx.serialization.json.JsonInput
import kotlinx.serialization.json.content
import kotlin.reflect.KClass

internal abstract class JsonPolymorphicSerializer<T : Any>(
   private val klass: KClass<T>,
   private val discriminator: String,
   private val fallback: KSerializer<T>? = null
) : KSerializer<T> {
   override val descriptor: SerialDescriptor = object : SerialClassDescImpl(klass.simpleName!!) {
       override val kind: SerialKind get() = UnionKind.SEALED
   }

   override fun serialize(encoder: Encoder, obj: T) {
       val serializer = encoder.context.getPolymorphic(klass, obj)
       return if (serializer != null) {
           val compositeEncoder = encoder.beginStructure(descriptor)
           compositeEncoder.encodeStringElement(descriptor, 0, serializer.descriptor.name)
           @Suppress("UNCHECKED_CAST")
           compositeEncoder.encodeSerializableElement(descriptor, 1, serializer as KSerializer<T>, obj)
           compositeEncoder.endStructure(descriptor)
       } else {
           if (fallback != null) {
               encoder.encode(fallback, obj)
           } else {
               throw SerializationException("No serializer for class ${obj::class}")
           }
       }
   }

   override fun deserialize(decoder: Decoder): T {
       require(decoder is JsonInput)
       val jsonObj = decoder.decodeJson().jsonObject

       val type = jsonObj.getValue(discriminator).content

       val serializer = decoder.context.getPolymorphic(klass, type)
       return if (serializer != null) {
           decoder.json.fromJson(serializer, jsonObj)
       } else {
           if (fallback != null) {
               decoder.json.fromJson(fallback, jsonObj)
           } else {
               throw SerializationException("No serializer for '$discriminator' = '$type'")
           }
       }
   }
}
