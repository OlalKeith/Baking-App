package com.example.vidbregar.bakingapp.ui.recipe;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vidbregar.bakingapp.R;
import com.example.vidbregar.bakingapp.model.Recipe;
import com.example.vidbregar.bakingapp.ui.recipe.adapter.IngredientsAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeFragment extends Fragment {

    private Context context;
    private Recipe recipe;

    private IngredientsAdapter ingredientsAdapter;

    @BindView(R.id.ingredients_rv)
    RecyclerView ingredientsRecyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe, container, false);
        context = rootView.getContext();
        ButterKnife.bind(this, rootView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        ingredientsRecyclerView.setLayoutManager(layoutManager);
        ingredientsAdapter = new IngredientsAdapter();
        ingredientsRecyclerView.setAdapter(ingredientsAdapter);

        return rootView;
    }

    public void setRecipeFromActivity(Recipe recipe) {
        this.recipe = recipe;
        ingredientsAdapter.setIngredients(recipe.getIngredients());
    }
}
