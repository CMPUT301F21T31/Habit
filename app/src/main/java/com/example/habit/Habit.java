package com.example.habit;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Class denoting a single Habit, belonging to a single User object and having a list of HabitEvents
 */
public class Habit {

    private String title;
    private String reason;
    private Date start;
    private Date end;
    private HashMap<String, Boolean> daysOfWeek;
    private ArrayList<HabitEvent> events;

    /**
     * Constructor for a new habit TODO: Should some of these be optional?
     * @param title What this habit is called
     * @param reason Motivation/goal for this habit
     * @param start When the habit should start (just date, no time)
     * @param end When the habit should end (just date, no time)
     * @param daysOfWeek Hashmap with weekday keys and a boolean indicating if the habit occurs on that day
     * @param events Instances of this habit occurring
     */
    public Habit(String title, String reason, Date start, Date end, HashMap<String,
            Boolean> daysOfWeek, ArrayList<HabitEvent> events) {
        this.title = title;
        this.reason = reason;
        this.start = start;
        this.end = end;
        this.daysOfWeek = daysOfWeek;
        this.events = events;
    }

    /**
     * Habit constructor without events, will create Habit with empty events array
     */
    public Habit(String title, String reason, Date start, Date end, HashMap<String,
            Boolean> daysOfWeek) {
        this(title, reason, start, end, daysOfWeek, new ArrayList<HabitEvent>());
    }

    /**
     * @return Title of habit, e.g. Swimming or Reading
     */
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return Goal/motivation for this habit
     */
    public String getReason() {
        return reason;
    }

    /**
     * @param reason New goal/motivation for this habit
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * @return Date object containing start date for habit
     */
    public Date getStart() {
        return start;
    }

    /**
     * @param start Date object containing new start date for habit
     */
    public void setStart(Date start) {
        this.start = start;
    }

    /**
     * @return Date object containing end date for habit
     */
    public Date getEnd() {
        return end;
    }

    /**
     * @param end Date object containing new end date for habit
     */
    public void setEnd(Date end) {
        this.end = end;
    }

    /**
     * @return HashMap with weekday keys and boolean values which indicate if habit occurs that day
     * e.g. {"Monday": true, "Tuesday": false, ... ,"Sunday": true}
     */
    public Map<String, Boolean> getDaysOfWeek() {
        return daysOfWeek;
    }

    /**
     * @param daysOfWeek HashMap with new days of week this habit should occur
     */
    public void setDaysOfWeek(HashMap<String, Boolean> daysOfWeek) {
        // TODO: Should we do some verification that HashMap contains all 7 days?
        this.daysOfWeek = daysOfWeek;
    }

    /**
     * Helper function to convert a list of booleans to a days dictionary for habit constructor
     * @param days
     * @throws Exception
     */
    public static HashMap<String, Boolean> generateDaysDict(ArrayList<Boolean> days) throws Exception {

        // Must pass in array list of size 7 where each value indicates if habit will occur on that day
        if (days.size() != 7) {
            throw new Exception("Must pass in a list of booleans of size 7, one per day in order Monday-Sunday.");
        }

        // Default day value to false
        HashMap<String, Boolean> daysMap = new HashMap<String, Boolean>();
        daysMap.put("Monday", false);
        daysMap.put("Tuesday", false);
        daysMap.put("Wednesday", false);
        daysMap.put("Thursday", false);
        daysMap.put("Friday", false);
        daysMap.put("Saturday", false);
        daysMap.put("Sunday", false);

        // Set values
        for (int i = 0; i < days.size(); i++) {

            // Set variable for day if element matching it was set to true
            if (days.get(i)) {
                switch (i) {
                    case 0:
                        daysMap.put("Monday", true);
                    case 1:
                        daysMap.put("Tuesday", true);
                    case 2:
                        daysMap.put("Wednesday", true);
                    case 3:
                        daysMap.put("Thursday", true);
                    case 4:
                        daysMap.put("Friday", true);
                    case 5:
                        daysMap.put("Saturday", true);
                    case 6:
                        daysMap.put("Sunday", true);
                }
            }
        }
        return daysMap;
    }

    /**
     * @return Get all the HabitEvents for this Habit
     */
    public ArrayList<HabitEvent> getEvents() {
        return events;
    }

    /**
     * @param event HabitEvent object to add to this habit's event list
     */
    public void addEvent(HabitEvent event) {
        this.events.add(event);
    }

    /**
     * @param event Habit event to delte
     * @throws NoSuchElementException Thrown if event to delete is not in list
     */
    public void deleteEvent(HabitEvent event) throws NoSuchElementException {
        if (this.events.contains(event)) {
            this.events.remove(event);
        } else {
            throw new NoSuchElementException("Event to be deleted not in events list!");
        }
    }
}
