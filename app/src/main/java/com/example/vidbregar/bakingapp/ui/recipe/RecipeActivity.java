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

    public static final String RECIPE_BUNDLE_KEY = "recipe-bundle-key";
    @Inject
    DispatchingAndroidInjector<Fragment> fragmentDispatchingAndroidInjector;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        Recipe recipe = getRecipeFromLaunchingIntent();
        updateActivityNameWithRecipeName(recipe);
        passRecipeDataToFragment(recipe);

    }

    private Recipe getRecipeFromLaunchingIntent() {
        Intent launchIntent = getIntent();
        if (launchIntent != null && launchIntent.hasExtra(MainFragment.RECIPE_EXTRA_INTENT_DATA_KEY)) {
            return launchIntent.getParcelableExtra(MainFragment.RECIPE_EXTRA_INTENT_DATA_KEY);
        }
        throw new IllegalStateException("RecipeActivity must be launched via an Intent");
    }

    private void updateActivityNameWithRecipeName(Recipe recipe) {
        getSupportActionBar().setTitle(recipe.getName());
    }

    private void passRecipeDataToFragment(Recipe recipe) {
        RecipeFragment recipeFragment = (RecipeFragment) getSupportFragmentManager().findFragmentById(R.id.recipe_fragment);
        recipeFragment.setRecipeFromActivity(recipe);
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentDispatchingAndroidInjector;
    }
}
