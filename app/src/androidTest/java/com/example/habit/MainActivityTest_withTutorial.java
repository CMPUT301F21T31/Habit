package com.example.habit;


import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import static androidx.test.InstrumentationRegistry.getInstrumentation;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import com.example.habit.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest_withTutorial {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void mainActivityTest() {
        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.t_1t),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("lewis"), closeSoftKeyboard());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.t_1b), withText("->"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.t_2t),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText2.perform(click());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.t_2t),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("lewis214"), closeSoftKeyboard());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.t2b), withText("next"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.tutorial_email_input),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText4.perform(click());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.tutorial_email_input),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText5.perform(replaceText("lewistest@gmail.com"), closeSoftKeyboard());

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.tutorial_pwd_input),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatEditText6.perform(click());

        ViewInteraction appCompatEditText7 = onView(
                allOf(withId(R.id.tutorial_pwd_input),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatEditText7.perform(replaceText("qqqqqq"), closeSoftKeyboard());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.tutorial_emailpwd_continue), withText("Continue"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        appCompatButton3.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatEditText8 = onView(
                allOf(withId(R.id.t3et),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText8.perform(click());

        ViewInteraction appCompatEditText9 = onView(
                allOf(withId(R.id.t3et),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText9.perform(replaceText("running"), closeSoftKeyboard());

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.t3b), withText("Next"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatButton4.perform(click());

        ViewInteraction appCompatEditText10 = onView(
                allOf(withId(R.id.t4et),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText10.perform(click());

        ViewInteraction appCompatEditText11 = onView(
                allOf(withId(R.id.t4et),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText11.perform(replaceText("lose weights"), closeSoftKeyboard());

        pressBack();

        ViewInteraction appCompatCheckBox = onView(
                allOf(withId(R.id.Monday), withText("Monday"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatCheckBox.perform(click());

        ViewInteraction appCompatCheckBox2 = onView(
                allOf(withId(R.id.Wednesday), withText("Wednesday"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        appCompatCheckBox2.perform(click());

        ViewInteraction appCompatCheckBox3 = onView(
                allOf(withId(R.id.Friday), withText("Friday"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                6),
                        isDisplayed()));
        appCompatCheckBox3.perform(click());

        ViewInteraction appCompatButton5 = onView(
                allOf(withId(R.id.t4b), withText("NExt"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                10),
                        isDisplayed()));
        appCompatButton5.perform(click());

        ViewInteraction appCompatButton6 = onView(
                allOf(withId(R.id.t5b), withText("startdate"),
                        childAtPosition(
                                allOf(withId(R.id.relativeLayout),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatButton6.perform(click());

        ViewInteraction appCompatButton7 = onView(
                allOf(withId(R.id.calb2), withText("Button"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatButton7.perform(click());

        ViewInteraction appCompatButton8 = onView(
                allOf(withId(R.id.timeend), withText("enddate"),
                        childAtPosition(
                                allOf(withId(R.id.relativeLayout),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                2),
                        isDisplayed()));
        appCompatButton8.perform(click());

        ViewInteraction appCompatButton9 = onView(
                allOf(withId(R.id.calendb), withText("Button"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatButton9.perform(click());

        ViewInteraction appCompatButton10 = onView(
                allOf(withId(R.id.timenext), withText("next"),
                        childAtPosition(
                                allOf(withId(R.id.relativeLayout),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                4),
                        isDisplayed()));
        appCompatButton10.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.greeting), withText("Hey, lewis"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        textView.check(matches(withText("Hey, lewis")));

        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.add_habit_button),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                6),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction appCompatEditText12 = onView(
                allOf(withId(R.id.habit_title),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                6),
                        isDisplayed()));
        appCompatEditText12.perform(replaceText("running"), closeSoftKeyboard());

        ViewInteraction editText = onView(
                allOf(withId(R.id.habit_title), withText("running"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        editText.check(matches(withText("running")));

        ViewInteraction appCompatEditText13 = onView(
                allOf(withId(R.id.habit_reason),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                7),
                        isDisplayed()));
        appCompatEditText13.perform(click());

        ViewInteraction appCompatEditText14 = onView(
                allOf(withId(R.id.habit_reason),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                7),
                        isDisplayed()));
        appCompatEditText14.perform(replaceText("keep healthy"), closeSoftKeyboard());

        ViewInteraction editText2 = onView(
                allOf(withId(R.id.habit_reason), withText("keep healthy"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        editText2.check(matches(withText("keep healthy")));

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withId(R.id.start_date),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                9),
                        isDisplayed()));
        appCompatImageButton2.perform(click());

        ViewInteraction appCompatButton11 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        childAtPosition(
                                allOf(withClassName(is("android.widget.LinearLayout")),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                3)),
                                3),
                        isDisplayed()));
        appCompatButton11.perform(click());

        ViewInteraction editText3 = onView(
                allOf(withId(R.id.start_month), withText("11"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        editText3.check(matches(withText("11")));

        ViewInteraction editText4 = onView(
                allOf(withId(R.id.start_day), withText("5"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        editText4.check(matches(withText("5")));

        ViewInteraction editText5 = onView(
                allOf(withId(R.id.start_year), withText("2021"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        editText5.check(matches(withText("2021")));

        ViewInteraction appCompatImageButton3 = onView(
                allOf(withId(R.id.end_date),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        appCompatImageButton3.perform(click());

        ViewInteraction appCompatButton12 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        childAtPosition(
                                allOf(withClassName(is("android.widget.LinearLayout")),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                3)),
                                3),
                        isDisplayed()));
        appCompatButton12.perform(click());

        ViewInteraction editText6 = onView(
                allOf(withId(R.id.end_month), withText("11"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        editText6.check(matches(withText("11")));

        ViewInteraction editText7 = onView(
                allOf(withId(R.id.end_day), withText("10"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        editText7.check(matches(withText("10")));

        ViewInteraction editText8 = onView(
                allOf(withId(R.id.end_year), withText("2021"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        editText8.check(matches(withText("2021")));

        pressBack();

        ViewInteraction appCompatButton13 = onView(
                allOf(withId(R.id.monday), withText("M"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                17),
                        isDisplayed()));
        appCompatButton13.perform(click());

        ViewInteraction appCompatButton14 = onView(
                allOf(withId(R.id.wednesday), withText("W"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                23),
                        isDisplayed()));
        appCompatButton14.perform(click());

        ViewInteraction appCompatButton15 = onView(
                allOf(withId(R.id.wednesday), withText("W"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                23),
                        isDisplayed()));
        appCompatButton15.perform(click());

        ViewInteraction appCompatButton16 = onView(
                allOf(withId(R.id.friday), withText("F"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                18),
                        isDisplayed()));
        appCompatButton16.perform(click());

        ViewInteraction appCompatButton17 = onView(
                allOf(withId(R.id.friday), withText("F"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                18),
                        isDisplayed()));
        appCompatButton17.perform(click());

        ViewInteraction appCompatImageButton4 = onView(
                allOf(withId(R.id.confirm),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                25),
                        isDisplayed()));
        appCompatImageButton4.perform(click());

        ViewInteraction appCompatImageButton5 = onView(
                allOf(withId(R.id.add_habit_button),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                6),
                        isDisplayed()));
        appCompatImageButton5.perform(click());

        ViewInteraction appCompatEditText15 = onView(
                allOf(withId(R.id.habit_title),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                6),
                        isDisplayed()));
        appCompatEditText15.perform(replaceText("qq"), closeSoftKeyboard());

        pressBack();

        ViewInteraction appCompatImageButton6 = onView(
                allOf(withId(R.id.confirm),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                25),
                        isDisplayed()));
        appCompatImageButton6.perform(click());

        ViewInteraction view = onView(
                allOf(withParent(allOf(withId(android.R.id.content),
                        withParent(withId(R.id.action_bar_root)))),
                        isDisplayed()));
        view.check(matches(isDisplayed()));

        ViewInteraction appCompatEditText16 = onView(
                allOf(withId(R.id.habit_reason),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                7),
                        isDisplayed()));
        appCompatEditText16.perform(click());

        ViewInteraction appCompatEditText17 = onView(
                allOf(withId(R.id.habit_reason),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                7),
                        isDisplayed()));
        appCompatEditText17.perform(replaceText("q"), closeSoftKeyboard());

        pressBack();

        ViewInteraction appCompatImageButton7 = onView(
                allOf(withId(R.id.confirm),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                25),
                        isDisplayed()));
        appCompatImageButton7.perform(click());

        ViewInteraction view2 = onView(
                allOf(withParent(allOf(withId(android.R.id.content),
                        withParent(withId(R.id.action_bar_root)))),
                        isDisplayed()));
        view2.check(matches(isDisplayed()));

        ViewInteraction appCompatImageButton8 = onView(
                allOf(withId(R.id.start_date),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                9),
                        isDisplayed()));
        appCompatImageButton8.perform(click());

        ViewInteraction appCompatButton18 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        childAtPosition(
                                allOf(withClassName(is("android.widget.LinearLayout")),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                3)),
                                3),
                        isDisplayed()));
        appCompatButton18.perform(click());

        ViewInteraction appCompatImageButton9 = onView(
                allOf(withId(R.id.confirm),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                25),
                        isDisplayed()));
        appCompatImageButton9.perform(click());

        ViewInteraction view3 = onView(
                allOf(withParent(allOf(withId(android.R.id.content),
                        withParent(withId(R.id.action_bar_root)))),
                        isDisplayed()));
        view3.check(matches(isDisplayed()));

        ViewInteraction appCompatImageButton10 = onView(
                allOf(withId(R.id.end_date),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        appCompatImageButton10.perform(click());

        ViewInteraction appCompatButton19 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        childAtPosition(
                                allOf(withClassName(is("android.widget.LinearLayout")),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                3)),
                                3),
                        isDisplayed()));
        appCompatButton19.perform(click());

        ViewInteraction appCompatImageButton11 = onView(
                allOf(withId(R.id.confirm),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                25),
                        isDisplayed()));
        appCompatImageButton11.perform(click());

        ViewInteraction view4 = onView(
                allOf(withParent(allOf(withId(android.R.id.content),
                        withParent(withId(R.id.action_bar_root)))),
                        isDisplayed()));
        view4.check(matches(isDisplayed()));

        DataInteraction relativeLayout = onData(anything())
                .inAdapterView(allOf(withId(R.id.all_habits_list),
                        childAtPosition(
                                withId(R.id.frameLayout),
                                0)))
                .atPosition(0);
        relativeLayout.perform(click());

        ViewInteraction view5 = onView(
                allOf(withParent(allOf(withId(android.R.id.content),
                        withParent(withId(R.id.action_bar_root)))),
                        isDisplayed()));
        view5.check(matches(isDisplayed()));

        ViewInteraction editText9 = onView(
                allOf(withId(R.id.habit_title), withText("running"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        editText9.check(matches(withText("running")));

        ViewInteraction editText10 = onView(
                allOf(withId(R.id.habit_reason), withText("keep healthy"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        editText10.check(matches(withText("keep healthy")));

        ViewInteraction editText11 = onView(
                allOf(withId(R.id.start_month), withText("10"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        editText11.check(matches(withText("10")));

        ViewInteraction editText12 = onView(
                allOf(withId(R.id.start_day), withText("4"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        editText12.check(matches(withText("4")));

        ViewInteraction editText13 = onView(
                allOf(withId(R.id.start_year), withText("2021"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        editText13.check(matches(withText("2021")));

        ViewInteraction editText14 = onView(
                allOf(withId(R.id.end_month), withText("10"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        editText14.check(matches(withText("10")));

        ViewInteraction editText15 = onView(
                allOf(withId(R.id.end_day), withText("5"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        editText15.check(matches(withText("5")));

        ViewInteraction editText16 = onView(
                allOf(withId(R.id.end_year), withText("2021"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        editText16.check(matches(withText("2021")));

        ViewInteraction appCompatImageButton12 = onView(
                allOf(withId(R.id.back),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                24),
                        isDisplayed()));
        appCompatImageButton12.perform(click());

        ViewInteraction view6 = onView(
                allOf(withParent(allOf(withId(android.R.id.content),
                        withParent(withId(R.id.action_bar_root)))),
                        isDisplayed()));
        view6.check(matches(isDisplayed()));

        DataInteraction relativeLayout2 = onData(anything())
                .inAdapterView(allOf(withId(R.id.all_habits_list),
                        childAtPosition(
                                withId(R.id.frameLayout),
                                0)))
                .atPosition(1);
        relativeLayout2.perform(click());

        ViewInteraction editText17 = onView(
                allOf(withId(R.id.start_month), withText("10"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        editText17.check(matches(withText("10")));

        ViewInteraction editText18 = onView(
                allOf(withId(R.id.start_day), withText("4"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        editText18.check(matches(withText("4")));

        ViewInteraction editText19 = onView(
                allOf(withId(R.id.start_year), withText("2021"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        editText19.check(matches(withText("2021")));

        ViewInteraction appCompatImageButton13 = onView(
                allOf(withId(R.id.start_date),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                9),
                        isDisplayed()));
        appCompatImageButton13.perform(click());

        ViewInteraction appCompatButton20 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        childAtPosition(
                                allOf(withClassName(is("android.widget.LinearLayout")),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                3)),
                                3),
                        isDisplayed()));
        appCompatButton20.perform(click());

        ViewInteraction editText20 = onView(
                allOf(withId(R.id.start_month), withText("11"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        editText20.check(matches(withText("11")));

        ViewInteraction editText21 = onView(
                allOf(withId(R.id.start_day), withText("12"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        editText21.check(matches(withText("12")));

        ViewInteraction editText22 = onView(
                allOf(withId(R.id.start_year), withText("2021"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        editText22.check(matches(withText("2021")));

        ViewInteraction appCompatImageButton14 = onView(
                allOf(withId(R.id.confirm),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                25),
                        isDisplayed()));
        appCompatImageButton14.perform(click());

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.habit_name_text), withText("qq"),
                        withParent(withParent(withId(R.id.all_habits_list))),
                        isDisplayed()));
        textView2.check(matches(withText("qq")));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
