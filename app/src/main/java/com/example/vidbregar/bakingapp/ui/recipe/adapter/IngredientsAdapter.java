package com.example.vidbregar.bakingapp.ui.recipe.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.vidbregar.bakingapp.R;
import com.example.vidbregar.bakingapp.model.Ingredient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder> {

    public interface OnCheckboxClickListener {
        void onCheckBoxClick(int position, boolean isCurrentlyChecked);
    }

    private List<Ingredient> ingredients;
    private OnCheckboxClickListener onCheckboxClickListener;

    public IngredientsAdapter(OnCheckboxClickListener onCheckboxClickListener) {
        this.onCheckboxClickListener = onCheckboxClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ingredient_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ingredient ingredient = ingredients.get(position);
        holder.ingredientCheckbox.setText(ingredient.getIngredientName());
        holder.ingredientCheckbox.setChecked(ingredient.isCheckedFromList());
        holder.quantityTextView.setText("" + ingredient.getQuantity());
        holder.measureTextView.setText(ingredient.getMeasure());
    }

    @Override
    public int getItemCount() {
        if (ingredients == null) return 0;
        else return ingredients.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener {

        @BindView(R.id.ingredient_cb)
        CheckBox ingredientCheckbox;
        @BindView(R.id.ingredient_quantity_tv)
        TextView quantityTextView;
        @BindView(R.id.ingredient_measure_tv)
        TextView measureTextView;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            ingredientCheckbox.setOnCheckedChangeListener(this);
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            onCheckboxClickListener.onCheckBoxClick(getAdapterPosition(), isChecked);
        }
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
        notifyDataSetChanged();
    }
}
