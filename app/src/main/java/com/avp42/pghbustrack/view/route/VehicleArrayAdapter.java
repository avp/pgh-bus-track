package com.avp42.pghbustrack.view.routes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.avp42.pghbustrack.R;
import com.avp42.pghbustrack.models.vehicle.Vehicle;
import java.util.List;

public class VehicleArrayAdapter extends ArrayAdapter<Vehicle> {
  public VehicleArrayAdapter(Context context, List<Vehicle> vehicles) {
    super(context, R.layout.list_element_vehicle, vehicles);
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    Vehicle vehicle = getItem(position);
    if (convertView == null) {
      convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_element_vehicle, parent, false);
    }

    TextView vehicleIdTextView = (TextView) convertView.findViewById(R.id.tv_vehicle_id);
    vehicleIdTextView.setText(vehicle.getId());

    TextView vehicleDestinationTextView = (TextView) convertView.findViewById(R.id.tv_vehicle_destination);
    vehicleDestinationTextView.setText(vehicle.getDestination());

    return convertView;
  }
}
