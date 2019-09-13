package com.peruzal.wther.services;

import com.peruzal.weather.models.ForecastApiResponse;

public class Response {
    public  interface Listener {
        void onError(Exception error);
        void onResponse(ForecastApiResponse response);
    }
}
