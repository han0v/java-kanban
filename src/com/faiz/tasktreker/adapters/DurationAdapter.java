package com.faiz.tasktreker.adapters;

import com.google.gson.*;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonDeserializer;
import java.lang.reflect.Type;
import java.time.Duration;

public class DurationAdapter implements JsonSerializer<Duration>, JsonDeserializer<Duration> {
    @Override
    public JsonElement serialize(Duration duration, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(duration.toString());
    }

    @Override
    public Duration deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        return Duration.parse(json.getAsString());
    }
}
