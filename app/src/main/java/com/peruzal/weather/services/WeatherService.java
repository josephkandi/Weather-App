package com.peruzal.weather.services;


import com.peruzal.weather.helpers.NetworkUtils;
import com.peruzal.weather.models.ForecastApiResponse;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class WeatherService {
    static OkHttpClient client = new OkHttpClient();
    static Moshi moshi = (new Moshi.Builder()).build();
    static JsonAdapter<ForecastApiResponse> jsonAdapter = moshi.adapter(ForecastApiResponse.class);

    public static void fetchForecasts(double latidude, double longitude, final com.peruzal.wther.services.Response.Listener callback) {
        HttpUrl httpUrl = NetworkUtils.buildWeatherForecastUrl(latidude, longitude);
        Request request = (new Request.Builder()).url(httpUrl).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) throws IOException {
                callback.onResponse(jsonAdapter.fromJson(response.body().string()));
            }

            public void onFailure(Call call, IOException exception) {
                callback.onError(exception);
            }
        });
    }
}
