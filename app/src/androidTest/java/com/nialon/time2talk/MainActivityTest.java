package com.nialon.time2talk;

import org.junit.Rule;
import org.junit.Test;

import androidx.test.espresso.Espresso;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.not;

public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void appLaunchesSuccessfully()
    {
        // on vérifie que le texte "Click +" est affiché au démarrage
        Espresso.onView(withId(R.id.tvhelp))
                .check(matches(isDisplayed()));
    }
    @Test
    public void addParticipant()
    {
        // on vérifie que le texte "Click +" n'est plus affiché dès qu'on a ajouté un participant
        Espresso.onView(withId(R.id.imageButton2))
                .perform(click());
        Espresso.onView(withId(R.id.tvhelp))
                .check(matches(not(isDisplayed())));
    }
/*
    @Test
    public void onCreate() {

    }

    @Test
    public void onCreateOptionsMenu() {
    }

    @Test
    public void onOptionsItemSelected() {
    }

    @Test
    public void onConfigurationChanged() {
    }

    @Test
    public void addItem() {
    }

    @Test
    public void resetData() {
    }

    @Test
    public void shareData() {
    }
    */
}