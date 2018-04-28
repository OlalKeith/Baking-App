package com.example.vidbregar.bakingapp.ui.recipe;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.example.vidbregar.bakingapp.R;
import com.example.vidbregar.bakingapp.model.Recipe;
import com.example.vidbregar.bakingapp.ui.main.MainFragment;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class RecipeActivity extends AppCompatActivity implements HasSupportFragmentInjector {

    public static final String RECIPE_SAVE_STATE_KEY = "recipe-save-state-key";

    private Recipe recipe;

    @Inject
    DispatchingAndroidInjector<Fragment> fragmentDispatchingAndroidInjector;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        initialize(savedInstanceState);
        updateActivityNameWithRecipeName();
        passRecipeDataToFragment();
    }

    private void initialize(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(RECIPE_SAVE_STATE_KEY)) {
                recipe = savedInstanceState.getParcelable(RECIPE_SAVE_STATE_KEY);
            }
        } else {
            getRecipeFromLaunchingIntent();
        }
    }

    private void getRecipeFromLaunchingIntent() {
        Intent launchIntent = getIntent();
        if (launchIntent != null) {
            if (launchIntent.hasExtra(MainFragment.RECIPE_EXTRA_INTENT_DATA_KEY)) {
                recipe = launchIntent.getParcelableExtra(MainFragment.RECIPE_EXTRA_INTENT_DATA_KEY);
            }
        }
    }

    private void updateActivityNameWithRecipeName() {
        getSupportActionBar().setTitle(recipe.getName());
    }

    private void passRecipeDataToFragment() {
        RecipeFragment recipeFragment = (RecipeFragment) getSupportFragmentManager().findFragmentById(R.id.recipe_fragment);

        recipeFragment.setRecipeFromActivity(recipe);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(RECIPE_SAVE_STATE_KEY, recipe);
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentDispatchingAndroidInjector;
    }
}
