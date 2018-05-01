package com.example.vidbregar.bakingapp.dagger.builder;

import com.example.vidbregar.bakingapp.ui.main.MainActivity;
import com.example.vidbregar.bakingapp.ui.main.MainFragmentProvider;
import com.example.vidbregar.bakingapp.ui.recipe.RecipeActivity;
import com.example.vidbregar.bakingapp.ui.recipe.RecipeFragmentProvider;
import com.example.vidbregar.bakingapp.ui.recipe_step.RecipeStepActivity;
import com.example.vidbregar.bakingapp.ui.recipe_step.RecipeStepFragmentProvider;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = MainFragmentProvider.class)
    abstract MainActivity bindMainActivity();

    @ContributesAndroidInjector(modules = {RecipeFragmentProvider.class, RecipeStepFragmentProvider.class})
    abstract RecipeActivity bindRecipeActivity();

    @ContributesAndroidInjector(modules = RecipeStepFragmentProvider.class)
    abstract RecipeStepActivity bindRecipeStepActivity();
}