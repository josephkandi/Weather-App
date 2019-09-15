package com.peruzal.weather.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.peruzal.weather.Constants;
import com.peruzal.weather.R;
import com.peruzal.weather.adapters.ForecastAdapter;
import com.peruzal.weather.helpers.RootUtils;
import com.peruzal.weather.models.Forecast;
import com.peruzal.weather.models.WeatherData;
import com.peruzal.weather.models.WeatherError;
import com.peruzal.weather.viewmodels.LocationViewModel;
import com.peruzal.weather.viewmodels.MainActivityViewModel;
import com.peruzal.weather.viewmodels.WeatherDataViewModel;
import com.squareup.moshi.Moshi;

public class MainActivity extends AppCompatActivity implements ForecastAdapter.OnItemClickListener {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private static final String TAG = MainActivity.class.getSimpleName();
    ProgressBar loadingindicator;
    RecyclerView recyclerView;
    ForecastAdapter forecastAdapter;
    WeatherDataViewModel weatherDataViewModel;
    MainActivityViewModel mainActivityViewModel;
    LocationViewModel locationViewModel;

    // TODO use preference screen
    String UNITS = "si";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupViews();
        setupAdapter();
        setupViewModels();

        if(!mainActivityViewModel.isFirstRun()){
           firstTimeSetup();
       } else {
           getWeatherData();
       }

       mainActivityViewModel.getWeatherData().observe(this, this::updateUI);
        // TODO - Show error and not continue
        Log.d(TAG, "Device is rooted " + RootUtils.isRooted());
    }

    private void setupViewModels() {
        weatherDataViewModel = ViewModelProviders.of(this).get(WeatherDataViewModel.class);
        mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        locationViewModel = ViewModelProviders.of(this).get(LocationViewModel.class);
    }

    private void setupAdapter() {
        forecastAdapter = new ForecastAdapter(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        this.recyclerView.addItemDecoration(dividerItemDecoration);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.recyclerView.setAdapter(forecastAdapter);
    }

    private void getWeatherData() {
        setupWeatherDataObserver(mainActivityViewModel.getLatitude(), mainActivityViewModel.getLongitude());
    }


    private void firstTimeSetup() {
        requestPermission();
    }

    private void requestPermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                    new String[] {
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    LOCATION_PERMISSION_REQUEST_CODE
            );
        }
    }

    private void setupViews() {
        this.recyclerView = findViewById(R.id.recyclerView);
        this.loadingindicator = findViewById(R.id.loadingIndicator);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == LOCATION_PERMISSION_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            locationViewModel.getLastKnownLocation().observe(this, lastKnownLocation -> {
                if(lastKnownLocation != null) {
                    mainActivityViewModel.setFirstRun(true);
                    mainActivityViewModel.setLatitude(lastKnownLocation.getLatitude());
                    mainActivityViewModel.setLongitude(lastKnownLocation.getLongitude());

                    setupWeatherDataObserver(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                } else {
                    setWeatherErrorState(WeatherError.LOCATION_UNAVAILABLE);
                }
            });
        } else {
            setWeatherErrorState(WeatherError.LOCATION_PERMISSED_DENIED);
        }
    }

    private void setupWeatherDataObserver(double latitude, double longitude) {
        weatherDataViewModel.fetchWeatherForecast(latitude, longitude, UNITS)
                .observe(this, weatherData -> mainActivityViewModel.setWeatherData(weatherData));
    }

    private void setWeatherErrorState(WeatherError error) {
        WeatherData weatherData;
        if(mainActivityViewModel.getWeatherData().getValue() != null){
            weatherData = mainActivityViewModel.getWeatherData().getValue();
        }else {
            weatherData = new WeatherData();
        }
        weatherData.error  = error;
        mainActivityViewModel.setWeatherData(weatherData);
    }

    void hideIndicator() { this.loadingindicator.setVisibility(View.INVISIBLE); }
    void showIndicator() { loadingindicator.setVisibility(View.VISIBLE); }

    void showRecyclerView() { this.recyclerView.setVisibility(View.VISIBLE); }

    private void updateUI(WeatherData weatherData) {
        hideIndicator();
        if(weatherData.error != null){
            switch (weatherData.error){
                case INTERRUPTED:
                    Log.d(TAG, "****interrupted********");
                    break;
                case DATA_FAILURE:
                    Log.d(TAG, "******data*******");
                    break;
                case NETWORK_UNAVAILABLE:
                    Log.d(TAG, "******unavailable******");
                    break;
                case GENERAL_FAILURE:
                    Log.d(TAG, "******general failure******");
                    break;
                case LOCATION_PERMISSED_DENIED:
                    Log.d(TAG, "******permission denied******");
                    break;
                case LOCATION_UNAVAILABLE:
                    Log.d(TAG, "******location unavailable******");
                    break;
                default:
                    Log.d(TAG, "******unknown error******");
            }
        } else {
            forecastAdapter.setData(weatherData.currentForecast, weatherData.weeklyForecast.data);
            showRecyclerView();
        }
    }


    @Override
    public void OnItemClick(int position, View view) {
        Intent intent = new Intent(this, ForecastDetailActivity.class);
        Forecast forecast = forecastAdapter.getForecastItem(position);
        intent.putExtra(Constants.FORECAST_ITEM_KEY, (new Moshi.Builder()).build().adapter(Forecast.class).toJson(forecast));
        startActivity(intent);
    }
}
