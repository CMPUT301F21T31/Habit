package com.example.habit.entities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

/**
 * Class denoting an instance (event) of a particular Habit
 * @author Justin
 */
public class HabitEvent {

    private Double latitude;
    private Double longitude;
    private String comments;
    private String habitId;
    private String habitEventId;
    private int state; // 1=pending, 2=complete, 3=incomplete
    private String photoPath;

    /**
     * Create a new HabitEvent with latitude, longitude, and comment
     * @param latitude Double representing latitude of where habit event occurred
     * @param longitude Double representing longitude of where habit event occurred
     * @param comment String comment for this habit event
     */
    public HabitEvent(Double latitude, Double longitude, String comment) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.comments = comment;
    }

    /**
     *
     * @param comments String comment for this habit event
     * @param state State of this habit event
     */
    public HabitEvent(String comments, int state) {
        this.latitude = null;
        this.longitude = null;
        this.comments = comments;

        // Default state to "pending" if invalid one is passed in
        if (state < 1 || state > 3) {
            this.state = 1;
        } else {
            this.state = state;
        }
    }

    public HabitEvent() {}

    /**
     * Get the latitude of the selected location
     * @return Double containing latitude
     */
    public Double getLatitude() {
        return latitude;
    }

    /**
     * Set the latitude of the selected location
     * @param latitude
     */
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    /**
     * Get the longitude of the selected location
     * @return Double containing longitude
     */
    public Double getLongitude() {
        return longitude;
    }

    /**
     * Set the longitude of the selected location
     * @param longitude
     */
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
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
     * @return String ID corresponding to that in firebase
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

    /**
     * Get the state of the current habitEvent
     * @return int containing state
     */
    public int getState() {
        return state;
    }

    /**
     * Set the state of the current habitEvent
     * @param state
     */
    public void setState(int state) {
        if (state > 0 && state < 4) {
            this.state = state;
        }
    }

    /**
     * Get the photoPath of selected image
     * @return string containing photoPath
     */
    public String getPhotoPath() {
        return photoPath;
    }

}
