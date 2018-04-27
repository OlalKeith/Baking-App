package com.example.vidbregar.bakingapp.ui.main;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.vidbregar.bakingapp.model.Recipe;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

class RecipeLoader extends AsyncTaskLoader<List<Recipe>> {

    private Gson gson;
    private SoftReference<Context> contextWeakReference;

    RecipeLoader(Context context, Gson gson) {
        super(context);
        this.contextWeakReference = new SoftReference<>(context);
        this.gson = gson;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Recipe> loadInBackground() {
        String json = getJsonStringFromAssets();
        Type listType = new TypeToken<ArrayList<Recipe>>() {
        }.getType();
        return gson.fromJson(json, listType);
    }

    private String getJsonStringFromAssets() {
        String json = null;
        try {
            InputStream inputStream = contextWeakReference.get().getAssets().open("recipe_listing.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            Log.e("TAG", "Context was null");
        }
        return json;
    }


}