package com.avp42.pghbustrack.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import com.avp42.pghbustrack.BuildConfig;
import com.avp42.pghbustrack.models.DateTimeDeserializer;
import com.avp42.pghbustrack.models.DateTimeSerializer;
import com.avp42.pghbustrack.models.prediction.Prediction;
import com.avp42.pghbustrack.models.prediction.PredictionType;
import com.avp42.pghbustrack.models.prediction.PredictionTypeDeserializer;
import com.avp42.pghbustrack.models.prediction.PredictionTypeSerializer;
import com.avp42.pghbustrack.models.route.Route;
import com.avp42.pghbustrack.models.stop.Stop;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.joda.time.DateTime;
import java.lang.reflect.Type;
import java.util.List;

public class DataCache {
  private static final Gson gson = new GsonBuilder().registerTypeAdapter(DateTime.class, new DateTimeDeserializer())
      .registerTypeAdapter(DateTime.class, new DateTimeSerializer())
      .registerTypeAdapter(PredictionType.class, new PredictionTypeDeserializer())
      .registerTypeAdapter(PredictionType.class, new PredictionTypeSerializer())
      .create();

  private static final String KEY_ROUTES = "routes";
  private static final String KEY_PREDICTIONS = "predictions";
  private static final String KEY_STOPS = "stops";

  private DataCache() {
    throw new AssertionError("RouteCache cannot be instantiated.");
  }

  public static void cacheRoutes(final Context context, final List<Route> routes) {
    new AsyncTask<Void, Void, Void>() {
      @Override
      protected Void doInBackground(Void... params) {
        getSharedPreferences(context).edit().putString(KEY_ROUTES, gson.toJson(routes)).apply();
        return null;
      }
    }.execute();
  }

  public static List<Route> getRoutes(Context context) {
    Type routeListType = new TypeToken<List<Route>>() { }.getType();
    SharedPreferences sharedPreferences = getSharedPreferences(context);
    return gson.fromJson(sharedPreferences.getString(KEY_ROUTES, "[]"), routeListType);
  }

  public static Route getRouteById(Context context, String id) {
    List<Route> routes = getRoutes(context);
    for (Route route : routes) {
      if (route.getId().equals(id)) {
        return route;
      }
    }
    return null;
  }

  private static String getStopCacheKey(Route route) {
    return String.format("%s-%s", KEY_STOPS, route.getId());
  }

  public static void cacheStops(final Context context, final Route route, final List<Stop> stops) {
    new AsyncTask<Void, Void, Void>() {
      @Override
      protected Void doInBackground(Void... params) {
        getSharedPreferences(context).edit().putString(getStopCacheKey(route), gson.toJson(stops)).apply();
        return null;
      }
    }.execute();
  }

  public static List<Stop> getStops(Context context, Route route) {
    Type stopListType = new TypeToken<List<Stop>>() { }.getType();
    SharedPreferences sharedPreferences = getSharedPreferences(context);
    return gson.fromJson(sharedPreferences.getString(getStopCacheKey(route), "[]"), stopListType);
  }

  private static String getPredictionCacheKey(Stop stop) {
    return String.format("%s-%s", KEY_PREDICTIONS, stop.getId());
  }

  public static void cachePredictions(final Context context, final Stop stop, final List<Prediction> predictions) {
    new AsyncTask<Void, Void, Void>() {
      @Override
      protected Void doInBackground(Void... params) {
        getSharedPreferences(context).edit().putString(getPredictionCacheKey(stop), gson.toJson(predictions)).apply();
        return null;
      }
    }.execute();
  }

  public static List<Prediction> getPredictions(Context context, Stop stop) {
    Type predictionListType = new TypeToken<List<Prediction>>() { }.getType();
    SharedPreferences sharedPreferences = getSharedPreferences(context);
    return gson.fromJson(sharedPreferences.getString(getPredictionCacheKey(stop), "[]"), predictionListType);
  }

  private static SharedPreferences getSharedPreferences(Context context) {
    String filename = String.format("%s_%s", BuildConfig.PACKAGE_NAME, "datacache");
    return context.getSharedPreferences(filename, Context.MODE_PRIVATE);
  }
}
