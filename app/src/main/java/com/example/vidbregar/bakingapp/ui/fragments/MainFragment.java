package com.example.vidbregar.bakingapp.ui.fragments;

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
import com.example.vidbregar.bakingapp.ui.adapters.RecipesAdapter;

public class MainFragment extends Fragment {

    private Context context;

    private RecyclerView recipesRecyclerView;
    private RecipesAdapter recipesAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        context = rootView.getContext();

        recipesRecyclerView = rootView.findViewById(R.id.recipes_recycler_view);
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recipesRecyclerView.setLayoutManager(linearLayoutManager);
        recipesAdapter = new RecipesAdapter();
        recipesRecyclerView.setAdapter(recipesAdapter);
        return rootView;
    }
}
