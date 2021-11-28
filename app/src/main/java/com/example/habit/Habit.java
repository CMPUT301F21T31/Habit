package com.example.habit;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
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
    private int completed;
    private Boolean ifPublic;
    private int listPosition;

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
            Boolean> daysOfWeek, ArrayList<String> events, Boolean ifPublic, int listPosition) {
        this.title = title;
        this.reason = reason;
        this.start = start;
        this.end = end;
        this.daysOfWeek = daysOfWeek;
        this.events = events;
        this.ifPublic = ifPublic;
        this.listPosition = listPosition;
    }


    /**
     * Habit constructor without events, will create Habit with empty events array
     */
    public Habit(String title, String reason, Date start, Date end, HashMap<String,
            Boolean> daysOfWeek, Boolean ifPublic, int listPosition) {
        this(title, reason, start, end, daysOfWeek, new ArrayList<String>(), ifPublic, listPosition);
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
    @RequiresApi(api = Build.VERSION_CODES.Q)
    protected Habit(Parcel in) {
        this.title = in.readString();
        this.reason = in.readString();
        this.start = new Date(in.readLong());
        this.end = new Date(in.readLong());
        this.habitId = in.readString();
        this.userId = in.readString();
        this.ifPublic = in.readBoolean();
        Bundle b1 = in.readBundle();
        this.daysOfWeek = (HashMap<String, Boolean>)b1.getSerializable("HashMap");
        Bundle b2 = in.readBundle();
        this.events = b2.getStringArrayList("Events");
        this.completed = in.readInt();
    }

    /**
     * CREATOR field required for Parcelable
     */
    public static final Creator<Habit> CREATOR = new Creator<Habit>() {
        @RequiresApi(api = Build.VERSION_CODES.Q)
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
     * @return int describing contents
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Write Habit properties to parcel, must read in same order.
     * @param dest destination parcel to send
     * @param flags parcel flags
     */
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(reason);
        dest.writeLong(start.getTime());
        dest.writeLong(end.getTime());
        dest.writeString(habitId);
        dest.writeString(userId);
        dest.writeBoolean(ifPublic);
        Bundle b1 = new Bundle();
        b1.putSerializable("HashMap", daysOfWeek);
        dest.writeBundle(b1);
        Bundle b2 = new Bundle();
        b2.putStringArrayList("Events", events);
        dest.writeBundle(b2);
        dest.writeInt(completed);
    }

    /**
     * Get title of habit, e.g. Swimming or Reading
     * @return String containing title
     */
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Reason (motivation) for this habit
     * @return String containing reason
     */
    public String getReason() {
        return reason;
    }

    /**
     * Set new reason (motivation) for this habit
     * @param reason String with new reason for this habit
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * Get date this habit started
     * @return Date object containing start date for habit
     */
    public Date getStart() {
        return start;
    }

    /**
     * Set new start date for habit
     * @param start Date object containing new start date for habit
     */
    public void setStart(Date start) {
        this.start = start;
    }

    /**
     * Get date this habit ended
     * @return Date object containing end date for habit
     */
    public Date getEnd() {
        return end;
    }

    /**
     * Set new end date for habit
     * @param end Date object containing new end date for habit
     */
    public void setEnd(Date end) {
        this.end = end;
    }

    /**
     * Retrieve habit ID, used in database methods
     * @return String containing habit ID
     */
    public String getHabitId() {
        return habitId;
    }

    /**
     * Set the habit ID from firestore for this habit, can only set if not already
     * @param habitId String containing habitId
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
     * Retrieve user ID for user that this habit belongs to
     * @return String containing userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Set the user ID belonging to this habit, should not be changed and should only be one
     * @param userId String id to set
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
     * Set days of week this habit should occur
     * @param daysOfWeek Hashmap of days, e.g. {"Monday": true, ... ,"Sunday": true}
     */
    public void setDaysOfWeek(HashMap<String, Boolean> daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    public Boolean getIfPublic() {
        return ifPublic;
    }

    public void setIfPublic(Boolean ifPublic) {
        this.ifPublic = ifPublic;
    }

    public int getListPosition() {
        return listPosition;
    }

    public void setListPosition(int listPosition) {
        this.listPosition = listPosition;
    }

    /**
     * Helper function to convert a list of booleans to a days dictionary for habit constructor
     * @param days List of booleans in order of Monday, Tuesday, ... Sunday e.g. (True, ... , False)
     * @throws Exception Thrown if days array passed in is not of length 7
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
     * Check if habit occurs on certain day
     * @param weekday Day of week string, e.g. "Monday"
     * @return Boolean, false if day of week doesn't exist in hashmap
     */
    public Boolean isOnDay(String weekday) {
        Boolean result = daysOfWeek.get(weekday);
        return result != null && result;
    }

    /**
     * Get all the HabitEvents IDs for this Habit
     * @return ArrayList of string event IDs for this habit
     */
    public ArrayList<String> getEvents() {
        return events;
    }

    public int getCompleted() {
        return completed;
    }

    public void addCompleted() {
        completed++;
    }

    //    public Boolean completedToday() {
//
//    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private int totalPlanned() {

        int planned = 0;

        // Convert dates to local dates
        LocalDate s = start.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        LocalDate e = end.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        // Loop through all days between start and end, incrementing counter
        LocalDate localDate = s;
        while (localDate.isBefore(e)) {
            DayOfWeek dow = localDate.getDayOfWeek();
            if (this.isOnDay(dow.getDisplayName(TextStyle.FULL, Locale.getDefault()))) {
                planned++;
            }
        }

        return planned;
    }

    private int totalCompleted() {
        // TODO: Maybe we should just store this and increment when a habit is completed?
        return 0;
    }

    /* Firestore Methods */

    /**
     * Add a event object to the habitEvents collection and add a reference to the habitEvent in a
     * habit's events list.
     * @param habitId String id of the habit to add event to
     * @param event HabitEvent object to add
     */
    public static void addEvent(String habitId, HabitEvent event) {
        String eventId = addEventToEvents(event, habitId);
        addEventToHabit(habitId, eventId);
    }

    /**
     * Add a event ID to the list of event IDs for a particular habit
     * @param habitId String id of Habit to add event ID to
     * @param eventId String id of HabitEvent to add
     */
    private static void addEventToHabit(String habitId, String eventId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference thisHabit = db.collection("habits").document(habitId);
        thisHabit.update("events", FieldValue.arrayUnion(eventId));
    }

    /**
     * Add HabitEvent object to the HabitEvents collection
     * @param event HabitEvent object to add
     * @param habitId String id of parent habit
     * @return String ID of event added
     */
    private static String addEventToEvents(HabitEvent event, String habitId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference newEvent = db.collection("habitEvents").document();
        event.setHabitEventId(newEvent.getId());
        event.setHabitId(habitId);
        newEvent.set(event);
        return newEvent.getId();
    }

    /**
     * Delete event from events collection and events list for a habit
     * @param habitId String containing habit ID to delete event from
     * @param eventId String containing event ID to delete
     */
    public static void deleteEvent(String habitId, String eventId) {
        deleteEventFromEvents(eventId);
        deleteEventFromHabit(habitId, eventId);
    }

    /**
     * Update an existing habitEvent in DB
     * @param habitEventId
     * @param habitEvent
     */
    public static void updateHabitEvent(String habitEventId, HabitEvent habitEvent) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("habitEvents")
                .document(habitEventId)
                .set(habitEvent);
    }

    /**
     * Delete event from events collection
     * @param eventId String ID of HabitEvent to delete from collection
     */
    private static void deleteEventFromEvents(String eventId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("habitEvents").document(eventId).delete();
    }

    /**
     * Delete event ID from events list for a habit
     * @param habitId String Habit ID to delete HabitEvent ID from
     * @param eventId String Habit Event ID to delete
     */
    private static void deleteEventFromHabit(String habitId, String eventId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference thisUser = db.collection("habits").document(habitId);
        thisUser.update("events", FieldValue.arrayRemove(eventId));
    }
}