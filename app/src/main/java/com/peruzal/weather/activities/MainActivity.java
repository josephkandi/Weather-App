package com.peruzal.weather.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.peruzal.weather.Constants;
import com.peruzal.weather.R;
import com.peruzal.weather.adapters.ForecastAdapter;
import com.peruzal.weather.models.Forecast;
import com.peruzal.weather.models.ForecastApiResponse;
import com.peruzal.weather.services.WeatherService;
import com.peruzal.wther.services.Response;
import com.squareup.moshi.Moshi;

public class MainActivity extends AppCompatActivity implements ForecastAdapter.OnItemClickListener, Response.Listener {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private static final String TAG = MainActivity.class.getSimpleName();
    private FusedLocationProviderClient fusedLocationProviderClient;
    ProgressBar loadingindicator;
    RecyclerView recyclerView;
    ForecastAdapter forecastAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_second);
        this.recyclerView = findViewById(R.id.recyclerView);
        this.loadingindicator = findViewById(R.id.loadingIndicator);
        forecastAdapter = new ForecastAdapter(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        this.recyclerView.addItemDecoration(dividerItemDecoration);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.recyclerView.setAdapter(forecastAdapter);
        setLocationPermissions();
    }

    private void setLocationPermissions() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (shouldShowRequestPermissionRationale(Manifest.permission_group.LOCATION)) {
                    Toast.makeText(this, getString(R.string.location_permission_message), Toast.LENGTH_LONG).show();
                }
                requestPermissions(new String[] { Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION }, LOCATION_PERMISSION_REQUEST_CODE);
            }
        }
        else {
            setUpUserLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == LOCATION_PERMISSION_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            setUpUserLocation();
        } else {
            Toast.makeText(this, getString(R.string.location_permission_message), Toast.LENGTH_SHORT).show();
        }
    }

    private void setUpUserLocation() {
        this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        this.fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(this, location -> WeatherService.fetchForecasts(location.getLatitude(), location.getLongitude(),MainActivity.this))
                .addOnFailureListener(this, exception -> Toast.makeText(MainActivity.this, MainActivity.this.getString(R.string.user_location_not_found), Toast.LENGTH_SHORT).show());
    }

    void hideIndicator() { this.loadingindicator.setVisibility(View.INVISIBLE); }
    void hideRecyclerView() { this.recyclerView.setVisibility(View.INVISIBLE); }

    void showRecyclerView() { this.recyclerView.setVisibility(View.VISIBLE); }

    private void updateUI(ForecastApiResponse response) {
        hideIndicator();
        forecastAdapter.setData(response.currently, response.daily.data);
        showRecyclerView();
    }


    @Override
    public void OnItemClick(int position, View view) {
        Intent intent = new Intent(this, ForecastDetailActivity.class);
        Forecast forecast = forecastAdapter.getForecastItem(position);
        intent.putExtra(Constants.FORECAST_ITEM_KEY, (new Moshi.Builder()).build().adapter(Forecast.class).toJson(forecast));
        startActivity(intent);
    }

    @Override
    public void onResponse(ForecastApiResponse response) {
        runOnUiThread(() -> updateUI(response));
    }

    @Override
    public void onError(Exception error) {
        hideIndicator();
        hideRecyclerView();
        Log.d(TAG, error.getMessage());
        runOnUiThread(() -> Toast.makeText(this, getString(R.string.error_fetching_data),Toast.LENGTH_LONG).show());

    }
}
