package com.example.vidbregar.bakingapp.ui.recipe;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vidbregar.bakingapp.R;
import com.example.vidbregar.bakingapp.model.Ingredient;
import com.example.vidbregar.bakingapp.model.Recipe;
import com.example.vidbregar.bakingapp.ui.recipe.adapter.IngredientsAdapter;
import com.example.vidbregar.bakingapp.ui.recipe.adapter.RecipeStepsAdapter;
import com.google.gson.annotations.Expose;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeFragment extends Fragment implements IngredientsAdapter.OnCheckboxClickListener {

    public static final String RECIPE_SAVE_STATE_KEY = "recipe-save-state-key";

    private Context context;
    private Recipe recipe;

    private IngredientsAdapter ingredientsAdapter;
    private RecipeStepsAdapter recipeStepsAdapter;

    @BindView(R.id.ingredients_rv)
    RecyclerView ingredientsRecyclerView;
    @BindView(R.id.recipe_steps_rv)
    RecyclerView recipeStepsRecyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe, container, false);
        context = rootView.getContext();
        ButterKnife.bind(this, rootView);

        if (savedInstanceState != null && savedInstanceState.containsKey(RECIPE_SAVE_STATE_KEY)) {
            recipe = savedInstanceState.getParcelable(RECIPE_SAVE_STATE_KEY);
        }

        // Ingredients list
        ingredientsRecyclerView.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager ingredientLayoutManager = new LinearLayoutManager(context);
        ingredientsRecyclerView.setLayoutManager(ingredientLayoutManager);
        ingredientsAdapter = new IngredientsAdapter(this);
        ingredientsRecyclerView.setAdapter(ingredientsAdapter);

        // Recipe steps list
        recipeStepsRecyclerView.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager recipeStepsLayoutManager = new LinearLayoutManager(context);
        recipeStepsRecyclerView.setLayoutManager(recipeStepsLayoutManager);
        recipeStepsAdapter = new RecipeStepsAdapter(context);
        recipeStepsRecyclerView.setAdapter(recipeStepsAdapter);

        return rootView;
    }

    public void setRecipeFromActivity(Recipe recipe) {
        this.recipe = recipe;
        ingredientsAdapter.setIngredients(recipe.getIngredients());
        recipeStepsAdapter.setRecipeSteps(recipe.getRecipeSteps());
    }

    @Override
    public void onCheckBoxClick(int position, boolean isCurrentlyChecked) {
        this.recipe.getIngredients().get(position).setCheckedFromList(isCurrentlyChecked);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(RECIPE_SAVE_STATE_KEY, recipe);
    }
}
