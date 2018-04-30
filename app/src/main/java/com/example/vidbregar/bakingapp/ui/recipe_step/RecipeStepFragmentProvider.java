package com.example.vidbregar.bakingapp.ui.recipe_step;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class RecipeStepFragmentProvider {

    @ContributesAndroidInjector
    abstract RecipeStepFragment bindRecipeStepFragment();
}
