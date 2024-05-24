package com.client.myapplication.data.util

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.jsonPrimitive

object ConversionRatesSerializer : KSerializer<Map<String, Double>> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("ConversionRates") {
        element<Map<String, Double>>("conversion_rates")
    }

    override fun serialize(encoder: Encoder, value: Map<String, Double>) {
        encoder.encodeSerializableValue(
            MapSerializer(
                String.serializer(),
                Double.serializer()
            ), value
        )
    }

    override fun deserialize(decoder: Decoder): Map<String, Double> {
        val input = decoder.decodeSerializableValue(JsonObject.serializer())
        return input.mapNotNull { (key, element) ->
            val rate = element.jsonPrimitive.doubleOrNull
            if (rate != null) key to rate else null
        }.toMap()
    }
}