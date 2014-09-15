package com.avp42.pghbustrack.util;

public class Constants {
  public static class App {
    public static final String LOG_TAG = "PghBusTrack";
    public static final double ROUTE_LIST_GRADIENT_FACTOR = 0.55;
    public static final double MAX_METERS_FOR_FEET = 1609.34 * 0.5; // Max number of meters for feet instead of miles
  }

  public static class Location {
    public static final int UPDATE_INTERVAL = 10000;
    public static final int FASTEST_INTERVAL = 5000;
  }

  public static class Api {
    public static final String BASE_URL = "http://realtime.portauthority.org/bustime/api/v2";
    public static final String API_KEY = "dsMivDwaBRCLNk3ErA6rZ9Qvp";
  }
}
