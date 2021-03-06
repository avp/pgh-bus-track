package com.avp42.pghbustrack.view.stop;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.avp42.pghbustrack.R;
import com.avp42.pghbustrack.models.stop.Stop;
import com.avp42.pghbustrack.util.Util;
import com.avp42.pghbustrack.view.MainActivity;
import java.util.List;

public class StopArrayAdapter extends ArrayAdapter<Stop> {
  public StopArrayAdapter(Context context, List<Stop> stops) {
    super(context, R.layout.list_element_stop, stops);
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    Stop stop = getItem(position);
    if (convertView == null) {
      convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_element_stop, parent, false);
    }

    TextView stopIdTextView = (TextView) convertView.findViewById(R.id.tv_stop_name);
    stopIdTextView.setText(stop.getName());

    TextView stopDistanceTextView = (TextView) convertView.findViewById(R.id.tv_stop_distance);
    Location curLocation = ((MainActivity) getContext()).getLocation();
    stopDistanceTextView.setText(Util.humanizeDistance(stop.distanceFrom(curLocation)));

    return convertView;
  }
}
