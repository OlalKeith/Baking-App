package com.example.vidbregar.bakingapp.ui.recipe;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vidbregar.bakingapp.R;
import com.example.vidbregar.bakingapp.model.Recipe;

public class RecipeFragment extends Fragment {

    private Recipe recipe;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe, container, false);
        recipe = getRecipeFromArguments();
        return rootView;
    }

    public Recipe getRecipeFromArguments() {
        Bundle recipeBundle = getArguments();
        if (recipeBundle != null && recipeBundle.containsKey(RecipeActivity.RECIPE_BUNDLE_KEY)) {
            return recipeBundle.getParcelable(RecipeActivity.RECIPE_BUNDLE_KEY);
        }
        return null;
    }
}
