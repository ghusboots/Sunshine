package me.danco.sunshine.helpers;

import android.content.Context;
import android.content.ContextWrapper;
import android.preference.PreferenceManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import me.danco.sunshine.R;

public class WeatherHelper extends ContextWrapper {
    public WeatherHelper(Context context) {
        super(context);
    }

    public int convertTemperature(double temp) {
        if (isMetric()) {
            return (int) Math.round(temp);
        } else {
            return (int) Math.round(temp * 9 / 5) + 32;
        }
    }

    public int convertSpeed(double speed) {
        speed *= 3.6; // convert m/s to km/h
        if (isMetric()) {
            return (int) Math.round(speed);
        } else {
            return (int) Math.round(speed * .6214);
        }
    }

    public String GetDrawableSuffix(int weather) {
        if (weather == 500 || weather == 520) {
            return "light_rain";
        } else if (weather > 500 && weather < 600) {
            return "rain";
        } else if (weather >= 200 && weather < 300) {
            return "storm";
        } else if (weather >= 300 && weather < 400) {
            return "light_rain";
        } else if (weather >= 600 && weather < 700) {
            return "snow";
        } else if (weather >= 700 && weather < 800) {
            return "fog";
        } else if (weather == 800) {
            return "clear";
        } else if (weather >= 801 || weather < 804) {
            return "light_clouds";
        } else {
            return "clouds";
        }
    }

    public String FriendlyDay(int offset) {
        return FriendlyDay(offset, false);
    }

    public String FriendlyDay(int offset, boolean shortDate) {
        if (offset == 0) {
            return "Today";
        } else if (offset == 1) {
            return "Tomorrow";
        } else {
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.DATE, offset);
            SimpleDateFormat dayFormat = new SimpleDateFormat(shortDate ? "EEE" : "EEEE", Locale.US);

            return dayFormat.format(c.getTime());
        }
    }

    public boolean isMetric() {
        return PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString(getString(R.string.preference_units_key), getString(R.string.preference_units_default)).equals(getString(R.string.preference_units_default));
    }
}
