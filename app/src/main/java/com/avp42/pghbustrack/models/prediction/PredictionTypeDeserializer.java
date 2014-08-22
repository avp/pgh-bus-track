package com.avp42.pghbustrack.models.prediction;

import android.util.Log;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import static com.avp42.pghbustrack.util.Constants.App.LOG_TAG;

public class PredictionTypeDeserializer implements JsonDeserializer<PredictionType> {

  @Override
  public PredictionType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
    String string = json.getAsString();
    if (string.equals("A")) {
      return PredictionType.ARRIVAL;
    } else if (string.equals("D")) {
      return PredictionType.DEPARTURE;
    }
    Log.w(LOG_TAG, string + " is an invalid PredictionType");
    return null;
  }
}
