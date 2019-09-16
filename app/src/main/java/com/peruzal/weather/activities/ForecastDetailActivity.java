package com.peruzal.weather.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.peruzal.weather.Constants;
import com.peruzal.weather.R;
import com.peruzal.weather.models.Forecast;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class ForecastDetailActivity extends AppCompatActivity {
    public TextView currentDate;
    public TextView currentTemperature;
    public TextView summary;
    public TextView unit;
    private static final DecimalFormat decimalFormat = new DecimalFormat("#");
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E dd MMM yyyy", Locale.getDefault());
    private static SimpleDateFormat weekDayFormat = new SimpleDateFormat("E", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast_detail);

        this.currentDate = findViewById(R.id.currentDate);
        this.currentTemperature = findViewById(R.id.currentTemperature);
        this.unit = findViewById(R.id.unit);
        this.summary = findViewById(R.id.summary);
        if (getIntent() != null && getIntent().hasExtra(Constants.FORECAST_ITEM_KEY)) {
            String forecastString = getIntent().getStringExtra(Constants.FORECAST_ITEM_KEY);
            JsonAdapter jsonAdapter = (new Moshi.Builder()).build().adapter(Forecast.class);
            try {
                UpdateUI((Forecast) Objects.requireNonNull(jsonAdapter.fromJson(forecastString)));
            } catch (IOException str) {
                str.printStackTrace();
            }
        }
    }

    private void UpdateUI(Forecast forecast) {
        currentTemperature.setText(getString(R.string.temperature, decimalFormat.format(forecast.temperatureHigh)));
        summary.setText(forecast.summary);
        currentDate.setText(simpleDateFormat.format(new Date(forecast.time * 1000L)));
    }
}
