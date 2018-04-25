package com.example.vidbregar.bakingapp.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vidbregar.bakingapp.R;
import com.example.vidbregar.bakingapp.model.Recipe;
import com.example.vidbregar.bakingapp.ui.main.adapter.RecipesAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

public class MainFragment extends Fragment {

    private Context context;

    private RecyclerView recipesRecyclerView;
    private RecipesAdapter recipesAdapter;
    @Inject
    Gson gson;

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        context = rootView.getContext();

        recipesRecyclerView = rootView.findViewById(R.id.recipes_recycler_view);
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recipesRecyclerView.setLayoutManager(linearLayoutManager);
        recipesAdapter = new RecipesAdapter();
        recipesRecyclerView.setAdapter(recipesAdapter);

        String json = getJsonStringFromAssets();
        Type listType = new TypeToken<ArrayList<Recipe>>() {
        }.getType();
        List<Recipe> recipes = gson.fromJson(json, listType);

        recipesAdapter.setRecipes(recipes);
        return rootView;
    }

    private String getJsonStringFromAssets() {
        String json = null;
        try {
            InputStream inputStream = context.getAssets().open("recipe_listing.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }
}
