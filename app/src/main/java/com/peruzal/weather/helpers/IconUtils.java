package com.peruzal.weather.helpers;


import com.peruzal.weather.R;

public class IconUtils {
    public static int getForecastIcon(String icon) {
        switch (icon){
            case "partly-cloudy-day":
                return R.drawable.ic_partly_cloudy_day;
            case "clear-night":
                return R.drawable.ic_clear_night;
            case "sleet":
                return R.drawable.ic_sleet;
            case "wind":
                return R.drawable.ic_wind;
            case "snow":
                return R.drawable.ic_snow;
            case "rain":
                return R.drawable.ic_rain;
            case "hail":
                return R.drawable.ic_hail;
            case "fog":
                return R.drawable.ic_fog;
            case "tornado":
                return R.drawable.ic_tornado;
            case "clear-day":
                return R.drawable.ic_clear_day;
            case "cloudy":
                return R.drawable.ic_cloudy;
            case "thunderstorm":
                return R.drawable.ic_thunderstorm;
            case "partly-cloudy-night":
                return R.drawable.ic_partly_cloudy_night;
            default:
                return R.drawable.ic_clear_day;
        }
    }
}
