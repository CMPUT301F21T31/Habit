package com.example.habit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class HabitEventTests {

    HabitEvent mockHabitEvent1;
    HabitEvent mockHabitEvent2;

    @BeforeEach
    void setup() {
        // Instantiate mock habit events
        mockHabitEvent1 = new HabitEvent("Edmonton", "Swam 20 laps");
        mockHabitEvent2 = new HabitEvent("Library", "Finished chapter 7");
    }

    @Test
    void getLocation() {
        assertEquals("Edmonton", mockHabitEvent1.getLocation());
    }

    @Test
    void setLocation() {
        mockHabitEvent1.setLocation("Calgary");
        assertNotEquals("Edmonton", mockHabitEvent1.getLocation());
        assertEquals("Calgary", mockHabitEvent1.getLocation());
    }

    @Test
    void getComments() {
        assertEquals("Swam 20 laps", mockHabitEvent1.getComments());
    }

    @Test
    void setComments() {
        mockHabitEvent2.setComments("I love this book");
        assertNotEquals("Finished chapter 7", mockHabitEvent2.getComments());
        assertEquals("I love this book", mockHabitEvent2.getComments());
    }

    @Test
    void getHabitId() {
        assertNull(mockHabitEvent2.getHabitId());
    }

    @Test
    void setHabitId() {
        mockHabitEvent2.setHabitId("jlk5432lkjf");
        assertNotNull(mockHabitEvent2.getHabitId());
        assertEquals("jlk5432lkjf", mockHabitEvent2.getHabitId());

        // User ID shouldn't change once it is set
        mockHabitEvent2.setHabitId("fasfsafklj3");
        assertNotEquals("fasfsafklj3", mockHabitEvent2.getHabitId());
        assertEquals("jlk5432lkjf", mockHabitEvent2.getHabitId());
    }

    @Test
    void getHabitEventId() {
        assertNull(mockHabitEvent2.getHabitEventId());
    }

    @Test
    void setHabitEventId() {
        mockHabitEvent2.setHabitEventId("jlk5432lkjf");
        assertNotNull(mockHabitEvent2.getHabitEventId());
        assertEquals("jlk5432lkjf", mockHabitEvent2.getHabitEventId());

        // User ID shouldn't change once it is set
        mockHabitEvent2.setHabitEventId("fasfsafklj3");
        assertNotEquals("fasfsafklj3", mockHabitEvent2.getHabitEventId());
        assertEquals("jlk5432lkjf", mockHabitEvent2.getHabitEventId());
    }
}
