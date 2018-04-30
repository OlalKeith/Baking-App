package com.example.vidbregar.bakingapp.widget;

import android.app.Application;
import android.content.Context;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.vidbregar.bakingapp.R;
import com.example.vidbregar.bakingapp.model.Ingredient;
import com.example.vidbregar.bakingapp.model.Recipe;
import com.example.vidbregar.bakingapp.room.AppDatabase;
import com.google.gson.Gson;

import java.util.List;

public class BakingRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context applicationContext;
    private AppDatabase appDatabase;
    private Gson gson;
    private List<Ingredient> ingredients;

    BakingRemoteViewsFactory(Application applicationContext, AppDatabase appDatabase, Gson gson) {
        this.applicationContext = applicationContext;
        this.appDatabase = appDatabase;
        this.gson = gson;
    }

    @Override
    public void onCreate() {
        ingredients = queryRecipeIngredients();
    }

    @Override
    public void onDataSetChanged() {
        ingredients = queryRecipeIngredients();
    }

    private List<Ingredient> queryRecipeIngredients() {
        Recipe recipe = gson.fromJson(appDatabase.recipeDao().getRecipe().getRecipeJson(), Recipe.class);
        if (recipe != null) return recipe.getIngredients();
        else return null;
    }

    @Override
    public void onDestroy() {
        applicationContext = null;
        appDatabase = null;
        gson = null;
        ingredients = null;
    }

    @Override
    public int getCount() {
        if (ingredients == null)
            return 1; // if the user hasn't specified a recipe, return a message
        else return ingredients.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(applicationContext.getPackageName(), R.layout.ingredient_widget_item);
        if (ingredients != null) {
            Ingredient ingredient = ingredients.get(position);
            remoteViews.setTextViewText(R.id.ingredient_widget_tv, ingredient.getIngredientName());
            remoteViews.setTextViewText(R.id.ingredient_widget_quantity_tv, ingredient.getQuantity() + "");
            remoteViews.setTextViewText(R.id.ingredient_widget_measure_tv, ingredient.getMeasure());
        } else {
            remoteViews.setTextViewText(R.id.ingredient_widget_tv, "Please add a recipe to widget");
        }
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public boolean hasStableIds() {
        return false;
    }
}
