package com.avp42.pghbustrack.view.routes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.avp42.pghbustrack.R;
import com.avp42.pghbustrack.models.route.Route;
import java.util.List;

public class RouteArrayAdapter extends ArrayAdapter<Route> {
  public RouteArrayAdapter(Context context, List<Route> routes) {
    super(context, R.layout.list_element_route, routes);
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    Route route = getItem(position);
    if (convertView == null) {
      convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_element_route, parent, false);
    }

    TextView routeNameView = (TextView) convertView.findViewById(R.id.tv_route_name);
    routeNameView.setText(route.getName());
    return convertView;
  }
}
