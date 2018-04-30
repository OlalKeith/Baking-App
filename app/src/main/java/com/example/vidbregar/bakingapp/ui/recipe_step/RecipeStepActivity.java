package com.example.vidbregar.bakingapp.ui.recipe_step;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.vidbregar.bakingapp.R;
import com.example.vidbregar.bakingapp.ui.recipe.RecipeFragment;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class RecipeStepActivity extends AppCompatActivity implements HasSupportFragmentInjector {

    private static final String RECIPE_TITLE_SAVE_STATE_KEY = "recipe-title-save-state-key";

    private String recipeTitle;

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);

        initialize(savedInstanceState);
    }

    private void initialize(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            restoreFromSavedInstanceState(savedInstanceState);
        } else {
            initializeFromIntent();
        }
    }

    private void restoreFromSavedInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState.containsKey(RECIPE_TITLE_SAVE_STATE_KEY)) {
            recipeTitle = savedInstanceState.getString(RECIPE_TITLE_SAVE_STATE_KEY);
            updateActivityNameWithRecipeName(recipeTitle);
        }
    }

    private void initializeFromIntent() {
        Intent launchIntent = getIntent();
        if (launchIntent != null) {
            if (launchIntent.hasExtra(RecipeFragment.RECIPE_TITLE_EXTRA_KEY)) {
                recipeTitle = launchIntent.getStringExtra(RecipeFragment.RECIPE_TITLE_EXTRA_KEY);
                updateActivityNameWithRecipeName(recipeTitle);
            }
        } else {
            throw new IllegalStateException("RecipeStepActivity must be provided with extra data");
        }
    }

    private void updateActivityNameWithRecipeName(String recipeTitle) {
        getSupportActionBar().setTitle(recipeTitle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(RECIPE_TITLE_SAVE_STATE_KEY, recipeTitle);
        super.onSaveInstanceState(outState);
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }
}
