package com.faiz.tasktreker.adapters;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.Duration;

public class DurationAdapter implements JsonSerializer<Duration>, JsonDeserializer<Duration> {
    @Override
    public JsonElement serialize(Duration duration, Type typeOfSrc, JsonSerializationContext context) {
        if (duration == null) {
            return JsonNull.INSTANCE;
        }
        return new JsonPrimitive(duration.toString());
    }

    @Override
    public Duration deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        if (json.isJsonNull() || "null".equals(json.getAsString())) {
            return null;
        }
        return Duration.parse(json.getAsString());
    }

}
