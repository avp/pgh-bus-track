package com.avp42.pghbustrack.models.vehicle;

import com.google.gson.annotations.SerializedName;
import org.joda.time.DateTime;

public class Vehicle {
  @SerializedName("vid")
  private String id;

  @SerializedName("tmstmp")
  private DateTime timestamp;

  @SerializedName("lat")
  private String latitude;

  @SerializedName("lon")
  private String longitude;

  @SerializedName("hdg")
  private String heading;

  @SerializedName("rt")
  private String route;

  @SerializedName("pid")
  private int patternId;

  @SerializedName("pDist")
  private int distance;

  @SerializedName("spd")
  private int speed;

  @SerializedName("des")
  private String destination;
}
