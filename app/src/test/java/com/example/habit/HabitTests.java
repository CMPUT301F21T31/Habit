package com.example.habit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class HabitTests {

    Habit mockHabit1;
    Habit mockHabit2;
    Date now;
    Date later;
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
        now = new Date();
        LocalDate d = LocalDate.parse("2022-11-01");
        later = Date.from(d.atStartOfDay(ZoneId.systemDefault()).toInstant());

        // Create mock habits for testing using constructor with and without events
        mockHabit1 = new Habit("Swimming", "Get fit", now, later, daysOfWeek1, events);
        mockHabit2 = new Habit("Reading", "Learn", now, later, daysOfWeek2);

    }

    @Test
    void testGenerateDaysDict() throws Exception {

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
        assertNotEquals("Get fit", mockHabit2);
        assertEquals("For fun", mockHabit2);
    }

    @Test
    void getStart() {
        assertEquals(now, mockHabit1.getStart());
    }

//    @Test
//    void setStart() {
//        mockHabit2.setStart();
//        assertNotEquals("", mockHabit2);
//        assertEquals("", mockHabit2);
//    }

    @Test
    void getEnd() {
        assertEquals(later, mockHabit1.getEnd());
    }

//    @Test
//    void setEnd() {
//        mockHabit2.set();
//        assertNotEquals("", mockHabit2);
//        assertEquals("", mockHabit2);
//    }

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

//    @Test
//    void setUserId() {
//        mockHabit2.set();
//        assertNotEquals("", mockHabit2);
//        assertEquals("", mockHabit2);
//    }

    @Test
    void getDaysOfWeek() {
        assertEquals(daysOfWeek1, mockHabit1.getDaysOfWeek());
    }

//    @Test
//    void setDaysOfWeek() {
//        mockHabit2.set();
//        assertNotEquals("", mockHabit2);
//        assertEquals("", mockHabit2);
//    }

    @Test
    void isOnDay() {
    }

    @Test
    void getEvents() {
        assertEquals(events, mockHabit1.getEvents());
    }
}
