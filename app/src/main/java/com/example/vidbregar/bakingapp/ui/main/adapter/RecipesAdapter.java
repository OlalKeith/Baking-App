package com.example.vidbregar.bakingapp.ui.main.adapter;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.vidbregar.bakingapp.R;
import com.example.vidbregar.bakingapp.model.Recipe;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.ViewHolder> {

    public interface OnRecipeClickListener {
        void onClick(Recipe recipe);
    }

    private List<Recipe> recipes;
    private OnRecipeClickListener onRecipeClickListener;

    // List of recipes must be provided through setRecipes method
    public RecipesAdapter(OnRecipeClickListener onRecipeClickListener) {
        this.onRecipeClickListener = onRecipeClickListener;
    }

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
        viewHolder.servingsNumberTextView.setText("" + recipe.getServings());
        String lastVideoUrl = recipe.getRecipeSteps().get(recipe.getRecipeSteps().size() - 1).getVideoUrl();
        RequestOptions requestOptions = new RequestOptions().
                error(R.drawable.recipe_list_error_placeholder);
        Glide.with(viewHolder.itemView.getContext())
                .setDefaultRequestOptions(requestOptions)
                .load(Uri.parse(lastVideoUrl))
                .into(viewHolder.recipeThumbnailImageView);
    }

    @Override
    public int getItemCount() {
        if (recipes == null) return 0;
        else return recipes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.recipe_title_tv)
        TextView recipeTitleTextView;
        @BindView(R.id.servings_number_tv)
        TextView servingsNumberTextView;
        @BindView(R.id.recipe_thumbnail_iv)
        RoundedImageView recipeThumbnailImageView;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onRecipeClickListener.onClick(recipes.get(getAdapterPosition()));
        }
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
        notifyDataSetChanged();
    }
}
