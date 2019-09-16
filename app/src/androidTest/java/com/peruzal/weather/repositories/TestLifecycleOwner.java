package com.peruzal.weather.repositories;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
public class TestLifecycleOwner implements LifecycleOwner {
    private LifecycleRegistry mLifecycle;
    public TestLifecycleOwner() {
        mLifecycle = new LifecycleRegistry(this);
    }
    @Override
    public Lifecycle getLifecycle() {
        return mLifecycle;
    }
    public void handleEvent(Lifecycle.Event event) {
        mLifecycle.handleLifecycleEvent(event);
    }
}