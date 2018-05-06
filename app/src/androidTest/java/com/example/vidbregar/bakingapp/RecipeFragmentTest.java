package com.example.vidbregar.bakingapp;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.widget.NestedScrollView;

import com.example.vidbregar.bakingapp.model.Ingredient;
import com.example.vidbregar.bakingapp.model.Recipe;
import com.example.vidbregar.bakingapp.model.RecipeStep;
import com.example.vidbregar.bakingapp.ui.main.MainFragment;
import com.example.vidbregar.bakingapp.ui.recipe.RecipeActivity;
import com.example.vidbregar.bakingapp.ui.recipe_step.RecipeStepActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.example.vidbregar.bakingapp.matcher.RecyclerViewMatcher.atPosition;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.not;

@RunWith(AndroidJUnit4.class)
public class RecipeFragmentTest {

    @Rule
    public IntentsTestRule<RecipeActivity> recipeActivityIntentsTestRule =
            new IntentsTestRule<RecipeActivity>(RecipeActivity.class) {
                @Override
                protected Intent getActivityIntent() {
                    Context targetContext = InstrumentationRegistry
                            .getInstrumentation()
                            .getTargetContext();

                    Intent recipeActivityLaunchIntent = new Intent(targetContext, RecipeActivity.class);
                    recipeActivityLaunchIntent.putExtra(MainFragment.RECIPE_EXTRA_KEY, createNutellaPieRecipeFake());
                    return recipeActivityLaunchIntent;
                }
            };

    @Test
    public void testRecipeFragment_shouldDisplayIngredients() {
        onView(withId(R.id.ingredients_rv))
                .perform(RecyclerViewActions.scrollToPosition(0))
                .check(matches(atPosition(0, hasDescendant(withText("Graham Cracker Crumbs")))))
                .check(matches(atPosition(0, hasDescendant(withText("2.0")))))
                .check(matches(atPosition(0, hasDescendant(withText("CUP")))));
    }

    @Test
    public void testClickingOnIngredient_shouldCheckOrUncheck() {
        onView(withId(R.id.ingredients_rv))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withText("Graham Cracker Crumbs"))
                .check(matches(isChecked()));

        onView(withId(R.id.ingredients_rv))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withText("Graham Cracker Crumbs"))
                .check(matches(not(isChecked())));
    }

    @Test
    public void testRecipeFragment_shouldDisplayRecipeSteps() {
        onView(instanceOf(NestedScrollView.class))
                .perform(swipeUp());

        onView(withId(R.id.recipe_steps_rv))
                .perform(RecyclerViewActions.scrollToPosition(0))
                .check(matches(atPosition(0, hasDescendant(withText("Recipe Introduction")))));
    }

    @Before
    public void stubAllExternalIntents() {
        intending(not(isInternal()))
                .respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
    }

    @Test
    public void testClickingOnRecipeStep_shouldLaunchRecipeStepActivity() {
        onView(withId(R.id.recipe_steps_rv))
                .perform(RecyclerViewActions.scrollToPosition(0))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        intended(hasComponent(RecipeStepActivity.class.getName()));
    }

    private Recipe createNutellaPieRecipeFake() {
        List<Ingredient> ingredientList = new ArrayList<>();
        ingredientList.add(new Ingredient("Graham Cracker Crumbs", 2, "CUP"));
        ingredientList.add(new Ingredient("unsalted butter, melted", 6, "TBLSP"));
        ingredientList.add(new Ingredient("granulated sugar", 0.5, "CUP"));
        List<RecipeStep> recipeStepList = new ArrayList<>();
        recipeStepList.add(new RecipeStep(0, "Recipe Introduction", "Recipe Introduction", "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd974_-intro-creampie/-intro-creampie.mp4", ""));
        recipeStepList.add(new RecipeStep(1, "1. Preheat the oven to 350\\u00b0F. Butter a 9\\\" deep dish pie pan.", "Starting prep", "", ""));
        recipeStepList.add(new RecipeStep(2, "2. Whisk the graham cracker crumbs, 50 grams (1/4 cup) of sugar, and 1/2 teaspoon of salt together in a medium bowl. Pour the melted butter and 1 teaspoon of vanilla into the dry ingredients and stir together until evenly mixed.", "Prep the cookie crust.", "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd9a6_2-mix-sugar-crackers-creampie/2-mix-sugar-crackers-creampie.mp4", ""));
        return new Recipe(1, "Nutella Pie", ingredientList, recipeStepList, 8);
    }
}
