package com.peruzal.weather.repositories;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.peruzal.weather.helpers.NetworkUtils;
import com.peruzal.weather.models.ForecastApiResponse;
import com.peruzal.weather.models.TodayForecast;
import com.peruzal.weather.models.WeatherData;
import com.peruzal.weather.models.WeatherError;
import com.peruzal.weather.models.WeeklyForecast;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.peruzal.weather.models.WeatherError.GENERAL_FAILURE;
import static com.peruzal.weather.models.WeatherError.NETWORK_UNAVAILABLE;

public class WeatherDataRepository {
    private MutableLiveData<WeatherData> weatherDataMutableLiveData = new MutableLiveData<>();
    private WeatherData weatherData = new WeatherData();
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    OkHttpClient client = new OkHttpClient();
    Moshi moshi = (new Moshi.Builder()).build();
    private Context context;
    JsonAdapter<ForecastApiResponse> jsonAdapter = moshi.adapter(ForecastApiResponse.class);

    public WeatherDataRepository(Application context) {
        this.context = context;
    }

    public LiveData<WeatherData> fetchWeatherForecast(double latitude, double longitude){

        if(!isConnected()){
            setWeatherData(NETWORK_UNAVAILABLE, null, null);
            return weatherDataMutableLiveData;
        }

        Request request = new Request.Builder()
                .url(NetworkUtils.buildWeatherForecastUrl(latitude, longitude))
                .build();

        try {
            Response response = executorService.submit(client.newCall(request)::execute).get();
            processResponse(response);
        } catch (InterruptedException | ExecutionException | IOException e) {
            setWeatherData(GENERAL_FAILURE, null, null);
        }
        return weatherDataMutableLiveData;
    }

    private void processResponse(Response response) throws IOException {
        ForecastApiResponse apiResponse;
        if (response.isSuccessful() && (apiResponse = jsonAdapter.fromJson(response.body().source())) != null ) {
            if(apiResponse.todayForecast != null || apiResponse.weeklyForecast != null){
                setWeatherData(null, apiResponse.todayForecast, apiResponse.weeklyForecast);
            } else {
                setWeatherData(GENERAL_FAILURE, null, null);
            }
        } else {
            setWeatherData(WeatherError.DATA_FAILURE, null, null);
        }
    }

    private void setWeatherData(WeatherError errorState, TodayForecast todayForecast, WeeklyForecast weeklyForecast) {
        weatherData.error = errorState;
        weatherData.weeklyForecast = weeklyForecast;
        weatherData.currentForecast = todayForecast;
        weatherDataMutableLiveData.postValue(weatherData);
    }

    private boolean isConnected(){
        ConnectivityManager connectivityManager =  (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            if (connectivityManager.getActiveNetworkInfo() != null) {
                return connectivityManager.getActiveNetworkInfo().isConnected();
            }
        }
        return false;
    }
}
