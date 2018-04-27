package com.example.vidbregar.bakingapp.ui.recipe_step;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.vidbregar.bakingapp.R;
import com.example.vidbregar.bakingapp.model.RecipeStep;
import com.example.vidbregar.bakingapp.ui.recipe.RecipeFragment;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class RecipeStepActivity extends AppCompatActivity implements HasSupportFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);
        initialize();
    }

    private void initialize() {
        Intent launchIntent = getIntent();
        if (launchIntent != null) {
            if (launchIntent.hasExtra(RecipeFragment.RECIPE_TITLE_EXTRA_KEY)) {
                RecipeStep recipeStep = launchIntent.getParcelableExtra(RecipeFragment.RECIPE_STEP_EXTRA_KEY);
                passRecipeStepToFragment(recipeStep);
            }
            if (launchIntent.hasExtra(RecipeFragment.RECIPE_TITLE_EXTRA_KEY)) {
                String recipeTitle = launchIntent.getStringExtra(RecipeFragment.RECIPE_TITLE_EXTRA_KEY);
                updateActivityNameWithRecipeName(recipeTitle);
            }
        } else {
            throw new IllegalStateException("RecipeStepActivity must be provided with extra data");
        }
    }

    private void passRecipeStepToFragment(RecipeStep recipeStep) {
        RecipeStepFragment recipeStepFragment =
                (RecipeStepFragment) getSupportFragmentManager().findFragmentById(R.id.recipe_step_fragment);
        recipeStepFragment.setRecipeStep(recipeStep);
    }

    private void updateActivityNameWithRecipeName(String recipeTitle) {
        getSupportActionBar().setTitle(recipeTitle);
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }
}
