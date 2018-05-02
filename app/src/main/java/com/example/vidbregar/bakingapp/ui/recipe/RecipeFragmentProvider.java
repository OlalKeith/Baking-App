package com.example.vidbregar.bakingapp.ui.recipe;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class RecipeFragmentProvider {

    @ContributesAndroidInjector
    abstract RecipeFragment bindRecipeFragment();
}
