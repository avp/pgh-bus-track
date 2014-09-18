package com.avp42.pghbustrack.models.stop;

import android.location.Location;
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
    return results[0];
  }

  @Override
  public String toString() {
    return name;
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof Stop)) {
      return false;
    }

    Stop otherStop = (Stop) other;
    return id.equals(otherStop.id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }
}
