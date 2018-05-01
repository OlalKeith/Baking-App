package com.example.vidbregar.bakingapp.ui.recipe;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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
import com.example.vidbregar.bakingapp.model.RecipeStep;
import com.example.vidbregar.bakingapp.room.AppDatabase;
import com.example.vidbregar.bakingapp.room.RecipeStepEntity;
import com.example.vidbregar.bakingapp.ui.recipe.adapter.IngredientsAdapter;
import com.example.vidbregar.bakingapp.ui.recipe.adapter.RecipeStepsAdapter;
import com.example.vidbregar.bakingapp.ui.recipe_step.RecipeStepActivity;
import com.example.vidbregar.bakingapp.ui.recipe_step.RecipeStepFragment;
import com.google.gson.Gson;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeFragment extends Fragment implements IngredientsAdapter.OnCheckboxClickListener,
        RecipeStepsAdapter.OnRecipeStepClickListener {

    public static final String RECIPE_TITLE_EXTRA_KEY = "recipe-title-extra-key";

    private static final String RECIPE_SAVE_STATE_KEY = "recipe-save-state-key";

    private Recipe recipe;
    private IngredientsAdapter ingredientsAdapter;
    private RecipeStepsAdapter recipeStepsAdapter;
    private boolean isTablet;

    @Inject
    AppDatabase appDatabase;
    @Inject
    Gson gson;

    @BindView(R.id.ingredients_rv)
    RecyclerView ingredientsRecyclerView;
    @BindView(R.id.recipe_steps_rv)
    RecyclerView recipeStepsRecyclerView;

    @Override
    public void onAttach(Context context) {
        RecipeActivity recipeActivity = (RecipeActivity) getActivity();
        recipeActivity.fragmentDispatchingAndroidInjector.inject(this);
        isTablet = getResources().getBoolean(R.bool.isTablet);
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe, container, false);
        ButterKnife.bind(this, rootView);
        if (isTablet) {
            if (savedInstanceState == null) {
                prepareEmptyFragmentIfTablet();
            }
            initialize(savedInstanceState, rootView.getContext());
        } else {
            initialize(savedInstanceState, rootView.getContext());
        }
        return rootView;
    }

    private void prepareEmptyFragmentIfTablet() {
        RecipeStepFragment recipeStepFragment = new RecipeStepFragment();
        getFragmentManager().beginTransaction()
                .add(R.id.recipe_step_fragment_container, recipeStepFragment)
                .addToBackStack(null)
                .commit();
    }

    private void initialize(Bundle savedInstanceState, Context context) {
        if (savedInstanceState != null) {
            restoreFromSavedInstanceState(savedInstanceState);
        }
        prepareIngredientAndRecipeStepLists(context);
    }

    private void restoreFromSavedInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState.containsKey(RECIPE_SAVE_STATE_KEY)) {
            recipe = savedInstanceState.getParcelable(RECIPE_SAVE_STATE_KEY);
        }
    }

    private void prepareIngredientAndRecipeStepLists(Context context) {
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
        recipeStepsAdapter = new RecipeStepsAdapter(context, this);
        recipeStepsRecyclerView.setAdapter(recipeStepsAdapter);
    }

    public void setRecipe(Recipe recipe) {
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

    @Override
    public void onRecipeStepClick(RecipeStep recipeStep) {
        new AsyncOpenRecipeStep().execute(recipeStep);
    }

    private class AsyncOpenRecipeStep extends AsyncTask<RecipeStep, Void, Void> {

        @Override
        protected Void doInBackground(RecipeStep... recipeSteps) {
            String recipeStepJson = gson.toJson(recipeSteps[0]);
            RecipeStepEntity recipeStepEntity = new RecipeStepEntity(1,
                    recipe.getName(),
                    recipeStepJson,
                    0,
                    true);
            appDatabase.recipeStepDao().updateSelectedRecipeStep(recipeStepEntity);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (isTablet) {
                RecipeStepFragment recipeStepFragment = new RecipeStepFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.recipe_step_fragment_container, recipeStepFragment)
                        .commit();
            } else {
                Intent launchRecipeStepActivity = new Intent(getActivity(), RecipeStepActivity.class);
                launchRecipeStepActivity.putExtra(RECIPE_TITLE_EXTRA_KEY, recipe.getName());
                startActivity(launchRecipeStepActivity);
            }
        }
    }
}
