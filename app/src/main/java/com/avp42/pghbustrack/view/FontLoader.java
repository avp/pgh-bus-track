package com.avp42.pghbustrack.view;

import android.content.Context;
import android.graphics.Typeface;

public enum FontLoader {
  ARMATA("fonts/Armata-Regular.ttf");

  private String path;

  private FontLoader(final String path) {
    this.path = path;
  }

  public static Typeface getTypeface(Context context, FontLoader font) {
    return Typeface.createFromAsset(context.getAssets(), font.toString());
  }

  public String toString() {
    return path;
  }
}
