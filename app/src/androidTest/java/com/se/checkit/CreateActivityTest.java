package com.se.checkit;

import androidx.test.core.app.ActivityScenario;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

/*  class info - this is a test class for the Create Activity   */

@RunWith(AndroidJUnit4ClassRunner.class)
public class CreateActivityTest {


    /*  runs app, with Create Activity as the launcher activity (first activity to run)
     *   then runs infinite loop, so you can continue to manually test (otherwise the method returns, and the app is closed */
    @Test
    public void test_create_activity_manually() {
        // defines scenario you want to test
        ActivityScenario<CreateActivity> test = ActivityScenario.launch(CreateActivity.class);

        // infinite loop, to prevent method from returning - which would close app and end the test
        Thread thread = new Thread();
        while (true) {
            try {
                thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}