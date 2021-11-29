package com.example.habit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.habit.entities.Habit;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class HabitTests {

    Habit mockHabit1;
    Habit mockHabit2;
    Date start1;
    Date start2;
    Date end1;
    Date end2;
    HashMap<String, Boolean> daysOfWeek1;
    HashMap<String, Boolean> daysOfWeek2;
    ArrayList<String> events;

    @BeforeAll
    void setup() {

        // Mock habit event strings
        events = new ArrayList<String>();
        events.add("3WamsQqMlSlEL7ONVsuA");
        events.add("FMsioS8GjR4bjD3nbQbh");

        // Mock occurence maps
        try {
            daysOfWeek1 = Habit.generateDaysDict(new ArrayList<Boolean>(
                    Arrays.asList(false, true, false, false, true, false, false)));
            daysOfWeek2 = Habit.generateDaysDict(new ArrayList<Boolean>(
                    Arrays.asList(true, false, false, true, false, false, true)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Mock dates
        LocalDate d1 = LocalDate.parse("2021-11-01");
        LocalDate d2 = LocalDate.parse("2022-06-07");
        LocalDate d3 = LocalDate.parse("2022-10-21");
        start1 = new Date();
        start2 = Date.from(d1.atStartOfDay(ZoneId.systemDefault()).toInstant());
        end1 = Date.from(d2.atStartOfDay(ZoneId.systemDefault()).toInstant());
        end2 = Date.from(d3.atStartOfDay(ZoneId.systemDefault()).toInstant());


        // Create mock habits for testing using constructor with and without events
        mockHabit1 = new Habit("Swimming", "Get fit", start1, end1, daysOfWeek1, events, true, 0);
        mockHabit2 = new Habit("Reading", "Learn", start1, end1, daysOfWeek2, true, 0);
    }

    @Test
    void testGenerateDaysDict() throws Exception {

        ArrayList<Boolean> allDays = new ArrayList<Boolean>(Arrays.asList(true, true, true, true, true, true, true));
        ArrayList<Boolean> noDays = new ArrayList<Boolean>(Arrays.asList(false, false, false, false, false, false, false));
        ArrayList<Boolean> oneDay = new ArrayList<Boolean>(Arrays.asList(false, false, false, true, false, false, false));
        ArrayList<Boolean> manyDays = new ArrayList<Boolean>(Arrays.asList(true, false, false, true, true, true, false));

        HashMap<String, Boolean> allDaysExpected = new HashMap<String, Boolean>();
        allDaysExpected.put("Monday", true);
        allDaysExpected.put("Tuesday", true);
        allDaysExpected.put("Wednesday", true);
        allDaysExpected.put("Thursday", true);
        allDaysExpected.put("Friday", true);
        allDaysExpected.put("Saturday", true);
        allDaysExpected.put("Sunday", true);

        HashMap<String, Boolean> noDaysExpected = new HashMap<String, Boolean>();
        noDaysExpected.put("Monday", false);
        noDaysExpected.put("Tuesday", false);
        noDaysExpected.put("Wednesday", false);
        noDaysExpected.put("Thursday", false);
        noDaysExpected.put("Friday", false);
        noDaysExpected.put("Saturday", false);
        noDaysExpected.put("Sunday", false);

        HashMap<String, Boolean> oneDayExpected = new HashMap<String, Boolean>();
        oneDayExpected.put("Monday", false);
        oneDayExpected.put("Tuesday", false);
        oneDayExpected.put("Wednesday", false);
        oneDayExpected.put("Thursday", true);
        oneDayExpected.put("Friday", false);
        oneDayExpected.put("Saturday", false);
        oneDayExpected.put("Sunday", false);

        HashMap<String, Boolean> manyDaysExpected = new HashMap<String, Boolean>();
        manyDaysExpected.put("Monday", true);
        manyDaysExpected.put("Tuesday", false);
        manyDaysExpected.put("Wednesday", false);
        manyDaysExpected.put("Thursday", true);
        manyDaysExpected.put("Friday", true);
        manyDaysExpected.put("Saturday", true);
        manyDaysExpected.put("Sunday", false);

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
    }

    @Test
    void getTitle() {
        assertEquals("Swimming", mockHabit1.getTitle());
    }

    @Test
    void setTitle() {
        mockHabit2.setTitle("Gymnastics");
        assertNotEquals("Reading", mockHabit2.getTitle());
        assertEquals("Gymnastics", mockHabit2.getTitle());
    }

    @Test
    void getReason() {
        assertEquals("Get fit", mockHabit1.getReason());
    }

    @Test
    void setReason() {
        mockHabit2.setReason("For fun");
        assertNotEquals("Get fit", mockHabit2.getReason());
        assertEquals("For fun", mockHabit2.getReason());
    }

    @Test
    void getStart() {
        assertEquals(start1, mockHabit1.getStart());
    }

    @Test
    void setStart() {
        mockHabit2.setStart(start2);
        assertNotEquals(start1, mockHabit2.getStart());
        assertEquals(start2, mockHabit2.getStart());
    }

    @Test
    void getEnd() {
        assertEquals(end1, mockHabit1.getEnd());
    }

    @Test
    void setEnd() {
        mockHabit2.setEnd(end2);
        assertNotEquals(end1, mockHabit2.getEnd());
        assertEquals(end2, mockHabit2.getEnd());
    }

    @Test
    void getHabitId() {
        assertNull(mockHabit1.getHabitId());
    }

    @Test
    void setHabitId() {
        mockHabit2.setHabitId("jlk5432lkjf");
        assertNotNull(mockHabit2.getHabitId());
        assertEquals("jlk5432lkjf", mockHabit2.getHabitId());

        // Habit ID shouldn't change once it is set
        mockHabit2.setHabitId("fasfsafklj3");
        assertNotEquals("fasfsafklj3", mockHabit2.getHabitId());
        assertEquals("jlk5432lkjf", mockHabit2.getHabitId());
    }

    @Test
    void getUserId() {
        assertNull(mockHabit1.getUserId());
    }

    @Test
    void setUserId() {
        mockHabit2.setUserId("jlk5432lkjf");
        assertNotNull(mockHabit2.getUserId());
        assertEquals("jlk5432lkjf", mockHabit2.getUserId());

        // User ID shouldn't change once it is set
        mockHabit2.setUserId("fasfsafklj3");
        assertNotEquals("fasfsafklj3", mockHabit2.getUserId());
        assertEquals("jlk5432lkjf", mockHabit2.getUserId());
    }

    @Test
    void getDaysOfWeek() {
        assertEquals(daysOfWeek1, mockHabit1.getDaysOfWeek());
    }

    @Test
    void setDaysOfWeek() {
        mockHabit2.setDaysOfWeek(daysOfWeek2);
        assertNotEquals(daysOfWeek1, mockHabit2.getDaysOfWeek());
        assertEquals(daysOfWeek2, mockHabit2.getDaysOfWeek());
    }

    @Test
    void isOnDay() {
        assertTrue(mockHabit2.isOnDay("Monday"));
        assertTrue(mockHabit2.isOnDay("Thursday"));
        assertFalse(mockHabit2.isOnDay("Saturday"));
        assertFalse(mockHabit2.isOnDay("notAValidDay"));
    }

    @Test
    void getEvents() {
        assertEquals(events, mockHabit1.getEvents());
    }
}
