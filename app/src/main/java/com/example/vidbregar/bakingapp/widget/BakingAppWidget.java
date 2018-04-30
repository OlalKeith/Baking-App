package com.example.vidbregar.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.vidbregar.bakingapp.R;

public class BakingAppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);
        Intent remoteAdapterIntent = new Intent(context, BakingWidgetService.class);
        remoteViews.setRemoteAdapter(R.id.ingredients_widget_list_view, remoteAdapterIntent);
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        if (action != null && action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            notifyDataSetChanged(context);
        }
        super.onReceive(context, intent);
    }

    private void notifyDataSetChanged(Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName componentName = new ComponentName(context, BakingAppWidget.class);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetManager.getAppWidgetIds(componentName), R.id.ingredients_widget_list_view);
    }

    // @formatter:off
    @Override
    public void onEnabled(Context context) { }

    @Override
    public void onDisabled(Context context) { }
    // @formatter:on
}

