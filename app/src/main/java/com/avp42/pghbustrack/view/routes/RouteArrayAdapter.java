package com.avp42.pghbustrack.view.routes;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
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

    int bgColor = Color.parseColor(route.getColor());
    float[] hsv = new float[3];
    Color.colorToHSV(bgColor, hsv);
    hsv[1] /= 3;
    bgColor = Color.HSVToColor(hsv);

    LinearLayout routeListLayout = (LinearLayout) convertView.findViewById(R.id.route_list_layout);
    routeListLayout.setBackgroundColor(bgColor);

    TextView routeNameView = (TextView) convertView.findViewById(R.id.tv_route_name);
    routeNameView.setText(route.getName());

    TextView routeIdView = (TextView) convertView.findViewById(R.id.tv_route_id);
    routeIdView.setText(route.getId());

    return convertView;
  }
}
