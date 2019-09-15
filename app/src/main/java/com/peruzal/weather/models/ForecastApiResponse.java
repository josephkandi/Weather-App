package com.peruzal.weather.models;

import com.squareup.moshi.Json;

public class ForecastApiResponse {
    @Json(name = "currently")
    public TodayForecast todayForecast;
    @Json(name = "daily")
    public WeeklyForecast weeklyForecast;
    public double latitude;
    public double longitude;
    public int offset;
    public String timezone;


}
