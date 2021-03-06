package com.se.checkit;

import android.os.SystemClock;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;


/*  class info - this is a test class for the Login Activity   */

@RunWith(AndroidJUnit4ClassRunner.class)
public class LoginTest {

    // Start from Login Page
    @Rule
    public ActivityScenarioRule<LogInActivity> logInActivityActivityScenarioRule = new ActivityScenarioRule<>(LogInActivity.class);


    /*  runs app, with Login Activity as the launcher activity (first activity to run)
     *   then runs infinite loop, so you can continue to manually test (otherwise the method returns, and the app is closed */
//    @Test \
//    public void test_login_manually() {
//        // defines scenario you want to test
//        ActivityScenario<LogInActivity> test = ActivityScenario.launch(LogInActivity.class);
//
//        // infinite loop, to prevent method from returning - which would close app and end the test
//        Thread thread = new Thread();
//        while (true) {
//            try {
//                Thread.sleep(10000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    @Test
    public void test_login_title_visible() {
        onView(withId(R.id.loginTitle)).check(matches(isDisplayed()));
    }

    @Test
    public void test_login_userEmail_textField_visible() {
        onView(withId(R.id.login_user_email)).check(matches(isDisplayed()));
    }

    @Test
    public void test_login_password_textField_visible() {
        onView(withId(R.id.login_password)).check(matches(isDisplayed()));
    }

    @Test
    public void test_login_button_visible() {
        onView(withId(R.id.login_button)).check(matches(isDisplayed()));
    }

    @Test
    public void test_login_newUser_visible() {
        onView(withId(R.id.new_user)).check(matches(isDisplayed()));
    }

    @Test
    public void test_login_images_visible() {
        onView(withId(R.id.tasks_solid)).check(matches(isDisplayed()));
        onView(withId(R.id.check_double_solid)).check(matches(isDisplayed()));
        onView(withId(R.id.check)).check(matches(isDisplayed()));
        onView(withId(R.id.check_it_text)).check(matches(isDisplayed()));
    }

    @Test
    public void test_login_newUser() {
        // Click on New User
        onView(withId(R.id.new_user)).perform(click());

        // Wait Long Enough for Activity / Fragment to Launch
        SystemClock.sleep(1000);

        // On Register Screen
        onView(withId(R.id.registerTitle)).check(matches(isDisplayed()));
    }

    @Test
    public void test_login_validUsernamePassword() {
        // Login with Valid Test Username / Password
        onView(withId(R.id.login_user_email)).perform(typeText("test123@test.com"));
        onView(withId(R.id.login_password)).perform(typeText("test123")).perform(closeSoftKeyboard());
        onView(withId(R.id.login_button)).perform(click());

        // Wait Long Enough for Activity / Fragment to Launch
        SystemClock.sleep(2000);

        // On Create Screen
        onView(withId(R.id.textview_welcome)).check(matches(isDisplayed()));
    }

    @Test
    public void test_login_invalidUsername() {
        // Login with Invalid Username
        onView(withId(R.id.login_user_email)).perform(typeText("test"));
        onView(withId(R.id.login_password)).perform(typeText("test123")).perform(closeSoftKeyboard());
        onView(withId(R.id.login_button)).perform(click());

        // Wait Long Enough for Activity / Fragment to Launch
        SystemClock.sleep(2000);

        // Not On Create Screen
        onView(withId(R.id.textview_welcome)).check(doesNotExist());
    }

    @Test
    public void test_login_invalidPassword_tooShort() {
        // Login with Invalid Password (too short)
        onView(withId(R.id.login_user_email)).perform(typeText("test123@test.com"));
        onView(withId(R.id.login_password)).perform(typeText("test")).perform(closeSoftKeyboard());
        onView(withId(R.id.login_button)).perform(click());

        // Wait Long Enough for Activity / Fragment to Launch
        SystemClock.sleep(2000);

        // Not On Create Screen
        onView(withId(R.id.textview_welcome)).check(doesNotExist());
    }

    @Test
    public void test_login_invalidPassword_incorrect() {
        // Login with Invalid Password (incorrect)
        onView(withId(R.id.login_user_email)).perform(typeText("test123@test.com"));
        onView(withId(R.id.login_password)).perform(typeText("test12345")).perform(closeSoftKeyboard());
        onView(withId(R.id.login_button)).perform(click());

        // Wait Long Enough for Activity / Fragment to Launch
        SystemClock.sleep(2000);

        // Not On Create Screen
        onView(withId(R.id.textview_welcome)).check(doesNotExist());
    }

    @Test
    public void test_login_noInput() {
        // Login with No Username / Password
        onView(withId(R.id.login_button)).perform(click());

        // Wait Long Enough for Activity / Fragment to Launch
        SystemClock.sleep(2000);

        // Not On Create Screen
        onView(withId(R.id.textview_welcome)).check(doesNotExist());
    }

    @Test
    public void test_login_noUsername() {
        // Login with No Username
        onView(withId(R.id.login_password)).perform(typeText("test123")).perform(closeSoftKeyboard());
        onView(withId(R.id.login_button)).perform(click());

        // Wait Long Enough for Activity / Fragment to Launch
        SystemClock.sleep(2000);

        // Not On Create Screen
        onView(withId(R.id.textview_welcome)).check(doesNotExist());
    }

    @Test
    public void test_login_noPassword() {
        // Login with No Password
        onView(withId(R.id.login_user_email)).perform(typeText("test123@test.com")).perform(closeSoftKeyboard());
        onView(withId(R.id.login_button)).perform(click());

        // Wait Long Enough for Activity / Fragment to Launch
        SystemClock.sleep(2000);

        // Not On Create Screen
        onView(withId(R.id.textview_welcome)).check(doesNotExist());
    }


}

