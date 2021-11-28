package com.example.habit;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class PhotoUtil {

    public static final String storage_root = "gs://habit-bb585.appspot.com";
    public final FirebaseFirestore db;
    public final FirebaseStorage fs;
    public final StorageReference storageRef;

    /**
     * Initialize instance of class with references to Firebase Firestore and Storage
     */
    public PhotoUtil() {
        db = FirebaseFirestore.getInstance();
        fs = FirebaseStorage.getInstance();
        storageRef = fs.getReferenceFromUrl(storage_root);
    }

    /**
     * Store a HabitEvent photo in FirebaseStorage and update the corresponding HabitEvent with the
     * path to where the photo is stored
     * @param habitEventId String ID of the HabitEvent
     * @param bitmap Bitmap of the photo to store
     */
    public void storePhoto(String habitEventId, Bitmap bitmap) {

        // Get new reference for this photo
        StorageReference imageRef = storageRef.child(habitEventId + ".jpg");

        // Compress to jpeg and convert to byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        // Start uploading the image
        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Update the corresponding HabitEvent with the path to this photo
                db.collection("habitEvents").document(habitEventId).update("photoPath", imageRef.getPath());
            }
        });
    }

    public static Bitmap getRoundedRectBitmap(Bitmap bitmap, int pixels) {
        Bitmap result = bitmap;
        result = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
        try {
            result = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(result);

            int color = 0xff424242;
            Paint paint = new Paint();
            Rect rect = new Rect(0, 0, 200, 200);

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawCircle(100, 100, 100, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);

        } catch (NullPointerException e) {
        } catch (OutOfMemoryError o) {
        }

        return result;
    }
}
