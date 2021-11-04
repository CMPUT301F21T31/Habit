package com.example.habit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserTests {

    // Declare mock users we will use in testing
    User mockUser1;
    User mockUser2;

    @BeforeEach
    void testSetup() {

        // Mock user with a few habit IDs, using constructor 1
        ArrayList<String> habits = new ArrayList<String>();
        habits.add("JBjAnWMkUzIYPPOxBmg6");
        habits.add("mEsBB2ddAmmWid33HWjF");
        mockUser1 = new User("Boby", "bob99", "bobsmith@gmail.com", habits);

        // Mock user with no habit IDs, using constructor 2
        mockUser2 = new User("Carla", "crox44", "carlathegreat@hotmail.com");
    }

    @Test
    void testGetDisplayName() {
        assertEquals("Boby", mockUser1.getDisplayName());
    }

    @Test
    void testSetDisplayName() {
        mockUser1.setDisplayName("Not Boby");
        assertNotEquals("Boby", mockUser1.getDisplayName());
        assertEquals("Not Boby", mockUser1.getDisplayName());
    }

    @Test
    void testGetUserName() {
        assertEquals("bob99", mockUser1.getUserName());
    }

    @Test
    void testSetUserName() {
        mockUser2.setUserName("xXCARLAXx");
        assertNotEquals("crox44", mockUser2.getUserName());
        assertEquals("xXCARLAXx", mockUser2.getUserName());
    }

    @Test
    void testGetEmail() {
        assertEquals("carlathegreat@hotmail.com", mockUser2.getEmail());
    }

    @Test
    void testSetEmail() {
        mockUser2.setEmail("carlaswag@telus.net");
        assertNotEquals("carlathegreat@hotmail.com", mockUser2.getEmail());
        assertEquals("carlaswag@telus.net", mockUser2.getEmail());
    }

    @Test
    void testGetHabits() {

        // Expected habits IDs
        ArrayList<String> habits = new ArrayList<String>();
        habits.add("JBjAnWMkUzIYPPOxBmg6");
        habits.add("mEsBB2ddAmmWid33HWjF");
        assertEquals(habits, mockUser1.getHabits());
        assertArrayEquals(habits.toArray(), mockUser1.getHabits().toArray());
        assertEquals(habits.get(1), mockUser1.getHabits().get(1));
    }
}
