package com.example.vidbregar.bakingapp.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

@Dao
public interface RecipeDao {

    @Query("SELECT * FROM recipe_table WHERE id = 1")
    RecipeEntity getRecipe();

    @Query("UPDATE recipe_table SET recipe_json = :jsonRecipe WHERE id = 1")
    int updateRecipe(String jsonRecipe);

    @Insert
    void insertRecipe(RecipeEntity recipeEntity);
}
