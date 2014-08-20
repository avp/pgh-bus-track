package com.avp42.pghbustrack.util;

import android.graphics.Color;

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
}
