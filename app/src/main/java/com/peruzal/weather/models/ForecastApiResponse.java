package com.peruzal.weather.models;

public class ForecastApiResponse {
    public TodayForecast currently;
    public WeeklyForecast daily;
    public double latitude;
    public double longitude;
    public int offset;
    public String timezone;
}
