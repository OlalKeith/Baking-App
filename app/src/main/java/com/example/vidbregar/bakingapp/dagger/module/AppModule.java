package com.example.vidbregar.bakingapp.dagger.module;


import android.app.Application;
import android.content.Context;

import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    @Singleton
    @Provides
    Context provideApplicationContext(Application application) {
        return application;
    }

    @Singleton
    @Provides
    Gson provideGson() {
        return new Gson();
    }
}