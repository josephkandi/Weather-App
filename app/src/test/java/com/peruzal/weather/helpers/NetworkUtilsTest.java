package com.peruzal.weather.helpers;

import org.junit.Test;

import okhttp3.HttpUrl;

public class NetworkUtilsTest {

    @Test
    public void buildWeatherForecastUrl() {
        double latitude = -34.0833;
        double longitude = 18.85;

        HttpUrl url = NetworkUtils.buildWeatherForecastUrl(latitude, longitude, "si");
        assert url.isHttps();
        assert url.encodedPathSegments().contains(latitude+","+longitude);
        assert url.host().equals("api.darksky.net");
        assert  url.pathSegments().size() == 3;
        assert url.queryParameterNames().contains("units");
    }
}