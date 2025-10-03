package com.github.joechung2008.diagnostics_android.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonObject

@OptIn(ExperimentalSerializationApi::class)
object NullableExtensionSerializer : KSerializer<Extension?> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("Extension")

    override fun serialize(encoder: Encoder, value: Extension?) {
        when (value) {
            is ExtensionInfo -> encoder.encodeSerializableValue(ExtensionInfo.serializer(), value)
            is ExtensionError -> encoder.encodeSerializableValue(ExtensionError.serializer(), value)
            null -> encoder.encodeNull()
        }
    }

    override fun deserialize(decoder: Decoder): Extension? {
        val jsonDecoder = decoder as? JsonDecoder ?: return null
        val jsonElement = jsonDecoder.decodeJsonElement()

        if (jsonElement !is JsonObject) {
            return null // This handles nulls, primitives, or arrays from the API
        }

        return when {
            "extensionName" in jsonElement -> jsonDecoder.json.decodeFromJsonElement(ExtensionInfo.serializer(), jsonElement)
            "lastError" in jsonElement -> jsonDecoder.json.decodeFromJsonElement(ExtensionError.serializer(), jsonElement)
            else -> null
        }
    }
}

@Serializable(with = NullableExtensionSerializer::class)
sealed interface Extension

@Serializable
data class ExtensionInfo(
    val extensionName: String,
    val config: Map<String, String>? = null,
    val stageDefinition: Map<String, List<String>>? = null
) : Extension

@Serializable
data class ExtensionError(
    val lastError: LastError
) : Extension

@Serializable
data class LastError(
    val errorMessage: String,
    val time: String
)
