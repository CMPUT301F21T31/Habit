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
 * @author justin
 * @see android.app.Activity
 * @see android.content.Context
 */
public class HabitEvent {

    // private photo // TODO: How should we store photos?
    private Double latitude;
    private Double longitude;
    private String comments;
    private String habitId;
    private String habitEventId;
    private int state; // 1=pending, 2=complete, 3=incomplete
    private String photoPath;

    /**
     * new class to temporary store a habit event object
     * @param latitude
     * @param longitude
     * @param comment
     */
    public HabitEvent(Double latitude, Double longitude, String comment) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.comments = comment;
    }

    /**
     *
     * @param comments
     * @param state
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

    /**
     * Set the photoPath of selected image
     * @param photoPath
     */
    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    /**
     * This method is storing image to the database
     * @param habitEventId
     * @param bitmap
     */
    public static void storeImage(String habitEventId, Bitmap bitmap) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://habit-bb585.appspot.com");
        StorageReference imageRef = storageRef.child(habitEventId + ".jpg");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                db.collection("habitEvents").document(habitEventId).update("photoPath", imageRef.getPath());
            }
        });
    }
}
