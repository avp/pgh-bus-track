package com.avp42.pghbustrack.data;

import android.net.Uri;
import android.util.Log;
import com.avp42.pghbustrack.models.route.Route;
import com.avp42.pghbustrack.models.vehicle.Vehicle;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
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

  private static final Gson gson = new GsonBuilder().create();

  private static final Type vehicleListType = new TypeToken<List<Vehicle>>() { }.getType();
  private static final Type routeListType = new TypeToken<List<Route>>() { }.getType();

  private static final PaacApi INSTANCE = new PaacApi();

  private PaacApi() {}

  public static PaacApi getInstance() {
    return INSTANCE;
  }

  public List<Vehicle> getVehicles(Route route) {
    return getVehicles(Lists.newArrayList(route));
  }

  public List<Vehicle> getVehicles(List<Route> routes) {
    Log.d(LOG_TAG, "Executing Vehicle Request");
    Map<String, String> params = Maps.newHashMap();
    List<String> routeIds = Lists.newArrayList();
    for (Route r : routes) {
      routeIds.add(r.getId());
    }
    params.put("rt", StringUtils.join(routeIds, ","));
    String json = executeRequest("/getvehicles", params);
    JsonArray jsonArray = new JsonParser().parse(json)
        .getAsJsonObject()
        .get("bustime-response")
        .getAsJsonObject()
        .get("vehicle")
        .getAsJsonArray();
    List<Vehicle> vehicles = gson.fromJson(jsonArray, vehicleListType);
    return vehicles;
  }

  public List<Route> getRoutes() {
    Log.d(LOG_TAG, "Executing Routes Request");
    Map<String, String> params = Maps.newHashMap();
    String json = executeRequest("/getroutes", params);
    JsonArray jsonArray = new JsonParser().parse(json)
        .getAsJsonObject()
        .get("bustime-response")
        .getAsJsonObject()
        .get("routes")
        .getAsJsonArray();
    List<Route> routes = gson.fromJson(jsonArray, routeListType);
    return routes;
  }

  protected String executeRequest(String url, Map<String, String> params) {
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
      return null;
    } catch (ClientProtocolException e) {
      return null;
    } catch (IOException e) {
      return null;
    }
  }
}
