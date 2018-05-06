package com.example.vidbregar.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.vidbregar.bakingapp.ui.recipe.RecipeFragment;
import com.example.vidbregar.bakingapp.ui.recipe_step.RecipeStepActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class RecipeStepFragmentTest {

    @Rule
    public ActivityTestRule<RecipeStepActivity> recipeStepActivityTestRule =
            new ActivityTestRule<RecipeStepActivity>(RecipeStepActivity.class) {
                @Override
                protected Intent getActivityIntent() {
                    Context targetContext = InstrumentationRegistry
                            .getInstrumentation()
                            .getTargetContext();
                    Intent recipeStepActivityLaunchIntent = new Intent(targetContext, RecipeStepActivity.class);
                    recipeStepActivityLaunchIntent.putExtra(RecipeFragment.RECIPE_TITLE_EXTRA_KEY, "Nutella Pie");
                    return recipeStepActivityLaunchIntent;
                }
            };

    @Test
    public void testRecipeStepFragment_shouldDisplayRecipeStepTitleInPortrait() {
        onView(withId(R.id.recipe_step_short_description_tv))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testRecipesStepFragment_shouldDisplayRecipeStepInstructionsInPortrait() {
        onView(withId(R.id.recipe_step_description_tv))
                .check(matches(isDisplayed()));
    }
}
