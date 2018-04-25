package com.example.vidbregar.bakingapp.ui.main;

import dagger.Module;
import dagger.Provides;

@Module
public class MainFragmentModule {

    @Provides
    public String provideHelloMessage() {
        return "Hello from Dagger 2";
    }
}
