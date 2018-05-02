package com.example.vidbregar.bakingapp.ui.main;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

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
    private static final String RECYCLER_VIEW_POSITION_SAVE_STATE_KEY = "list-position-save-state-key";

    private RecipesAdapter recipesAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Disposable recipeDisposable;
    private Parcelable listPositionState;
    private boolean isNetworkAvailable;

    @Inject
    Retrofit retrofit;

    @BindView(R.id.recipes_rv)
    RecyclerView recipesRecyclerView;
    @BindView(R.id.no_internet_connection_container)
    LinearLayout noInternetConnectionContainer;
    @BindView(R.id.refresh_image_button)
    ImageButton refreshFragmentImageButton;

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
        Context context = rootView.getContext();
        checkNetworkAndInitialize(context, rootView, savedInstanceState);
        return rootView;
    }

    private void checkNetworkAndInitialize(Context context, View rootView, Bundle savedInstanceState) {
        isNetworkAvailable = isNetworkAvailable(context);
        if (isNetworkAvailable) {
            noInternetConnectionContainer.setVisibility(View.GONE);
            recipesRecyclerView.setVisibility(View.VISIBLE);
            initialize(rootView.getContext(), savedInstanceState);
        } else {
            recipesRecyclerView.setVisibility(View.GONE);
            noInternetConnectionContainer.setVisibility(View.VISIBLE);
            setRefreshFragmentButtonOnClick(context);
        }
    }

    private boolean isNetworkAvailable(Context context) {
        final ConnectivityManager connectivityManager =
                ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null &&
                connectivityManager.getActiveNetworkInfo().isConnected();
    }

    private void initialize(Context context, Bundle savedInstanceState) {
        boolean isTablet = getResources().getBoolean(R.bool.isTablet);
        if (isTablet) {
            prepareRecipeGrid(context, savedInstanceState);
        } else {
            prepareRecipeList(context, savedInstanceState);
        }
        sendNetworkGetRequest();
    }

    private void prepareRecipeGrid(Context context, Bundle savedInstanceState) {
        layoutManager = new GridLayoutManager(context, 3);
        restoreListPosition(savedInstanceState);
        recipesRecyclerView.setLayoutManager(layoutManager);
        recipesAdapter = new RecipesAdapter(this);
        recipesRecyclerView.setAdapter(recipesAdapter);
    }

    private void prepareRecipeList(Context context, Bundle savedInstanceState) {
        layoutManager = new LinearLayoutManager(context);
        restoreListPosition(savedInstanceState);
        recipesRecyclerView.setLayoutManager(layoutManager);
        recipesAdapter = new RecipesAdapter(this);
        recipesRecyclerView.setAdapter(recipesAdapter);
    }

    private void restoreListPosition(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(RECYCLER_VIEW_POSITION_SAVE_STATE_KEY))
                listPositionState = savedInstanceState.getParcelable(RECYCLER_VIEW_POSITION_SAVE_STATE_KEY);
        }
    }

    private void sendNetworkGetRequest() {
        RecipeService recipeService = retrofit.create(RecipeService.class);
        Observable<List<Recipe>> recipes = recipeService.getRecipes();
        recipeDisposable = recipes.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((data) -> {
                    recipesAdapter.setRecipes(data);
                    layoutManager.onRestoreInstanceState(listPositionState);
                });

    }

    private void setRefreshFragmentButtonOnClick(Context context) {
        refreshFragmentImageButton.setOnClickListener(view -> {
            // Recheck internet connection when clicked
            if (isNetworkAvailable(context)) {
                Intent refreshIntent = new Intent(context, MainActivity.class);
                // Remove current activity instance from back stack
                refreshIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(refreshIntent);
            }
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
        if (isNetworkAvailable) {
            outState.putParcelable(RECYCLER_VIEW_POSITION_SAVE_STATE_KEY, layoutManager.onSaveInstanceState());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (recipeDisposable != null && !recipeDisposable.isDisposed())
            recipeDisposable.dispose();
    }
}
