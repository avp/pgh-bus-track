package com.avp42.pghbustrack.util;

import android.graphics.Color;
import com.google.common.collect.Lists;
import java.util.List;

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

  public static <T> List<List<T>> partitionList(List<T> list, int partitionSize) {
    List<List<T>> partitions = Lists.newArrayList();
    for (int i = 0; i < list.size(); i += partitionSize) {
      List<T> sublist = list.subList(i, i + Math.min(partitionSize, list.size() - i));
      partitions.add(sublist);
    }
    return partitions;
  }

  public static String humanizeDistance(double meters) {
    double miles = metersToMiles(meters);
    if (miles < 1) {
      return milesToFeet(miles) + " feet";
    }
    return Math.round(miles * 100.0) / 100.0 + " miles";
  }

  private static double metersToMiles(double meters) {
    return meters * 0.000621371192;
  }

  private static int milesToFeet(double miles) {
    return (int) miles / 5280;
  }
}
