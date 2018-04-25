package com.example.vidbregar.bakingapp.ui.main.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vidbregar.bakingapp.R;
import com.example.vidbregar.bakingapp.model.Recipe;

import java.util.List;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.ViewHolder> {

    List<Recipe> recipes;

    // List of recipes must be provided through setRecipes method
    public RecipesAdapter() { }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Recipe recipe = recipes.get(position);
        viewHolder.recipeTitleTextView.setText(recipe.getName());
        viewHolder.servingsNumberTextView.setText(Integer.toString(recipe.getServings()));
    }

    @Override
    public int getItemCount() {
        if (recipes == null) return 0;
        else return recipes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView recipeTitleTextView;
        public TextView servingsNumberTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            recipeTitleTextView = itemView.findViewById(R.id.recipe_title_tv);
            servingsNumberTextView = itemView.findViewById(R.id.servings_number_tv);
        }
    }

    public void setRecipes(@NonNull List<Recipe> recipes) {
        this.recipes = recipes;
    }
}
