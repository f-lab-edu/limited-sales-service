package com.limited.sales.utils;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** LocalDateTime 형식을 바꿔 주기 위함. */
public class GsonUtils {
  private static String PATTERN_DATE = "yyyy-MM-dd";
  private static String PATTERN_TIME = "HH:mm:ss";
  private static String PATTERN_DATETIME = String.format("%s %s", PATTERN_DATE, PATTERN_TIME);

  private static Gson gson =
      new GsonBuilder()
          .disableHtmlEscaping()
          .setDateFormat(PATTERN_DATETIME)
          .registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeAdapter())
          .create();

  public static String toJson(Object o) {
    String result = gson.toJson(o);
    if ("string".equals(result)) return null;
    return result;
  }

  static class GsonLocalDateTimeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {
    @Override
    public JsonElement serialize(LocalDateTime localDateTime, Type srcType, JsonSerializationContext context) {
      return new JsonPrimitive(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(localDateTime));
    }

    @Override public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
      return LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
  }

}
