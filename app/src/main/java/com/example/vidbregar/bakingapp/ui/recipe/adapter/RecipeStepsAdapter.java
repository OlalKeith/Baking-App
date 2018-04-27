package com.example.vidbregar.bakingapp.ui.recipe.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vidbregar.bakingapp.R;
import com.example.vidbregar.bakingapp.model.RecipeStep;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepsAdapter extends RecyclerView.Adapter<RecipeStepsAdapter.ViewHolder> {

    private List<RecipeStep> recipeSteps;
    private Context context;

    public RecipeStepsAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_step_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        RecipeStep recipeStep = recipeSteps.get(position);
        viewHolder.recipeStepTitleTextView.setText(recipeStep.getShortDescription());
        viewHolder.recipeStepBackgroundImageView.setBackgroundColor(getRandomColor());
    }

    @Override
    public int getItemCount() {
        if (recipeSteps == null) return 0;
        else return recipeSteps.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.recipe_step_background_iv)
        RoundedImageView recipeStepBackgroundImageView;
        @BindView(R.id.recipe_step_title_tv)
        TextView recipeStepTitleTextView;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setRecipeSteps(List<RecipeStep> recipeSteps) {
        this.recipeSteps = recipeSteps;
        notifyDataSetChanged();
    }

    private int getRandomColor() {
        int color;
        TypedArray colorsArray = context.getResources().obtainTypedArray(R.array.mdcolor_500);
        int index = (int) (Math.random() * colorsArray.length());
        color = colorsArray.getColor(index, Color.BLACK);
        colorsArray.recycle();
        return color;
    }
}
