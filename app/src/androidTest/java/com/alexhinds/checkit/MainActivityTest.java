package com.alexhinds.checkit;

import android.util.Log;

import androidx.test.core.app.ActivityScenario;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;


/*  class info - this is a test class for the Main Activity   */

@RunWith(AndroidJUnit4ClassRunner.class)
public class MainActivityTest {


    /*  runs app, with Main Activity as the launcher activity (first activity to run)
     *   then runs infinite loop, so you can continue to manually test (otherwise the method returns, and the app is closed */
    // Currently fails b/c no UID -> NPE
    @Test
    public void test_main_manually() {
        // defines scenario you want to test
        ActivityScenario<MainActivity> test = ActivityScenario.launch(MainActivity.class);

        // infinite loop, to prevent method from returning - which would close app and end the test
        Thread thread = new Thread();
        while (true) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void test_login_fields_visible() {
        // Start at Login
        ActivityScenario<LogInActivity> test = ActivityScenario.launch(LogInActivity.class);

        // Fields are Visible
        onView(withId(R.id.login_user_email)).check(matches(isDisplayed()));
        onView(withId(R.id.login_password)).check(matches(isDisplayed()));
        onView(withId(R.id.login_button)).check(matches(isDisplayed()));
    }

    @Test
    public void test_login() {
        // Start at Login
        ActivityScenario<LogInActivity> test = ActivityScenario.launch(LogInActivity.class);

        // Login with Test Username / Password
        onView(withId(R.id.login_user_email)).perform(typeText("test123@test.com"));
        onView(withId(R.id.login_password)).perform(typeText("test123")).perform(closeSoftKeyboard());
        onView(withId(R.id.login_button)).perform(click());

        // On Create Screen
        onView(withId(R.id.textview_welcome)).check(matches(isDisplayed()));
    }


}

