package com.avp42.pghbustrack.models.stop;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Stop implements Serializable {
  private static final long serialVersionUID = -8185932649815385844L;

  @SerializedName("stpid")
  private String id;

  @SerializedName("stpnm")
  private String name;

  @SerializedName("lat")
  private String latitude;

  @SerializedName("lon")
  private String longitude;

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String toString() {
    return name;
  }
}
