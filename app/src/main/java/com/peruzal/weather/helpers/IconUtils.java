package com.peruzal.weather.helpers;


import com.peruzal.weather.R;

public class IconUtils {
    public static int getForecastIcon(String icon) {
        switch (icon){
            case "partly-cloudy-day":
                return R.raw.ic_partly_cloudy_day;
            case "clear-night":
                return R.raw.ic_clear_night;
            case "sleet":
                return R.raw.ic_fog;
            case "wind":
                return R.raw.ic_fog;
            case "snow":
                return R.raw.ic_snow;
            case "rain":
                return R.raw.ic_partly_cloudy_day;
            case "hail":
                return R.raw.ic_fog;
            case "fog":
                return R.raw.ic_fog;
            case "tornado":
                return R.raw.ic_snow;
            case "clear-day":
                return R.raw.sunny;
            case "cloudy":
                return R.raw.ic_snow;
            case "thunderstorm":
                return R.raw.ic_snow;
            case "partly-cloudy-night":
                return R.raw.ic_partly_cloudy_day;
            default:
                return R.raw.sunny;
        }
    }
}
