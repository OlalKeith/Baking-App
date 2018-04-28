package com.example.vidbregar.bakingapp.ui.recipe_step;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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

    private static final String RECIPE_STEP_FRAGMENT_TAG = "recipe-step-fragment-tag";

    public static final String RECIPE_STEP_ARGS_KEY = "recipe-step-data-key";
    public static final String CURRENT_POSITION_ARGS_KEY = "current-position-args-key";
    public static final String IS_PLAYING_ARGS_KEY = "is-playing-args-key";

    private static final String RECIPE_STEP_SAVE_STATE_KEY = "recipe-step-data-key";
    private static final String CURRENT_POSITION_SAVE_STATE_KEY = "current-position-save-state-key";
    private static final String IS_PLAYING_SAVE_STATE_KEY = "is-playing-save-state-key";
    private static final String RECIPE_TITLE_SAVE_STATE_KEY = "recipe-title-save-state-key";

    private RecipeStep recipeStep;
    private long currentPosition = 0;
    private boolean isPlaying = true;
    private String recipeTitle;

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(RECIPE_STEP_SAVE_STATE_KEY) &&
                    savedInstanceState.containsKey(CURRENT_POSITION_SAVE_STATE_KEY) &&
                    savedInstanceState.containsKey(IS_PLAYING_SAVE_STATE_KEY) &&
                    savedInstanceState.containsKey(RECIPE_TITLE_SAVE_STATE_KEY)) {
                recipeStep = savedInstanceState.getParcelable(RECIPE_STEP_SAVE_STATE_KEY);
                currentPosition = savedInstanceState.getLong(CURRENT_POSITION_SAVE_STATE_KEY);
                isPlaying = savedInstanceState.getBoolean(IS_PLAYING_SAVE_STATE_KEY);
                recipeTitle = savedInstanceState.getString(RECIPE_TITLE_SAVE_STATE_KEY);
                updateActivityNameWithRecipeName(recipeTitle);
                replaceFragment(prepareFragment(recipeStep, currentPosition, isPlaying));
            }
        } else {
            initialize();
        }
    }

    private void replaceFragment(RecipeStepFragment recipeStepFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.recipe_step_fragment_container, recipeStepFragment, RECIPE_STEP_FRAGMENT_TAG)
                .commit();
    }

    private void initialize() {
        Intent launchIntent = getIntent();
        if (launchIntent != null) {
            if (launchIntent.hasExtra(RecipeFragment.RECIPE_STEP_EXTRA_KEY)) {
                recipeStep = launchIntent.getParcelableExtra(RecipeFragment.RECIPE_STEP_EXTRA_KEY);
                startRecipeStepFragment(prepareFragment(recipeStep, currentPosition, isPlaying));
            }
            if (launchIntent.hasExtra(RecipeFragment.RECIPE_TITLE_EXTRA_KEY)) {
                recipeTitle = launchIntent.getStringExtra(RecipeFragment.RECIPE_TITLE_EXTRA_KEY);
                updateActivityNameWithRecipeName(recipeTitle);
            }
        } else {
            throw new IllegalStateException("RecipeStepActivity must be provided with extra data");
        }
    }

    private RecipeStepFragment prepareFragment(RecipeStep recipeStep, long currentPosition, boolean isPlaying) {
        RecipeStepFragment recipeStepFragment = new RecipeStepFragment();
        Bundle args = new Bundle(3);
        args.putParcelable(RECIPE_STEP_ARGS_KEY, recipeStep);
        args.putLong(CURRENT_POSITION_ARGS_KEY, currentPosition);
        args.putBoolean(IS_PLAYING_ARGS_KEY, isPlaying);
        recipeStepFragment.setArguments(args);
        return recipeStepFragment;
    }

    private void startRecipeStepFragment(RecipeStepFragment recipeStepFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.recipe_step_fragment_container, recipeStepFragment, RECIPE_STEP_FRAGMENT_TAG)
                .commit();
    }

    private void updateActivityNameWithRecipeName(String recipeTitle) {
        getSupportActionBar().setTitle(recipeTitle);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        RecipeStepFragment recipeStepFragment = (RecipeStepFragment) getSupportFragmentManager().findFragmentByTag(RECIPE_STEP_FRAGMENT_TAG);
        outState.putLong(CURRENT_POSITION_SAVE_STATE_KEY, recipeStepFragment.getCurrentPlayerPosition());
        outState.putBoolean(IS_PLAYING_SAVE_STATE_KEY, recipeStepFragment.isPlaying());
        outState.putString(RECIPE_TITLE_SAVE_STATE_KEY, recipeTitle);
        outState.putParcelable(RECIPE_STEP_SAVE_STATE_KEY, recipeStep);
        super.onSaveInstanceState(outState);
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }
}
