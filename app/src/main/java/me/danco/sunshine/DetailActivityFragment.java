package me.danco.sunshine;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import me.danco.sunshine.helpers.Weather;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {
    private final String LOG_TAG = DetailActivityFragment.class.getSimpleName();
    private View rootView;

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            Activity activity = getActivity();
            JSONObject forecast = new JSONObject(activity.getIntent().getStringExtra("Forecast"));
            int position = activity.getIntent().getIntExtra("Position", 0);

            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.DATE, position);
            SimpleDateFormat dateFormat = new SimpleDateFormat("LLLL d", Locale.US);

            rootView = view;

            populateText(
                    R.id.detail_day,
                    Weather.FriendlyDay(position)
            );

            populateText(
                    R.id.detail_date,
                    dateFormat.format(c.getTime())
            );

            populateText(
                    R.id.detail_high,
                    String.format(getString(R.string.format_temperature), Math.round(forecast.getJSONObject("temp").getDouble("max")))
            );

            populateText(
                    R.id.detail_low,
                    String.format(getString(R.string.format_temperature), Math.round(forecast.getJSONObject("temp").getDouble("min")))
            );

            populateText(
                    R.id.detail_humidity,
                    String.format(getString(R.string.format_humidity), Math.round(forecast.getDouble("humidity")))
            );

            populateText(
                    R.id.detail_pressure,
                    String.format(getString(R.string.format_pressure_metric), Math.round(forecast.getDouble("pressure")))
            );

            int directionIndex = (int) Math.floor((forecast.getDouble("deg") + 22.5) / 45.0) % 8;
            int windSpeed = (int) Math.round(forecast.getDouble("speed") * 3600 / 1000);
            String windDirection = new String[]{"N","NE","E","SE","S","SW","W","NW"}[directionIndex];
            populateText(
                    R.id.detail_wind,
                    String.format(getString(R.string.format_wind_metric), windSpeed, windDirection)
            );

            populateText(
                    R.id.detail_weather_label,
                    forecast.getJSONArray("weather").getJSONObject(0).getString("main")
            );

            ImageView image = (ImageView) view.findViewById(R.id.detail_weather_image);
            image.setImageResource(getResources().getIdentifier(
                    "art_" + Weather.GetDrawableSuffix(forecast.getJSONArray("weather").getJSONObject(0).getInt("id")),
                    "drawable",
                    getActivity().getPackageName()));

            Log.v(LOG_TAG, forecast.toString());
        } catch (JSONException ex) {
            Log.e(LOG_TAG, ex.toString());
        }

    }

    private void populateText(int id, String text) {
        ((TextView) rootView.findViewById(id)).setText(text);
    }
}
