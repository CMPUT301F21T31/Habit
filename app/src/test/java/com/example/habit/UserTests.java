package com.example.habit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.habit.entities.User;

import java.util.ArrayList;

/**
 * Tests the functionality of all getter and setter methods, and constructors
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserTests {

    // Declare mock users we will use in testing
    User mockUser1;
    User mockUser2;
    ArrayList<String> mock_user_ids1;
    ArrayList<String> mock_user_ids2;

    @BeforeEach
    void testSetup() {

        // Mock other user IDs for use in follower/following/requests testing
        mock_user_ids1 = new ArrayList<String>();
        mock_user_ids1.add("uuid1");
        mock_user_ids1.add("uuid2");
        mock_user_ids1.add("uuid3");
        mock_user_ids2 = new ArrayList<String>();
        mock_user_ids2.add("uuid1");
        mock_user_ids2.add("uuid2");
        mock_user_ids2.add("uuid3");

        // Mock user with a few habit IDs plus following, followers, and requests lists, using
        // constructor 1
        ArrayList<String> habits = new ArrayList<String>();
        habits.add("JBjAnWMkUzIYPPOxBmg6");
        habits.add("mEsBB2ddAmmWid33HWjF");
        mockUser1 = new User("Boby", "bobsmith@gmail.com", "test_uuid",
                true, habits, mock_user_ids1, mock_user_ids1, mock_user_ids1);

        // Mock user with no habit IDs, using constructor 2
        mockUser2 = new User("Carla", "carlathegreat@hotmail.com",
                "test_uuid", true);
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

    @Test
    void testSetHabits() {
        // Habit IDs to add
        ArrayList<String> habits = new ArrayList<String>();
        habits.add("JBjAnWMkUzIYPPOxBmg6");
        habits.add("mEsBB2ddAmmWid33HWjF");

        mockUser1.setHabits(habits);
        assertEquals(habits, mockUser1.getHabits());
    }

    @Test
    void testGetUuid() {
        assertEquals("test_uuid", mockUser1.getUuid());
    }

    @Test
    void testGetStayLoggedIn() {
        assertTrue(mockUser1.getStayLoggedIn());
    }

    @Test
    void testSetStayLoggedIn() {
        mockUser1.setStayLoggedIn(false);
        assertFalse(mockUser1.getStayLoggedIn());
    }

    @Test
    void testGetFollowers() {
        assertEquals(mockUser1.getFollowers(), mock_user_ids1);
    }

    @Test
    void testGetFollowing() {
        assertEquals(mockUser1.getFollowing(), mock_user_ids1);
    }

    @Test
    void testGetRequests() {
        assertEquals(mockUser1.getRequests(), mock_user_ids1);
    }
}
