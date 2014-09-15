package com.avp42.pghbustrack.util;

import android.graphics.Color;
import static com.avp42.pghbustrack.util.Constants.App.MAX_METERS_FOR_FEET;

public class Util {
  private Util() {
    throw new AssertionError("Util cannot be instantiated.");
  }

  public static boolean isColorDark(int color) {
    return 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255 >= 0.5;
  }

  public static int darken(int color, double factor) {
    float[] hsv = new float[3];
    Color.colorToHSV(color, hsv);
    hsv[2] *= factor;
    return Color.HSVToColor(hsv);
  }

  public static String humanizeDistance(double meters) {
    if (meters < MAX_METERS_FOR_FEET) {
      return metersToFeet(meters) + " feet";
    }
    double miles = metersToMiles(meters);
    return Math.round(miles * 100.0) / 100.0 + " miles";
  }

  private static double metersToMiles(double meters) {
    return meters * 0.000621371192;
  }

  private static int metersToFeet(double meters) {
    return (int) Math.round(meters * 3.28084);
  }
}
