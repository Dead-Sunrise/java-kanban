package controller;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.Duration;

public class DurationTypeAdapter implements JsonSerializer<Duration>, JsonDeserializer<Duration> {
    @Override
    public JsonElement serialize(Duration duration, Type type, JsonSerializationContext context) {
        return new JsonPrimitive(duration.toMinutes());
    }

    @Override
    public Duration deserialize(JsonElement json, Type type, JsonDeserializationContext context) {
        return Duration.ofMinutes(json.getAsLong());
    }
}
