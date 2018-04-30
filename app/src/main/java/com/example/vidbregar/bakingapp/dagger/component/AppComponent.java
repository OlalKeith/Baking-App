package com.example.vidbregar.bakingapp.dagger.component;


import android.app.Application;

import com.example.vidbregar.bakingapp.RecipeApplication;
import com.example.vidbregar.bakingapp.dagger.builder.ServiceBuilder;
import com.example.vidbregar.bakingapp.dagger.module.AppModule;
import com.example.vidbregar.bakingapp.dagger.builder.ActivityBuilder;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {
        AndroidSupportInjectionModule.class,
        AppModule.class,
        ActivityBuilder.class,
        ServiceBuilder.class})
public interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }

    void inject(RecipeApplication app);
}