package com.example.vidbregar.bakingapp.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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
import com.example.vidbregar.bakingapp.network.RecipeService;
import com.example.vidbregar.bakingapp.ui.main.adapter.RecipesAdapter;
import com.example.vidbregar.bakingapp.ui.recipe.RecipeActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class MainFragment extends Fragment implements RecipesAdapter.OnRecipeClickListener {

    public static final String RECIPE_EXTRA_KEY = "recipe-extra-key";
    private static final String LIST_POSITION_SAVE_STATE_KEY = "list-position-save-state-key";

    private RecipesAdapter recipesAdapter;
    private Disposable recipeDisposable;
    private RecyclerView.LayoutManager linearLayoutManager;
    private Parcelable listPositionState;

    @Inject
    Retrofit retrofit;

    @BindView(R.id.recipes_rv)
    RecyclerView recipesRecyclerView;

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, rootView);

        initialize(rootView.getContext(), savedInstanceState);

        return rootView;
    }

    private void initialize(Context context, Bundle savedInstanceState) {
        prepareRecipeList(context, savedInstanceState);
        sendNetworkGetRequest();
    }

    private void prepareRecipeList(Context context, Bundle savedInstanceState) {
        linearLayoutManager = new LinearLayoutManager(context);
        restoreListPosition(savedInstanceState);
        recipesRecyclerView.setLayoutManager(linearLayoutManager);
        recipesAdapter = new RecipesAdapter(this);
        recipesRecyclerView.setAdapter(recipesAdapter);
    }

    private void restoreListPosition(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(LIST_POSITION_SAVE_STATE_KEY))
                listPositionState = savedInstanceState.getParcelable(LIST_POSITION_SAVE_STATE_KEY);
        }
    }

    private void sendNetworkGetRequest() {
        RecipeService recipeService = retrofit.create(RecipeService.class);
        Observable<List<Recipe>> recipes = recipeService.getRecipes();
        recipeDisposable = recipes.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((data) -> {
                    recipesAdapter.setRecipes(data);
                    linearLayoutManager.onRestoreInstanceState(listPositionState);
                });

    }

    @Override
    public void onClick(Recipe recipe) {
        Intent launchRecipeActivity = new Intent(getActivity(), RecipeActivity.class);
        launchRecipeActivity.putExtra(RECIPE_EXTRA_KEY, recipe);
        startActivity(launchRecipeActivity);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(LIST_POSITION_SAVE_STATE_KEY, linearLayoutManager.onSaveInstanceState());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (recipeDisposable != null && !recipeDisposable.isDisposed())
            recipeDisposable.dispose();
    }
}
