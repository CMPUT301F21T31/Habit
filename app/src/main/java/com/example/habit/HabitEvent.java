package com.example.habit;

/**
 * Class denoting an instance (event) of a particular Habit
 */
public class HabitEvent {

    // private photo // TODO: How should we store photos?
    private String location;
    private String comments;

    /**
     *
     * @param location
     * @param comments
     */
    public HabitEvent(String location, String comments) {
        this.location = location;
        this.comments = comments;
    }

    /**
     * @return Get location of this habit event
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location New location for this habit event
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return Get the comments for this habit event
     */
    public String getComments() {
        return comments;
    }

    /**
     * @param comments New comments for this habit event
     */
    public void setComments(String comments) {
        this.comments = comments;
    }
}
