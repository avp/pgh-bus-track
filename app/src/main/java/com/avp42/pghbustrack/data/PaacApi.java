package com.avp42.pghbustrack.data;

import android.net.Uri;
import android.util.Log;
import com.avp42.pghbustrack.models.DateTimeDeserializer;
import com.avp42.pghbustrack.models.direction.Direction;
import com.avp42.pghbustrack.models.prediction.Prediction;
import com.avp42.pghbustrack.models.prediction.PredictionType;
import com.avp42.pghbustrack.models.prediction.PredictionTypeDeserializer;
import com.avp42.pghbustrack.models.route.Route;
import com.avp42.pghbustrack.models.stop.Stop;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.joda.time.DateTime;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import static com.avp42.pghbustrack.util.Constants.Api.API_KEY;
import static com.avp42.pghbustrack.util.Constants.Api.BASE_URL;
import static com.avp42.pghbustrack.util.Constants.App.LOG_TAG;

public class PaacApi {
  private static final HttpClient httpClient = new DefaultHttpClient();

  private static final Gson gson = new GsonBuilder().registerTypeAdapter(DateTime.class, new DateTimeDeserializer())
      .registerTypeAdapter(PredictionType.class, new PredictionTypeDeserializer())
      .create();

  private static final Type routeListType = new TypeToken<List<Route>>() { }.getType();
  private static final Type stopListType = new TypeToken<List<Stop>>() { }.getType();
  private static final Type directionListType = new TypeToken<List<Direction>>() { }.getType();
  private static final Type predictionListType = new TypeToken<List<Prediction>>() { }.getType();

  private static final PaacApi INSTANCE = new PaacApi();

  private PaacApi() {}

  public static PaacApi getInstance() {
    return INSTANCE;
  }

  public List<Route> getRoutes() throws IOException {
    Log.d(LOG_TAG, "Executing Routes Request");
    Map<String, String> params = Maps.newHashMap();
    String json = executeRequest("/getroutes", params);
    JsonArray jsonArray = new JsonParser().parse(json)
        .getAsJsonObject()
        .get("bustime-response")
        .getAsJsonObject()
        .get("routes")
        .getAsJsonArray();
    return gson.fromJson(jsonArray, routeListType);
  }

  public List<Direction> getDirections(Route route) throws IOException {
    Log.d(LOG_TAG, "Executing Stops Request");
    Map<String, String> params = Maps.newHashMap();
    params.put("rt", route.getId());
    String json = executeRequest("/getdirections", params);
    JsonArray jsonArray = new JsonParser().parse(json)
        .getAsJsonObject()
        .get("bustime-response")
        .getAsJsonObject()
        .get("directions")
        .getAsJsonArray();
    return gson.fromJson(jsonArray, directionListType);
  }

  public List<Stop> getStops(Route route, Direction direction) throws IOException {
    Log.d(LOG_TAG, "Executing Stops Request");
    Map<String, String> params = Maps.newHashMap();
    params.put("rt", route.getId());
    params.put("dir", direction.getDirection());
    String json = executeRequest("/getstops", params);
    JsonArray jsonArray = new JsonParser().parse(json)
        .getAsJsonObject()
        .get("bustime-response")
        .getAsJsonObject()
        .get("stops")
        .getAsJsonArray();
    return gson.fromJson(jsonArray, stopListType);
  }

  public List<Prediction> getPredictions(List<Stop> stops) throws IOException {
    Log.d(LOG_TAG, "Executing Prediction Request");
    Map<String, String> params = Maps.newHashMap();
    List<String> stopIds = Lists.newArrayList();
    for (Stop stop : stops) {
      stopIds.add(stop.getId());
    }
    params.put("stpid", StringUtils.join(stopIds, ","));
    String json = executeRequest("/getpredictions", params);

    JsonElement jsonElement = new JsonParser().parse(json)
        .getAsJsonObject()
        .get("bustime-response")
        .getAsJsonObject()
        .get("prd");

    if (jsonElement != null) {
      return gson.fromJson(jsonElement.getAsJsonArray(), predictionListType);
    }
    return Lists.newArrayList();
  }

  private String executeRequest(String url, Map<String, String> params) throws IOException {
    Uri.Builder uri = Uri.parse(BASE_URL + url).buildUpon();

    for (Map.Entry<String, String> entry : params.entrySet()) {
      uri.appendQueryParameter(entry.getKey(), entry.getValue());
    }

    String uriString = uri.appendQueryParameter("key", API_KEY)
        .appendQueryParameter("format", "json")
        .appendQueryParameter("localestring", "en_US")
        .build()
        .toString();

    Log.i(LOG_TAG, uriString);

    try {
      HttpGet httpGet = new HttpGet(uriString);
      HttpResponse response = httpClient.execute(httpGet);
      Log.i(LOG_TAG, response.getStatusLine().toString());
      HttpEntity entity = response.getEntity();
      if (entity != null) {
        InputStream inputStream = entity.getContent();
        String result = IOUtils.toString(inputStream);
        inputStream.close();
        return result;
      }
      return StringUtils.EMPTY;
    } catch (ClientProtocolException e) {
      return StringUtils.EMPTY;
    }
  }
}
