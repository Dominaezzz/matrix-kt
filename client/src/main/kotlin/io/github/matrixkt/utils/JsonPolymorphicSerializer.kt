package io.github.matrixkt.utils

import kotlinx.serialization.*
import kotlinx.serialization.json.JsonInput
import kotlinx.serialization.json.content
import kotlin.reflect.KClass

internal abstract class JsonPolymorphicSerializer<T : Any>(
   private val klass: KClass<T>,
   private val discriminator: String,
   private val fallback: KSerializer<T>? = null
) : KSerializer<T> {
   override val descriptor = SerialDescriptor(klass.simpleName!!, PolymorphicKind.SEALED)

   override fun serialize(encoder: Encoder, value: T) {
       val serializer = encoder.context.getPolymorphic(klass, value)
       return if (serializer != null) {
           encoder.encodeStructure(descriptor) {
               encodeStringElement(descriptor, 0, serializer.descriptor.serialName)
               @Suppress("UNCHECKED_CAST")
               encodeSerializableElement(descriptor, 1, serializer as KSerializer<T>, value)
           }
       } else {
           if (fallback != null) {
               encoder.encode(fallback, value)
           } else {
               throw SerializationException("No serializer for class ${value::class}")
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
