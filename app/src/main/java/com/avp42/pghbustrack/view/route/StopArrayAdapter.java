package com.avp42.pghbustrack.view.route;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.avp42.pghbustrack.R;
import com.avp42.pghbustrack.models.stop.Stop;
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

    return convertView;
  }
}
