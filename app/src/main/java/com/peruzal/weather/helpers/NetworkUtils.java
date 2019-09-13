package com.peruzal.weather.helpers;


import com.peruzal.weather.Constants;

import okhttp3.HttpUrl;

public class NetworkUtils {
    private static final String BASE_API_URL = "api.darksky.net";
    private static final String EXCLUDE_QUERY_PARAM = "exclude";
    private static final String EXCLUDE_QUERY_VALUE = "[minutely,flags,hourly]";
    private static final String FORECAST_PATH_SEGMENT = "forecast";
    private static final String UNITS_QUERY_PARAM = "units";
    private static final String UNITS_QUERY_VALUE = "si";
    private static final String URL_SCHEME = "https";

    public static HttpUrl buildWeatherForecastUrl(double latitude, double longitude) {
        HttpUrl.Builder builder = (new HttpUrl.Builder())
                .scheme(URL_SCHEME)
                .host(BASE_API_URL)
                .addPathSegment(FORECAST_PATH_SEGMENT)
                .addPathSegment(Constants.API_KEY);

        builder.addEncodedPathSegment(String.format("%s,%s", new Object[] { Double.valueOf(latitude), Double.valueOf(longitude) }))
                .addQueryParameter(EXCLUDE_QUERY_PARAM, EXCLUDE_QUERY_VALUE)
                .addQueryParameter(UNITS_QUERY_PARAM, UNITS_QUERY_VALUE);
        return builder.build();
    }
}
