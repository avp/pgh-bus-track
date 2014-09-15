package com.avp42.pghbustrack.view.stop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.avp42.pghbustrack.R;
import com.avp42.pghbustrack.models.prediction.Prediction;
import java.util.List;

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

    TextView routeIdTextView = (TextView) convertView.findViewById(R.id.tv_route_id);
    routeIdTextView.setText(prediction.getRouteId());

    TextView routeNameTextView = (TextView) convertView.findViewById(R.id.tv_route_name);
    routeNameTextView.setText(prediction.getRouteId());

    TextView predictionTimeTextView = (TextView) convertView.findViewById(R.id.tv_prediction_time);
    predictionTimeTextView.setText(String.valueOf(prediction.getMinutesUntilPrediction()));

    return convertView;
  }
}
