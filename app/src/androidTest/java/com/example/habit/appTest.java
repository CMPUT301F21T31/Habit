package com.example.habit;

import android.app.Activity;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Test class for MainActivity. All the UI tests are written here. Robotium test framework is
 used
 */
@RunWith(AndroidJUnit4.class)

public class appTest{

    private Solo solo;

    @Rule
    public ActivityTestRule<HabitListActivity> rule =
            new ActivityTestRule<>(HabitListActivity.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    /**
     * Gets the Activity
     * @throws Exception
     */
    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    /**
     * Use the regular step to test is the functionalities work properly
     */
    @Test
    public void checkAddHabit(){

        solo.assertCurrentActivity("Wrong Activity", HabitListActivity.class);

        // Click the add button
        View view = solo.getView("add_habit_button");
        solo.clickOnView(view);

        assertTrue(solo.waitForActivity("addHabit"));

        // Enter the title and reason
        solo.enterText((EditText) solo.getView(R.id.habit_title), "T1: Swimming");
        solo.enterText((EditText) solo.getView(R.id.habit_reason), "T1: lose weight");
        assertTrue(solo.waitForText("T1: Swimming", 1, 2000));
        assertTrue(solo.waitForText("T1: lose weight", 1, 2000));

        // Select the start and end dates
        /*
        solo.enterText((EditText) solo.getView(R.id.start_day), "1");
        solo.enterText((EditText) solo.getView(R.id.start_month), "11");
        solo.enterText((EditText) solo.getView(R.id.start_year), "2021");

         */


    }

    /**
     * Close activity after each test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
