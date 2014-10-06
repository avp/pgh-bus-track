package com.avp42.pghbustrack.models;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.lang.reflect.Type;

public class DateTimeSerializer implements JsonSerializer<DateTime> {

  @Override
  public JsonElement serialize(DateTime src, Type typeOfSrc, JsonSerializationContext context) {
    DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyyMMdd HH:mm");
    return new JsonPrimitive(formatter.print(src));
  }

}
