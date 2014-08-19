package com.avp42.pghbustrack.models.route;

import com.google.gson.annotations.SerializedName;

public class Route {
  @SerializedName("rt")
  private String route;

  @SerializedName("rtnm")
  private String name;

  @SerializedName("rtclr")
  private String color;
}
