package com.example.vidbregar.bakingapp.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;

import com.example.vidbregar.bakingapp.R;
import com.example.vidbregar.bakingapp.model.Recipe;
import com.example.vidbregar.bakingapp.ui.main.adapter.RecipesAdapter;
import com.example.vidbregar.bakingapp.ui.recipe.RecipeActivity;
import com.google.gson.Gson;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;

public class MainFragment extends Fragment implements LoaderCallbacks<List<Recipe>>, RecipesAdapter.OnRecipeClickListener {

    public static final String TAG = MainFragment.class.getSimpleName();
    public static final int RECIPE_LIST_LOADER_ID = 10;
    public static final String RECIPE_EXTRA_INTENT_DATA_KEY = "recipe-extra-intent-data-key";
    private Context context;

    @BindView(R.id.recipes_rv)
    RecyclerView recipesRecyclerView;
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

        ButterKnife.bind(this, rootView);
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recipesRecyclerView.setLayoutManager(linearLayoutManager);
        recipesAdapter = new RecipesAdapter(this);
        recipesRecyclerView.setAdapter(recipesAdapter);

        getLoaderManager().initLoader(RECIPE_LIST_LOADER_ID, null, this);
        return rootView;
    }

    @NonNull
    @Override
    public Loader<List<Recipe>> onCreateLoader(int id, @Nullable Bundle args) {
        return new RecipeLoader(context, gson);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Recipe>> loader, List<Recipe> recipes) {
        recipesAdapter.setRecipes(recipes);
    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) {
        recipesAdapter.setRecipes(null);
    }

    @Override
    public void onClick(Recipe recipe) {
        Intent launchRecipeActivity = new Intent(getActivity(), RecipeActivity.class);
        launchRecipeActivity.putExtra(RECIPE_EXTRA_INTENT_DATA_KEY, recipe);
        startActivity(launchRecipeActivity);
    }
}
