package com.avp42.pghbustrack.models.prediction;

import com.google.gson.annotations.SerializedName;
import org.joda.time.DateTime;
import org.joda.time.Minutes;

public class Prediction {
  @SerializedName("tmstmp")
  private DateTime timestamp;

  @SerializedName("typ")
  private PredictionType type;

  @SerializedName("stpid")
  private String stopId;

  @SerializedName("stpnm")
  private String stopName;

  @SerializedName("vid")
  private String vehicleId;

  @SerializedName("dstp")
  private String distance;

  @SerializedName("rt")
  private String routeId;

  @SerializedName("rtdir")
  private String routeDirection;

  @SerializedName("des")
  private String destination;

  @SerializedName("prdtm")
  private DateTime predictionTime;

  @SerializedName("dly")
  private boolean delayed;

  public String getRouteId() {
    return routeId;
  }

  public DateTime getPredictionTime() {
    return predictionTime;
  }

  public int getMinutesUntilPrediction() {
    return Minutes.minutesBetween(new DateTime(), predictionTime).getMinutes();
  }

  public String toString() {
    return predictionTime.toString();
  }
}
