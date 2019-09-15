package com.peruzal.weather.viewmodels;

import android.app.Application;
import android.location.Location;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class LocationViewModel extends AndroidViewModel {
    private MutableLiveData<Location> lastKnownLocation;
    private FusedLocationProviderClient client;

    public LocationViewModel(@NonNull Application application) {
        super(application);
        client = LocationServices.getFusedLocationProviderClient(application);
        lastKnownLocation = new MutableLiveData<>();
    }

    public LiveData<Location> getLastKnownLocation() {
        client.getLastLocation().addOnCompleteListener(task -> {
            Location locationResult;

            if ((task.isSuccessful() & (locationResult = task.getResult()) != null)) {
                lastKnownLocation.setValue(locationResult);
            } else {
                lastKnownLocation.setValue(null);
            }
        });

        return lastKnownLocation;
    }
}
