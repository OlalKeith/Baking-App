package com.example.vidbregar.bakingapp.room;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.Nullable;

@Entity(tableName = "recipe_table")
public class RecipeEntity {

    @PrimaryKey
    private int id;

    @ColumnInfo(name = "recipe_json")
    @Nullable
    private String recipeJson;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRecipeJson() {
        return recipeJson;
    }

    public void setRecipeJson(String recipeJson) {
        this.recipeJson = recipeJson;
    }
}
