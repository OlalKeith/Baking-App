package com.example.vidbregar.bakingapp.dagger.builder;

import com.example.vidbregar.bakingapp.ui.main.MainActivity;
import com.example.vidbregar.bakingapp.ui.main.MainFragmentProvider;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = MainFragmentProvider.class)
    abstract MainActivity bindMainActivity();
}