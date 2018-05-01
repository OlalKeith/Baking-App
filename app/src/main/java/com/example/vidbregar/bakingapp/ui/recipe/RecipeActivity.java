package com.example.vidbregar.bakingapp.ui.recipe;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.vidbregar.bakingapp.R;
import com.example.vidbregar.bakingapp.model.Recipe;
import com.example.vidbregar.bakingapp.room.AppDatabase;
import com.example.vidbregar.bakingapp.room.RecipeStepEntity;
import com.example.vidbregar.bakingapp.ui.main.MainFragment;
import com.example.vidbregar.bakingapp.widget.BakingAppWidget;
import com.google.gson.Gson;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class RecipeActivity extends AppCompatActivity implements HasSupportFragmentInjector {

    public static final String RECIPE_SAVE_STATE_KEY = "recipe-save-state-key";

    private Recipe recipe;
    @Inject
    AppDatabase appDatabase;
    @Inject
    Gson gson;

    @Inject
    DispatchingAndroidInjector<Fragment> fragmentDispatchingAndroidInjector;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        initialize(savedInstanceState);
    }

    private void initialize(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            restoreFromSavedInstanceState(savedInstanceState);
        } else {
            getRecipeFromLaunchingIntent();
        }
        updateActivityNameWithRecipeName();
        passRecipeToFragment();
    }

    private void restoreFromSavedInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState.containsKey(RECIPE_SAVE_STATE_KEY)) {
            recipe = savedInstanceState.getParcelable(RECIPE_SAVE_STATE_KEY);
        }
    }

    private void getRecipeFromLaunchingIntent() {
        Intent launchIntent = getIntent();
        if (launchIntent != null) {
            if (launchIntent.hasExtra(MainFragment.RECIPE_EXTRA_KEY)) {
                recipe = launchIntent.getParcelableExtra(MainFragment.RECIPE_EXTRA_KEY);
            }
        }
    }

    private void updateActivityNameWithRecipeName() {
        getSupportActionBar().setTitle(recipe.getName());
    }

    private void passRecipeToFragment() {
        RecipeFragment recipeFragment =
                (RecipeFragment) getSupportFragmentManager().findFragmentById(R.id.recipe_fragment);
        recipeFragment.setRecipe(recipe);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recipe_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_to_widget_action:
                new AsyncWidgetUpdate().execute();
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(RECIPE_SAVE_STATE_KEY, recipe);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearSelectedRecipeStep();
    }

    private void clearSelectedRecipeStep() {
        new AsyncClearRecipeStep().execute();
    }

    private class AsyncClearRecipeStep extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            appDatabase.recipeStepDao().updateSelectedRecipeStep(new RecipeStepEntity(1, "", "", 0, true));
            return null;
        }
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentDispatchingAndroidInjector;
    }

    private class AsyncWidgetUpdate extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... voids) {
            return appDatabase.recipeDao().updateRecipe(gson.toJson(recipe));
        }

        @Override
        protected void onPostExecute(Integer integer) {
            // There should be only one record at a time and it should have id 1
            if (integer == 1) {
                updateWidget();
            }
        }

        private void updateWidget() {
            Intent updateWidgetIntent = new Intent(RecipeActivity.this, BakingAppWidget.class);
            updateWidgetIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            int[] ids = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), BakingAppWidget.class));
            updateWidgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
            sendBroadcast(updateWidgetIntent);
        }
    }
}
