package com.example.habit;

import android.util.Log;

/**
 * Class denoting an instance (event) of a particular Habit
 */
public class HabitEvent {

    // private photo // TODO: How should we store photos?
    private String location; // TODO: Will have to change this to store an actual location
    private String comments;
    private String habitId;
    private String habitEventId;
    private int state; // 1=pending, 2=complete, 3=incomplete

    /**
     *
     * @param location String location for this HabitEvent
     * @param comments String comments for this HabitEvent
     */
    public HabitEvent(String location, String comments) {
        this.location = location;
        this.comments = comments;
    }

    /**
     *
     * @param location String location for this HabitEvent
     * @param comments String comments for this HabitEvent
     * @param state String state for this HabitEvent, 1=pending, 2=complete, 3=incomplete
     */
    public HabitEvent(String location, String comments, int state) {
        this.location = location;
        this.comments = comments;

        // Default state to "pending" if invalid one is passed in
        if (state < 1 || state > 3) {
            this.state = 1;
        } else {
            this.state = state;
        }
    }

    public HabitEvent() {}

    /** Get location of this habit event
     * @return String location
     */
    public String getLocation() {
        return location;
    }

    /** Set location for this habit event
     * @param location String location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Get the comments for this habit event
     * @return String comments
     */
    public String getComments() {
        return comments;
    }

    /** Set comments string for this habit event
     * @param comments String comments
     */
    public void setComments(String comments) {
        this.comments = comments;
    }

    /**
     * Get the habitId for this HabitEvent
     * @return String ID corresponding to that in firestore
     */
    public String getHabitId() {
        return habitId;
    }

    /**
     * Set habitId for this HabitEvent if not already set
     * @param habitId String ID corresponding to that in firestore
     */
    public void setHabitId(String habitId) {
        if (this.habitId != null) {
            Log.e("USER ID SET ERROR", "Should not set the habitId of HabitEvent which already has an ID");
        } else {
            this.habitId = habitId;
        }
    }

    /**
     * Get the habitEventId of this habitEvent
     * @return String ID corresponding to that in firestore
     */
    public String getHabitEventId() {
        return habitEventId;
    }

    /**
     * Set habitEventId if not already set
     * @param habitEventId String ID corresponding to that in firestore
     */
    public void setHabitEventId(String habitEventId) {
        if (this.habitEventId != null) {
            Log.e("HABIT EVENT ID SET", "Should not set habitEventId of HabitEvent which already has an ID");
        } else {
            this.habitEventId = habitEventId;
        }
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        if (state > 0 && state < 4) {
            this.state = state;
        }
    }
}
