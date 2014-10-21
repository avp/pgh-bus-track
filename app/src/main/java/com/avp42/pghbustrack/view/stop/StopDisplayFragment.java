package com.avp42.pghbustrack.view.stop;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.avp42.pghbustrack.R;
import com.avp42.pghbustrack.data.DataCache;
import com.avp42.pghbustrack.data.PaacApi;
import com.avp42.pghbustrack.models.prediction.Prediction;
import com.avp42.pghbustrack.models.stop.Stop;
import com.avp42.pghbustrack.util.Util;
import com.avp42.pghbustrack.view.FontLoader;
import com.avp42.pghbustrack.view.MainActivity;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.List;
import static com.avp42.pghbustrack.util.Constants.App.ROUTE_LIST_GRADIENT_FACTOR;

public class StopDisplayFragment extends Fragment {
  private ListView predictionListView;

  private RelativeLayout progressBar;

  private Stop stop;

  private Activity activity;

  public static StopDisplayFragment newInstance(Stop stop) {
    StopDisplayFragment fragment = new StopDisplayFragment();
    Bundle args = new Bundle();
    args.putSerializable("stop", stop);
    fragment.setArguments(args);
    return fragment;
  }

  public StopDisplayFragment() {}

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    this.activity = activity;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_stopdisplay, container, false);
    this.stop = (Stop) getArguments().getSerializable("stop");

    ActionBar actionBar = this.activity.getActionBar();
    if (actionBar != null) {
      ((MainActivity) getActivity()).getNavigationDrawerFragment().setDrawerIndicatorEnabled(false);
      setHasOptionsMenu(true);
    }

    LinearLayout layoutHeading = (LinearLayout) view.findViewById(R.id.stop_display_heading);
    int darkColor = Color.parseColor("#0099dd");
    GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
        new int[] {darkColor, Util.darken(darkColor, ROUTE_LIST_GRADIENT_FACTOR)});
    //noinspection deprecation
    layoutHeading.setBackgroundDrawable(gradientDrawable);

    TextView stopNameTextView = (TextView) view.findViewById(R.id.tv_stop_name);
    stopNameTextView.setText(stop.getName());
    stopNameTextView.setTypeface(FontLoader.getTypeface(this.activity, FontLoader.ARMATA));

    progressBar = (RelativeLayout) view.findViewById(R.id.progress_stopdisplay_loading);

    predictionListView = (ListView) view.findViewById(R.id.lv_predictions);

    List<Prediction> predictions = DataCache.getPredictions(this.activity, this.stop);
    if (!predictions.isEmpty()) {
      setPredictions(predictions);
    }
    getPredictionInfo(this.stop);

    return view;
  }

  private void getPredictionInfo(final Stop stop) {
    new AsyncTask<Stop, Void, List<Prediction>>() {
      @Override
      protected List<Prediction> doInBackground(Stop... stops) {
        try {
          return PaacApi.getInstance().getPredictions(Lists.newArrayList(stops));
        } catch (IOException e) {
          return Lists.newArrayList();
        }
      }

      @Override
      protected void onPostExecute(List<Prediction> predictions) {
        super.onPostExecute(predictions);
        setPredictions(predictions);
      }
    }.execute(stop);
  }

  private void setPredictions(List<Prediction> predictions) {
    PredictionArrayAdapter predictionArrayAdapter = new PredictionArrayAdapter(this.activity, predictions);
    DataCache.cachePredictions(this.activity, this.stop, predictions);
    predictionListView.setAdapter(predictionArrayAdapter);
    predictionListView.setVisibility(View.VISIBLE);
    progressBar.setVisibility(View.GONE);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        this.activity.onBackPressed();
        return true;
    }
    return true;
  }
}
