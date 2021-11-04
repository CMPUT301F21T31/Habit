package com.example.habit;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Class denoting a single Habit, belonging to a single User object and having a list of HabitEvents
 */
public class Habit implements Parcelable {

    private String title;
    private String reason;
    private Date start;
    private Date end;
    private HashMap<String, Boolean> daysOfWeek;
    private ArrayList<String> events;
    private String habitId;
    private String userId;

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
            Boolean> daysOfWeek, ArrayList<String> events) {
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
        this(title, reason, start, end, daysOfWeek, new ArrayList<String>());
    }

    /**
     * No arg constructors allows us to store and retrieve Habit objects from firebase
     */
    public Habit() {}

    /* Parcel Methods */

    /**
     * Constructor building Habit object from Parcel
     * @param in Parcel passed through intent, containing a Habit object
     */
    protected Habit(Parcel in) {
        title = in.readString();
        reason = in.readString();
        start = new Date(in.readLong());
        end = new Date(in.readLong());

        Bundle bundle = in.readBundle();
        daysOfWeek = (HashMap<String, Boolean>)bundle.getSerializable("HashMap");

        events = in.createStringArrayList();
        habitId = in.readString();
        userId = in.readString();
    }

    /**
     * CREATOR field required for Parcelable
     */
    public static final Creator<Habit> CREATOR = new Creator<Habit>() {
        @Override
        public Habit createFromParcel(Parcel source) {
            return new Habit(source);
        }

        @Override
        public Habit[] newArray(int size) {
            return new Habit[size];
        }
    };

    /**
     * Required by Parcelable but not used for our application
     * @return
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Write Habit properties to parcel, must read in same order.
     * @param dest
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(reason);
        dest.writeLong(start.getTime());
        dest.writeLong(end.getTime());

        Bundle bundle = new Bundle();
        bundle.putSerializable("HashMap", daysOfWeek);
        dest.writeBundle(bundle);

        dest.writeList(events);
        dest.writeString(habitId);
        dest.writeString(userId);
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
     * Retrieve habit ID
     * @return
     */
    public String getHabitId() {
        return habitId;
    }

    /**
     * Set the habit ID from firestore for this habit
     * @param habitId
     */
    public void setHabitId(String habitId) {
        // Should only set habitId when it is null
        if (this.habitId != null) {
            Log.e("HABIT SET ERROR", "Should not set habitId of habit which already has an ID");
        } else {
            this.habitId = habitId;
        }
    }

    /**
     * Retrieve user ID
     * @return
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Set the user ID from firestore belonging to this habit, should not be changed and should only be one
     * @param userId
     */
    public void setUserId(String userId) {
        if (this.userId != null) {
            Log.e("HABIT SET ERROR", "Should not set userId of habit which already has an ID");
        } else {
            this.userId = userId;
        }
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
                        break;
                    case 1:
                        daysMap.put("Tuesday", true);
                        break;
                    case 2:
                        daysMap.put("Wednesday", true);
                        break;
                    case 3:
                        daysMap.put("Thursday", true);
                        break;
                    case 4:
                        daysMap.put("Friday", true);
                        break;
                    case 5:
                        daysMap.put("Saturday", true);
                        break;
                    case 6:
                        daysMap.put("Sunday", true);
                        break;
                }
            }
        }
        return daysMap;
    }

    /**
     * @return Get all the HabitEvents for this Habit
     */
    public ArrayList<String> getEvents() {
        return events;
    }

    /**
     * Add a event object to the habitEvents collection and add a reference to the habitEvent in a
     * habit's events list.
     * @param habitId
     * @param event
     */
    public static void addEvent(String habitId, HabitEvent event) {
        String eventId = addEventToEvents(event);
        addEventToHabit(habitId, eventId);
    }

    /**
     * Add a event ID to the list of event IDs for a particular habit
     * @param habitId
     * @param eventId
     */
    private static void addEventToHabit(String habitId, String eventId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference thisHabit = db.collection("habits").document(habitId);
        thisHabit.update("events", FieldValue.arrayUnion(eventId));
    }

    /**
     * Add HabitEvent object to the HabitEvents collection
     * @param event
     * @return
     */
    private static String addEventToEvents(HabitEvent event) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference newEvent = db.collection("habitEvents").document();
        newEvent.set(event);
        return newEvent.getId();
    }

    /**
     * Delete event from events collection and events list for a habit
     * @param habitId
     * @param eventId
     */
    public static void deleteEvent(String habitId, String eventId) {
        deleteEventFromEvents(eventId);
        deleteEventFromHabit(habitId, eventId);
    }

    /**
     * Delete event from events collection
     * @param eventId
     */
    private static void deleteEventFromEvents(String eventId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("habitEvents").document(eventId).delete();
    }

    /**
     * Delete event ID from events list for a habit
     * @param habitId
     * @param eventId
     */
    private static void deleteEventFromHabit(String habitId, String eventId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference thisUser = db.collection("habits").document(habitId);
        thisUser.update("events", FieldValue.arrayRemove(eventId));
    }
}