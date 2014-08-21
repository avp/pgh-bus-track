package com.avp42.pghbustrack.models.direction;

import com.google.gson.annotations.SerializedName;

public class Direction {
  @SerializedName("dir")
  private String direction;

  public String getDirection() {
    return direction;
  }

  public String toString() {
    return direction;
  }
}
