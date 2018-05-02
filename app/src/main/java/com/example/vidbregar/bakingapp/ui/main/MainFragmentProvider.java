package com.example.vidbregar.bakingapp.ui.main;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainFragmentProvider {

    @ContributesAndroidInjector
    abstract MainFragment bindMainFragment();
}
