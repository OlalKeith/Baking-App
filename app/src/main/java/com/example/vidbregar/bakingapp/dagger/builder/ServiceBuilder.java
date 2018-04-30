package com.example.vidbregar.bakingapp.dagger.builder;

import com.example.vidbregar.bakingapp.widget.BakingWidgetService;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ServiceBuilder {

    @ContributesAndroidInjector
    abstract BakingWidgetService bindBakingWidgetService();
}
