package com.example.vidbregar.bakingapp.dagger.module;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.example.vidbregar.bakingapp.room.AppDatabase;
import com.example.vidbregar.bakingapp.room.RecipeEntity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class AppModule {

    @Singleton
    @Provides
    HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor =
                new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        return httpLoggingInterceptor;
    }

    @Singleton
    @Provides
    OkHttpClient provideOkHttpClient(HttpLoggingInterceptor httpLoggingInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .build();
    }

    @Singleton
    @Provides
    Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl("https://d17h27t6h515a5.cloudfront.net")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    @Singleton
    @Provides
    Gson provideGson() {
        return new GsonBuilder().create();
    }

    @Singleton
    @Provides
    AppDatabase provideAppDatabase(Application application) {
        AppDatabase appDatabase = Room.databaseBuilder(application, AppDatabase.class, "recipe-database")
                // TODO: 29/04/2018 Only for debugging -> don't allow querying on main thread
                .allowMainThreadQueries()
                .build();
        if (appDatabase.recipeDao().getRecipe() == null) {
            RecipeEntity recipeEntity = new RecipeEntity();
            recipeEntity.setId(1);
            recipeEntity.setRecipeJson("");
            appDatabase.recipeDao().insertRecipe(recipeEntity);
        }
        return appDatabase;
    }
}