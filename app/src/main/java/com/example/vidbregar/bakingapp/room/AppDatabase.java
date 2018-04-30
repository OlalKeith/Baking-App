package com.example.vidbregar.bakingapp.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {RecipeEntity.class, RecipeStepEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract RecipeDao recipeDao();

    public abstract RecipeStepDao recipeStepDao();
}
