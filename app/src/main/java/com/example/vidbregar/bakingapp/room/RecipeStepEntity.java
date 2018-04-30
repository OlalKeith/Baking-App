package com.example.vidbregar.bakingapp.room;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "recipe_step_table")
public class RecipeStepEntity {

    @PrimaryKey
    private int id;
    private String recipeTitle;
    private String recipeStepJson;
    private long currentPosition;
    private boolean isPlaying;

    public RecipeStepEntity(int id, String recipeTitle, String recipeStepJson, long currentPosition, boolean isPlaying) {
        this.id = id;
        this.recipeTitle = recipeTitle;
        this.recipeStepJson = recipeStepJson;
        this.currentPosition = currentPosition;
        this.isPlaying = isPlaying;
    }

    public int getId() {
        return id;
    }

    public String getRecipeTitle() {
        return recipeTitle;
    }

    public String getRecipeStepJson() {
        return recipeStepJson;
    }

    public long getCurrentPosition() {
        return currentPosition;
    }

    public boolean isPlaying() {
        return isPlaying;
    }
}
