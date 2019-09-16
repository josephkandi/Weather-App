package com.peruzal.weather.helpers;


import com.peruzal.weather.R;

public class IconUtils {
    public static int getForecastIcon(String icon) {
        switch (icon){
            case "partly-cloudy-day":
            case "cloudy":
                return R.raw.ic_partly_cloudy_day;
            case "clear-night":
                return R.raw.ic_clear_night;
            case "wind":
                return R.raw.ic_wind;
            case "sleet":
            case "snow":
                return R.raw.ic_snow;
            case "rain":
                return R.raw.ic_rain;
            case "hail":
                return R.raw.ic_hail;
            case "fog":
                return R.raw.ic_fog;
            case "thunderstorm":
                return R.raw.ic_thunderstorm;
            case "partly-cloudy-night":
                return R.raw.ic_partly_cloudy_night;
            default:
                return R.raw.sunny;
        }
    }
}
