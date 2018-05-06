package com.example.vidbregar.bakingapp;

import android.support.annotation.Nullable;
import android.support.test.espresso.IdlingResource;

import java.util.concurrent.atomic.AtomicBoolean;

public class RecipeIdlingResource implements IdlingResource {

    @Nullable
    private volatile android.support.test.espresso.IdlingResource.ResourceCallback resourceCallback;

    private AtomicBoolean isIdle = new AtomicBoolean(true);

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public boolean isIdleNow() {
        return isIdle.get();
    }

    @Override
    public void registerIdleTransitionCallback(android.support.test.espresso.IdlingResource.ResourceCallback callback) {
        resourceCallback = callback;
    }

    public void setIdleState(boolean isIdle) {
        this.isIdle.set(isIdle);
        if (isIdle && resourceCallback != null) {
            resourceCallback.onTransitionToIdle();
        }
    }
}
