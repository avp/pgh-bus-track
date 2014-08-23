package com.avp42.pghbustrack.models.stop;

import android.location.Location;
import com.avp42.pghbustrack.util.Util;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Stop implements Serializable {
  private static final long serialVersionUID = -8185932649815385844L;

  @SerializedName("stpid")
  private String id;

  @SerializedName("stpnm")
  private String name;

  @SerializedName("lat")
  private double latitude;

  @SerializedName("lon")
  private double longitude;

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public double distanceFrom(Location location) {
    float[] results = new float[3];
    Location.distanceBetween(this.latitude, this.longitude, location.getLatitude(), location.getLongitude(), results);
    return Util.metersToMiles(results[0]);
  }

  public String toString() {
    return name;
  }
}
