package com.se.checkit;

import android.os.SystemClock;

import androidx.test.core.app.ActivityScenario;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.action.ViewActions;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.assertion.ViewAssertions.matches;

@RunWith(AndroidJUnit4ClassRunner.class)
public class UiTestCreateFragment {

    // this is an already existing user
    private final String EMAIL = "jttest@gmail.com";
    private final String PASSWORD = "password";

    @Test
    public void test_get_to_create_fragment() {
        ActivityScenario<LogInActivity> scenario = ActivityScenario.launch(LogInActivity.class);

        // login
        onView(withId(R.id.login_user_email)).perform(ViewActions.typeText(EMAIL));
        onView(withId(R.id.login_password)).perform(ViewActions.typeText(PASSWORD)).perform(ViewActions.closeSoftKeyboard());
        SystemClock.sleep(200);
        onView(withId(R.id.login_button)).perform(ViewActions.click());

        // check that stuff in fragment create is there
        SystemClock.sleep(200);
        onView(withId(R.id.editText_date)).check(matches(isDisplayed()));
        onView(withId(R.id.editText_share_with)).check(matches(isDisplayed()));
        onView(withId(R.id.editText_time)).check(matches(isDisplayed()));
        onView(withId(R.id.button_create)).check(matches(isDisplayed()));
    }

    @Test
    public void test_createFragment_stays_there_when_createButton_clicked_with_no_data_filled() {
        ActivityScenario<LogInActivity> scenario = ActivityScenario.launch(LogInActivity.class);

        // login
        onView(withId(R.id.login_user_email)).perform(ViewActions.typeText(EMAIL));
        onView(withId(R.id.login_password)).perform(ViewActions.typeText(PASSWORD)).perform(ViewActions.closeSoftKeyboard());
        SystemClock.sleep(200);
        onView(withId(R.id.login_button)).perform(ViewActions.click());

        // click create button, without inputting any data
        onView(withId(R.id.button_create_frag)).perform(ViewActions.click());

        // assert we are still in create fragment
        onView(withId(R.id.editText_date)).check(matches(isDisplayed()));
        onView(withId(R.id.editText_share_with)).check(matches(isDisplayed()));
        onView(withId(R.id.editText_time)).check(matches(isDisplayed()));
        onView(withId(R.id.button_create_frag)).check(matches(isDisplayed()));
    }

     

}
