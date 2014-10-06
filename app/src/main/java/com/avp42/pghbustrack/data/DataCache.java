package com.avp42.pghbustrack.data;

import android.content.Context;
import com.avp42.pghbustrack.models.route.Route;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;
import static android.content.Context.MODE_PRIVATE;
import static com.avp42.pghbustrack.BuildConfig.PACKAGE_NAME;

public class DataCache {
  private static final Gson gson = new GsonBuilder().create();

  private static final Type routeListType = new TypeToken<List<Route>>() { }.getType();

  private DataCache() {
    throw new AssertionError("RouteCache cannot be instantiated.");
  }

  public static void cacheRoutes(Context context, List<Route> routes) {
    context.getSharedPreferences(PACKAGE_NAME, MODE_PRIVATE)
        .edit()
        .putString("routes", gson.toJson(routes))
        .apply();
  }

  public static List<Route> getRoutes(Context context) {
    return gson.fromJson(
        context.getSharedPreferences(PACKAGE_NAME, MODE_PRIVATE).getString("routes", "[]"),
        routeListType);
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
}
