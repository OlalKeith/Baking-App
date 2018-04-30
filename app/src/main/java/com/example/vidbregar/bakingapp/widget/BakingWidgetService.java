package com.example.vidbregar.bakingapp.widget;

import android.app.Application;
import android.content.Intent;
import android.widget.RemoteViewsService;

import com.example.vidbregar.bakingapp.room.AppDatabase;
import com.google.gson.Gson;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class BakingWidgetService extends RemoteViewsService {

    @Inject
    Application applicationContext;
    @Inject
    AppDatabase appDatabase;
    @Inject
    Gson gson;

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidInjection.inject(this);
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new BakingRemoteViewsFactory(applicationContext, appDatabase, gson);
    }
}


