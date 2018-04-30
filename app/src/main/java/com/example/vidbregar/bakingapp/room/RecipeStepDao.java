package com.example.vidbregar.bakingapp.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

@Dao
public interface RecipeStepDao {

    @Query("SELECT * FROM recipe_step_table WHERE id = 1")
    RecipeStepEntity getSelectedRecipeStep();

    @Update
    void updateSelectedRecipeStep(RecipeStepEntity recipeStepEntity);
}
