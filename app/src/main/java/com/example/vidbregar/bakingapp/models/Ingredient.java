package com.example.vidbregar.bakingapp.models;

import android.os.Parcel;
import android.os.Parcelable;

class Ingredient implements Parcelable {
    private String ingredientName;
    private double quantity;
    private String measure;

    public Ingredient(String ingredientName, double quantity, String measure) {
        this.ingredientName = ingredientName;
        this.quantity = quantity;
        this.measure = measure;
    }

    public Ingredient(Parcel in) {
        ingredientName = in.readString();
        quantity = in.readDouble();
        measure = in.readString();
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    public String getIngredientName() {
        return ingredientName;
    }

    public double getQuantity() {
        return quantity;
    }

    public String getMeasure() {
        return measure;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ingredientName);
        dest.writeDouble(quantity);
        dest.writeString(measure);
    }
}
