package com.avp42.pghbustrack.models.route;

import com.google.gson.annotations.SerializedName;

public class Route {
  @SerializedName("rt")
  private String id;

  @SerializedName("rtnm")
  private String name;

  @SerializedName("rtclr")
  private String color;

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getColor() {
    return color;
  }

  public String toString() {
    return this.name;
  }
}
