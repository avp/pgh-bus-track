package com.avp42.pghbustrack.view.stop;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.avp42.pghbustrack.R;
import com.avp42.pghbustrack.data.DataCache;
import com.avp42.pghbustrack.models.prediction.Prediction;
import com.avp42.pghbustrack.models.route.Route;
import com.avp42.pghbustrack.util.Util;
import java.util.List;
import static com.avp42.pghbustrack.util.Constants.App.ROUTE_LIST_GRADIENT_FACTOR;

public class PredictionArrayAdapter extends ArrayAdapter<Prediction> {
  public PredictionArrayAdapter(Context context, List<Prediction> predictions) {
    super(context, R.layout.list_element_stop, predictions);
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    Prediction prediction = getItem(position);
    if (convertView == null) {
      convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_element_prediction, parent, false);
    }

    Route route = DataCache.getRouteById(getContext(), prediction.getRouteId());
    if (route != null) {
      TextView routeNameTextView = (TextView) convertView.findViewById(R.id.tv_route_name);
      routeNameTextView.setText(DataCache.getRouteById(getContext(), prediction.getRouteId()).getName());
    }

    TextView routeIdTextView = (TextView) convertView.findViewById(R.id.tv_route_id);
    routeIdTextView.setText(prediction.getRouteId());

    if (route != null) {
      int darkColor = route.getDarkColor();
      GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
          new int[] {darkColor, Util.darken(darkColor, ROUTE_LIST_GRADIENT_FACTOR)});
      //noinspection deprecation
      convertView.setBackgroundDrawable(gradientDrawable);
    }

    TextView routeDirectionTextView = (TextView) convertView.findViewById(R.id.tv_route_direction);
    routeDirectionTextView.setText(prediction.getRouteDirection());

    TextView predictionTimeTextView = (TextView) convertView.findViewById(R.id.tv_prediction_time);
    predictionTimeTextView.setText(String.valueOf(prediction.getMinutesUntilPrediction()));

    return convertView;
  }
}
