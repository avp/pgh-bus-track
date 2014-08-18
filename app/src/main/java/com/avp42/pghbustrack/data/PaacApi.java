package com.avp42.pghbustrack.data;

import com.avp42.pghbustrack.models.vehicle.Vehicle;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import java.util.List;

public class PaacApi {
  private static final HttpClient httpClient = new DefaultHttpClient();

  private PaacApi() {}

  public static List<Vehicle> getVehicles() {
    return null;
  }
}
