package me.danco.sunshine;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * A placeholder fragment containing a simple view.
 */
public class ForecastFragment extends Fragment {
    @SuppressWarnings("unused")
    private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();

    private ArrayAdapter<String> forecastAdapter;

    public ForecastFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_forecast, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                new FetchWeatherTask().execute("98117");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String[] forecastArray = {
                "Today - Sunny - 88/63",
                "Today - Sunny - 88/63",
                "Today - Sunny - 88/63",
                "Today - Sunny - 88/63",
                "Today - Sunny - 88/63",
                "Today - Sunny - 88/63",
                "Today - Sunny - 88/63",
                "Today - Sunny - 88/63",
                "Today - Sunny - 88/63",
                "Today - Sunny - 88/63",
                "Today - Sunny - 88/63",
                "Today - Sunny - 88/63",
        };

        ArrayList<String> forecasts = new ArrayList<>(Arrays.asList(forecastArray));
        forecastAdapter = new ArrayAdapter<>(getActivity(), R.layout.list_item_forecast, R.id.list_item_forecast_textview, forecasts);

        View rootView = inflater.inflate(R.layout.fragment_forecast, container, false);

        ListView forecastList = (ListView)rootView.findViewById(R.id.listview_forecast);
        forecastList.setAdapter(forecastAdapter);

        return rootView;
    }

    private class FetchWeatherTask extends AsyncTask<String, Void, ArrayList<String>> {
        private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();

        @Override
        protected ArrayList<String> doInBackground(String... args) {
            ArrayList<String> forecasts = new ArrayList<>();

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String forecastJsonStr;

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are available at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                Uri.Builder builder = new Uri.Builder();
                builder.scheme("http")
                        .authority("api.openweathermap.org")
                        .appendPath("data")
                        .appendPath("2.5")
                        .appendPath("forecast")
                        .appendPath("daily")
                        .appendQueryParameter("q", args.length > 0 ? args[0] : "94043")
                        .appendQueryParameter("mode", "json")
                        .appendQueryParameter("units", "metric")
                        .appendQueryParameter("cnt", "7");

                URL url = new URL(builder.build().toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder buffer = new StringBuilder();
                if (inputStream != null) {
                    reader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;

                    while ((line = reader.readLine()) != null) {
                        // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                        // But it does make debugging a *lot* easier if you print out the completed
                        // buffer for debugging.
                        buffer.append(line).append("\n");
                    }
                }

                forecastJsonStr = buffer.toString();

                try {
                    JSONObject forecastData = new JSONObject(forecastJsonStr);
                    JSONArray forecastList = forecastData.getJSONArray("list");


                    SimpleDateFormat shortDateFormat = new SimpleDateFormat("EEE, MMM dd", Locale.US);

                    Calendar c = Calendar.getInstance();
                    c.setTime(new Date());

                    for (int i = 0; i < forecastList.length(); i++) {
                        StringBuilder sb = new StringBuilder();
                        JSONObject forecastDay = forecastList.getJSONObject(i);

                        sb.append(shortDateFormat.format(c.getTime()));
                        sb.append(" - ");
                        sb.append(forecastDay.getJSONArray("weather").getJSONObject(0).getString("main"));
                        sb.append(" - ");
                        sb.append(Math.round(forecastDay.getJSONObject("temp").getDouble("max")));
                        sb.append("/");
                        sb.append(Math.round(forecastDay.getJSONObject("temp").getDouble("min")));

                        c.add(Calendar.DATE, 1);

                        forecasts.add(sb.toString());
                    }
                } catch (JSONException ex) {
                    Log.e(LOG_TAG, "Error ", ex);
                }
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }

                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            return forecasts;
        }

        @Override
        protected void onPostExecute(ArrayList<String> forecast) {
            forecastAdapter.clear();
            forecastAdapter.addAll(forecast);
        }
    }

}


/*


 */