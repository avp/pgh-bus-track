package com.avp42.pghbustrack.data;

import com.avp42.pghbustrack.models.route.Route;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;

public class RouteCache {
  private static final Map<String, Route> routeMap = Maps.newHashMap();

  private RouteCache() {
    throw new AssertionError("RouteCache cannot be instantiated.");
  }

  public static void updateRoutes(List<Route> routeList) {
    for (Route route : routeList) {
      routeMap.put(route.getId(), route);
    }
  }

  public static List<Route> getRoutes() {
    return Lists.newArrayList(routeMap.values());
  }

  public static Route getRouteById(String id) {
    return routeMap.get(id);
  }
}
