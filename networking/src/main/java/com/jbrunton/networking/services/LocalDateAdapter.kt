package com.jbrunton.networking.services

import com.google.gson.*
import org.joda.time.LocalDate
import java.lang.reflect.Type

class LocalDateAdapter : JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {
    override fun serialize(src: LocalDate, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        return JsonPrimitive(src.toString())
    }

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): LocalDate? {
        val dateString = json.asJsonPrimitive.asString
        return if (dateString.isEmpty()) {
            null
        } else {
            LocalDate(dateString)
        }
    }
}