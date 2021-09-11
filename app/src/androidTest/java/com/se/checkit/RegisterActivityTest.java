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

@RunWith(AndroidJUnit4ClassRunner.class)
public class RegisterActivityTest {

        // Start from Login Page
        @Rule
        public ActivityScenarioRule<RegisterActivity> RegisterActivityScenarioRule = new ActivityScenarioRule<>(RegisterActivity.class);


        /*  runs app, with Register Activity as the launcher activity (first activity to run)
         *   then runs infinite loop, so you can continue to manually test (otherwise the method returns, and the app is closed */
//    @Test
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

        // UI Visibility Tests
        @Test
        public void test_registerActivity_title_visible() {
            onView(withId(R.id.registerTitle)).check(matches(isDisplayed()));
        }

        @Test
        public void test_registerActivity_user_email_textField_visible() {
            onView(withId(R.id.register_user_email)).check(matches(isDisplayed()));
        }
        @Test
        public void test_registerActivity_user_name_textField_visible() {
            onView(withId(R.id.register_user_name)).check(matches(isDisplayed()));
        }

        @Test
        public void test_registerActivity_password_textField_visible() {
            onView(withId(R.id.register_password)).check(matches(isDisplayed()));
        }

        @Test
        public void test_registerActivity_register_button_visible() {
            onView(withId(R.id.register_button)).check(matches(isDisplayed()));
        }

        @Test
        public void test_registerActivity_alreadyRegistered_textField_visible() {
            onView(withId(R.id.already_registered)).check(matches(isDisplayed()));
        }

        @Test
        public void test_registerActivity_images_visible() {
            onView(withId(R.id.tasks_solid)).check(matches(isDisplayed()));
            onView(withId(R.id.check_double_solid)).check(matches(isDisplayed()));
            onView(withId(R.id.check)).check(matches(isDisplayed()));
            onView(withId(R.id.check_it_text)).check(matches(isDisplayed()));
        }


        // Functional Tests
        @Test
        public void test_registerActivity_alreadyRegistered() {
            // Click on Log In
            onView(withId(R.id.already_registered)).perform(click());

            // Wait Long Enough for Activity / Fragment to Launch
            SystemClock.sleep(2000);

            // On Login Screen
            onView(withId(R.id.loginTitle)).check(matches(isDisplayed()));
        }



        @Test
        public void test_registerActivity_invalidEmail_alreadyInUse() { // Can't test all valid info because once you use an email, clicking will register it (each test would need a different email)
            // Register with Valid: Email / Username / Password
            onView(withId(R.id.register_user_email)).perform(typeText("registerActivityTest@test.com"));
            onView(withId(R.id.register_user_name)).perform(typeText("testing"));
            onView(withId(R.id.register_password)).perform(typeText("test123")).perform(closeSoftKeyboard());
            onView(withId(R.id.register_button)).perform(click());

            // Wait Long Enough for Activity / Fragment to Launch
            SystemClock.sleep(2000);

            // Go to login Screen
            onView(withId(R.id.loginTitle)).check(matches(isDisplayed()));
        }

        @Test
        public void test_registerActivity_invalidEmail() {
            // Register with Invalid Email
            onView(withId(R.id.register_user_email)).perform(typeText("test"));
            onView(withId(R.id.register_user_name)).perform(typeText("testname"));
            onView(withId(R.id.register_password)).perform(typeText("test123")).perform(closeSoftKeyboard());
            onView(withId(R.id.register_button)).perform(click());

            // Wait Long Enough for Activity / Fragment to Launch
            SystemClock.sleep(2000);

            // Not On Login Screen
            onView(withId(R.id.loginTitle)).check(doesNotExist());
        }

        @Test
        public void test_registerActivity_invalidUsername() {
            // Login with Invalid Username
            onView(withId(R.id.register_user_email)).perform(typeText("test@test.com"));
            onView(withId(R.id.register_user_name)).perform(typeText("23//."));
            onView(withId(R.id.register_password)).perform(typeText("test123")).perform(closeSoftKeyboard());
            onView(withId(R.id.register_button)).perform(click());

            // Wait Long Enough for Activity / Fragment to Launch
            SystemClock.sleep(2000);

            // Not On Login Screen
            onView(withId(R.id.loginTitle)).check(doesNotExist());
        }

            @Test
            public void test_login_invalidPassword_tooShort() {
                // Login with Invalid Password (too short)
                onView(withId(R.id.register_user_email)).perform(typeText("test123@test.com"));
                onView(withId(R.id.register_password)).perform(typeText("test")).perform(closeSoftKeyboard());
                onView(withId(R.id.register_button)).perform(click());

                // Wait Long Enough for Activity / Fragment to Launch
                SystemClock.sleep(2000);

                // Not On Login Screen
                onView(withId(R.id.loginTitle)).check(doesNotExist());
            }

            @Test
            public void test_registerActivity_noInput() {
                // Login with No Username / Password
                onView(withId(R.id.register_button)).perform(click());

                // Wait Long Enough for Activity / Fragment to Launch
                SystemClock.sleep(2000);

                // Not On Login Screen
                onView(withId(R.id.loginTitle)).check(doesNotExist());
            }

        @Test
        public void test_registerActivity_noUserEmail() {
            // Login with No Username
            onView(withId(R.id.register_user_name)).perform(typeText("test"));
            onView(withId(R.id.register_password)).perform(typeText("test123")).perform(closeSoftKeyboard());
            onView(withId(R.id.register_button)).perform(click());
        }
            @Test
            public void test_registerActivity_noUsername() {
                // Login with No Username
                onView(withId(R.id.register_user_email)).perform(typeText("test@test.com"));
                onView(withId(R.id.register_password)).perform(typeText("test123")).perform(closeSoftKeyboard());
                onView(withId(R.id.register_button)).perform(click());

                // Wait Long Enough for Activity / Fragment to Launch
                SystemClock.sleep(2000);

                // Not On Login Screen
                onView(withId(R.id.loginTitle)).check(doesNotExist());
            }

            @Test
            public void test_registerActivity_noPassword() {
                // Login with No Password
                onView(withId(R.id.register_user_email)).perform(typeText("test123@test.com"));
                onView(withId(R.id.register_user_name)).perform(typeText("test")).perform(closeSoftKeyboard());
                onView(withId(R.id.register_button)).perform(click());

                // Wait Long Enough for Activity / Fragment to Launch
                SystemClock.sleep(2000);

                // Not On Login Screen
                onView(withId(R.id.loginTitle)).check(doesNotExist());
            }






}
