package com.peruzal.weather.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

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
    Button retryButton;
    ImageView errorImageView;
    ForecastAdapter forecastAdapter;
    ProgressBar loadingindicator;
    LocationViewModel locationViewModel;
    MainActivityViewModel mainActivityViewModel;
    RecyclerView recyclerView;
    TextView errorTextView;
    View errorLayout;
    WeatherDataViewModel weatherDataViewModel;
    // TODO use preference screen
    String UNITS = "si";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupViews();
        setupAdapter();
        setupViewModels();

        if(RootUtils.isRooted()){
            showRootedDeviceError();
            return;
        }

        if(!mainActivityViewModel.isFirstRun()){
           firstTimeSetup();
       } else {
           getWeatherData();
       }

       mainActivityViewModel.getWeatherData().observe(this, this::updateUI);
    }

    private void showRootedDeviceError() {
        errorImageView.setImageResource(R.drawable.ic_device_rooted);
        errorTextView.setText(getText(R.string.device_rooted));
        retryButton.setVisibility(View.INVISIBLE);
        showErrorLayout();
    }

    private void setupViewModels() {
        weatherDataViewModel = ViewModelProviders.of(this).get(WeatherDataViewModel.class);
        mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        locationViewModel = ViewModelProviders.of(this).get(LocationViewModel.class);
    }


    private void setupAdapter() {
        forecastAdapter = new ForecastAdapter(this, this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        this.recyclerView.addItemDecoration(dividerItemDecoration);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.recyclerView.setAdapter(forecastAdapter);
    }

    private void getWeatherData() {
        showIndicator();
        hideRecyclerView();
        hideErrorLayout();
        setupWeatherDataObserver(mainActivityViewModel.getLatitude(), mainActivityViewModel.getLongitude());
    }

    private void removeObserver(){
        weatherDataViewModel
                .fetchWeatherForecast(0,0, null)
                .removeObservers(this);
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
        recyclerView = findViewById(R.id.recyclerView);
        loadingindicator = findViewById(R.id.loadingIndicator);
        errorLayout = findViewById(R.id.errorLayout);
        errorTextView = findViewById(R.id.errorTextView);
        errorImageView = findViewById(R.id.errorImageView);
        retryButton = findViewById(R.id.retryButton);

        retryButton.setOnClickListener(view -> {
            removeObserver();

            getWeatherData();
        });
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

    void hideIndicator() { loadingindicator.setVisibility(View.INVISIBLE); }
    void hideRecyclerView() { recyclerView.setVisibility(View.INVISIBLE); }
    void hideErrorLayout() { errorLayout.setVisibility(View.INVISIBLE); }
    void showIndicator() { loadingindicator.setVisibility(View.VISIBLE); }
    void showRecyclerView() { recyclerView.setVisibility(View.VISIBLE); }
    void showErrorLayout() { errorLayout.setVisibility(View.VISIBLE); }


    private void updateUI(WeatherData weatherData) {
        hideIndicator();
        if(weatherData.error != null){
            hideRecyclerView();
            errorLayout.setVisibility(View.VISIBLE);
            switch (weatherData.error){
                case INTERRUPTED:
                case GENERAL_FAILURE:
                case DATA_FAILURE:
                    errorImageView.setImageResource(R.drawable.ic_no_data);
                    errorTextView.setText(getText(R.string.error_no_data));
                    break;
                case NETWORK_UNAVAILABLE:
                    errorImageView.setImageResource(R.drawable.ic_network_error);
                    errorTextView.setText(getString(R.string.error_fetching_data));
                    break;
                case LOCATION_PERMISSED_DENIED:
                    errorImageView.setImageResource(R.drawable.ic_permission_denied);
                    errorTextView.setText(getString(R.string.error_permission_denied));
                    break;
                case LOCATION_UNAVAILABLE:
                    errorImageView.setImageResource(R.drawable.ic_network_error);
                    errorTextView.setText(getString(R.string.error_location_unavailable));
                    break;
                default:
                    errorImageView.setImageResource(R.drawable.ic_no_data);
                    errorTextView.setText(getString(R.string.error_fetching_data));
            }
        } else if((weatherData.currentForecast != null) || (weatherData.weeklyForecast != null)){
            hideErrorLayout();
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
