package com.peruzal.weather.viewmodels;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.preference.PreferenceManager;

import com.peruzal.weather.Constants;
import com.peruzal.weather.models.WeatherData;

public class MainActivityViewModel extends AndroidViewModel {
    private SharedPreferences sharedPreferences;
    private MutableLiveData<WeatherData> weatherData;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(application);
    }

    public MutableLiveData<WeatherData> getWeatherData() {
        if(weatherData == null){
            weatherData = new MutableLiveData<>();
        }
        return weatherData;
    }

    public void setWeatherData(WeatherData weatherData) {
        this.weatherData.setValue(weatherData);
    }

    public double getLatitude() {
        return Double.longBitsToDouble(sharedPreferences.getLong(Constants.LATITUDE, 0));
    }

    public double getLongitude() {
        return Double.longBitsToDouble(sharedPreferences.getLong(Constants.LONGITUDE, 0));
    }

    public void  setLatitude(double latitude){
        sharedPreferences.edit().putLong(Constants.LATITUDE, Double.doubleToRawLongBits(latitude)).apply();
    }

    public void  setLongitude(double longitude){
        sharedPreferences.edit().putLong(Constants.LONGITUDE, Double.doubleToRawLongBits(longitude)).apply();
    }

    public boolean isFirstRun() {
        return sharedPreferences.contains(Constants.FIRST_RUN);
    }

    public void setFirstRun(boolean firstRun) {
        sharedPreferences.edit().putBoolean(Constants.FIRST_RUN, firstRun).apply();
    }
}
