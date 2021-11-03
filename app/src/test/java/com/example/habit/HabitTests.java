package com.example.habit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HabitTests {

    @Test
    public void testGenerateDaysDict() throws Exception {

        ArrayList<Boolean> allDays = new ArrayList<Boolean>(Arrays.asList(true, true, true, true, true, true, true));
        ArrayList<Boolean> noDays = new ArrayList<Boolean>(Arrays.asList(false, false, false, false, false, false, false));
        ArrayList<Boolean> oneDay = new ArrayList<Boolean>(Arrays.asList(false, false, false, true, false, false, false));
        ArrayList<Boolean> manyDays = new ArrayList<Boolean>(Arrays.asList(true, false, false, true, true, true, false));

        HashMap<String, Boolean> allDaysExpected = new HashMap<String, Boolean>(Map.of(
                "Monday", true,
                "Tuesday", true,
                "Wednesday", true,
                "Thursday", true,
                "Friday", true,
                "Saturday", true,
                "Sunday", true
        ));

        HashMap<String, Boolean> noDaysExpected = new HashMap<String, Boolean>(Map.of(
                "Monday", false,
                "Tuesday", false,
                "Wednesday", false,
                "Thursday", false,
                "Friday", false,
                "Saturday", false,
                "Sunday", false
        ));

        HashMap<String, Boolean> oneDayExpected = new HashMap<String, Boolean>(Map.of(
                "Monday", false,
                "Tuesday", false,
                "Wednesday", false,
                "Thursday", true,
                "Friday", false,
                "Saturday", false,
                "Sunday", false
        ));

        HashMap<String, Boolean> manyDaysExpected = new HashMap<String, Boolean>(Map.of(
                "Monday", true,
                "Tuesday", false,
                "Wednesday", false,
                "Thursday", true,
                "Friday", true,
                "Saturday", true,
                "Sunday", false
        ));

        HashMap<String, Boolean> result1 = Habit.generateDaysDict(allDays);
        HashMap<String, Boolean> result2 = Habit.generateDaysDict(noDays);
        HashMap<String, Boolean> result3 = Habit.generateDaysDict(oneDay);
        HashMap<String, Boolean> result4 = Habit.generateDaysDict(manyDays);

        System.out.println(result1.toString());
        System.out.println(result2.toString());
        System.out.println(result3.toString());
        System.out.println(result4.toString());

        assertEquals(result1, allDaysExpected);
        assertEquals(result2, noDaysExpected);
        assertEquals(result3, oneDayExpected);
        assertEquals(result4, manyDaysExpected);

        assertEquals(1, 1);
    }
}
