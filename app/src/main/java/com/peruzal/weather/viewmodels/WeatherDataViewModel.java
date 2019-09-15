package com.peruzal.weather.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.peruzal.weather.models.WeatherData;
import com.peruzal.weather.repositories.WeatherDataRepository;

public class WeatherDataViewModel extends AndroidViewModel {
    private WeatherDataRepository weatherDataRepository;

    public WeatherDataViewModel(@NonNull Application application) {
        super(application);
        weatherDataRepository = new WeatherDataRepository(application);
    }

    public LiveData<WeatherData> fetchWeatherForecast(double latitude, double longitude, String units){
        return weatherDataRepository.fetchWeatherForecast(latitude, longitude, units);
    }
}
