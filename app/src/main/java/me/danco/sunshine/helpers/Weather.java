package me.danco.sunshine.helpers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Weather {
    public static String GetDrawableSuffix(int weather) {
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

    public static String FriendlyDay(int offset) {
        if (offset == 0) {
            return "Today";
        } else if (offset == 1) {
            return "Tomorrow";
        } else {
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.DATE, offset);
            SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);

            return dayFormat.format(c.getTime());
        }
    }
}
