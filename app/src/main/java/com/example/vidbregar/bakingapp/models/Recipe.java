package com.example.vidbregar.bakingapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Recipe implements Parcelable {
    private int recipeId;
    private String name;
    private List<Ingredient> ingredients;
    private List<RecipeStep> recipeSteps;
    private int servings;

    public Recipe(int recipeId, String name, List<Ingredient> ingredients, List<RecipeStep> recipeSteps, int servings) {
        this.recipeId = recipeId;
        this.name = name;
        this.ingredients = ingredients;
        this.recipeSteps = recipeSteps;
        this.servings = servings;
    }

    public Recipe(Parcel in) {
        recipeId = in.readInt();
        name = in.readString();
        ingredients = in.createTypedArrayList(Ingredient.CREATOR);
        recipeSteps = in.createTypedArrayList(RecipeStep.CREATOR);
        servings = in.readInt();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public int getRecipeId() {
        return recipeId;
    }

    public String getName() {
        return name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public List<RecipeStep> getRecipeSteps() {
        return recipeSteps;
    }

    public int getServings() {
        return servings;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(recipeId);
        dest.writeString(name);
        dest.writeTypedList(ingredients);
        dest.writeTypedList(recipeSteps);
        dest.writeInt(servings);
    }
}
