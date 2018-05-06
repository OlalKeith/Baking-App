package com.example.vidbregar.bakingapp;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import com.example.vidbregar.bakingapp.ui.main.MainActivity;
import com.example.vidbregar.bakingapp.ui.recipe.RecipeActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.example.vidbregar.bakingapp.matcher.RecyclerViewMatcher.atPosition;
import static org.hamcrest.CoreMatchers.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainFragmentTest {

    @Rule
    public IntentsTestRule<MainActivity> mainActivityIntentsTestRule =
            new IntentsTestRule<>(MainActivity.class);

    private IdlingResource idlingResource;

    @Before
    public void registerIdlingResource() {
        idlingResource = mainActivityIntentsTestRule.getActivity().getIdlingResource();
        // To prove that the test fails, omit this call:
        IdlingRegistry.getInstance().register(idlingResource);
    }

    @After
    public void unregisterIdlingResource() {
        if (idlingResource != null) {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }


    @Test
    public void testRecipeRecyclerView_shouldBeDisplayed() {
        onView(withId(R.id.recipes_rv))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testRecipeRecyclerView_shouldHaveRecipes() {
        // First item
        onView(withId(R.id.recipes_rv))
                .perform(RecyclerViewActions.scrollToPosition(0))
                .check(matches(atPosition(0, hasDescendant(withText("Nutella Pie")))))
                .check(matches(atPosition(0, hasDescendant(withText("8")))));

        // Last item
        onView(withId(R.id.recipes_rv))
                .perform(RecyclerViewActions.scrollToPosition(3))
                .check(matches(atPosition(3, hasDescendant(withText("Cheesecake")))))
                .check(matches(atPosition(3, hasDescendant(withText("8")))));
    }

    @Before
    public void stubAllExternalIntents() {
        intending(not(isInternal()))
                .respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
    }

    @Test
    public void testClickingOnRecipe_shouldLaunchRecipeActivity() {
        onView(withId(R.id.recipes_rv))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        intended(hasComponent(RecipeActivity.class.getName()));
    }
}
