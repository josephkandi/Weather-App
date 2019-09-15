package com.peruzal.weather.repositories;

import android.app.Application;
import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.peruzal.weather.activities.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class WeatherDataRepositoryTest {

    @Rule
    public InstantTaskExecutorRule taskExecutor = new InstantTaskExecutorRule();

    @Test
    public void fetchWeatherForecast() {
        double latitude = -34.0833;
        double longitude = 18.85;

        WeatherDataRepository weatherDataRepository = new WeatherDataRepository(mock(Application.class));
        weatherDataRepository.fetchWeatherForecast(latitude, longitude, "si").observe(mock(LifecycleOwner.class), weatherData -> {
            assert  weatherData.error == null;
        });
    }
}