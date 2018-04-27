package com.example.vidbregar.bakingapp.ui.main;

import android.content.Context;
import android.content.Intent;
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

    public static final String TAG = MainFragment.class.getSimpleName();
    public static final String RECIPE_EXTRA_INTENT_DATA_KEY = "recipe-extra-intent-data-key";

    private Context context;
    private RecipesAdapter recipesAdapter;
    private Disposable recipeDisposable;

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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        context = rootView.getContext();

        ButterKnife.bind(this, rootView);
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recipesRecyclerView.setLayoutManager(linearLayoutManager);
        recipesAdapter = new RecipesAdapter(this);
        recipesRecyclerView.setAdapter(recipesAdapter);

        sendNetworkGetRequest();
        return rootView;
    }

    private void sendNetworkGetRequest() {
        RecipeService recipeService = retrofit.create(RecipeService.class);
        Observable<List<Recipe>> recipes = recipeService.getRecipes();
        recipeDisposable = recipes.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((data) -> recipesAdapter.setRecipes(data));

    }

    @Override
    public void onClick(Recipe recipe) {
        Intent launchRecipeActivity = new Intent(getActivity(), RecipeActivity.class);
        launchRecipeActivity.putExtra(RECIPE_EXTRA_INTENT_DATA_KEY, recipe);
        startActivity(launchRecipeActivity);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (recipeDisposable != null && !recipeDisposable.isDisposed())
            recipeDisposable.dispose();
    }
}
