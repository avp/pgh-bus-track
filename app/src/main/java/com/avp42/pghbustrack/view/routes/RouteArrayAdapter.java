package com.avp42.pghbustrack.view.routes;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.avp42.pghbustrack.R;
import com.avp42.pghbustrack.models.route.Route;
import com.avp42.pghbustrack.util.Util;
import com.avp42.pghbustrack.view.FontLoader;
import java.util.List;
import static com.avp42.pghbustrack.util.Constants.App.ROUTE_LIST_GRADIENT_FACTOR;

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


    LinearLayout routeListLayout = (LinearLayout) convertView.findViewById(R.id.route_list_layout);
    int darkColor = route.getDarkColor();
    GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
        new int[]{darkColor, Util.darken(darkColor, ROUTE_LIST_GRADIENT_FACTOR)});
    routeListLayout.setBackgroundDrawable(gradientDrawable);

    TextView routeNameView = (TextView) convertView.findViewById(R.id.tv_route_name);
    routeNameView.setText(route.getName());
    routeNameView.setTypeface(FontLoader.getTypeface(getContext(), FontLoader.ARMATA));

    TextView routeIdView = (TextView) convertView.findViewById(R.id.tv_route_id);
    routeIdView.setText(route.getId());
    routeIdView.setTypeface(FontLoader.getTypeface(getContext(), FontLoader.ARMATA));

    return convertView;
  }
}
