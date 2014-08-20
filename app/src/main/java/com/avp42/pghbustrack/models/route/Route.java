package com.avp42.pghbustrack.models.route;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Route implements Serializable {
  private static final long serialVersionUID = 6497937177310136759L;

  public Route(String id, String name, String color) {
    this.id = id;
    this.name = name;
    this.color = color;
  }

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
