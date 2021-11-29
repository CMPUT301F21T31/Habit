package com.example.habit.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import com.example.habit.entities.Habit;
import com.example.habit.entities.HabitEvent;
import com.example.habit.R;
import com.example.habit.utilities.PhotoUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * US 05.03.01
 * This class is viewing all events of each PUBLIC habits belongs to the current user followers.
 * All text boxes and image in the interface are viewable ONLY.
 * @author lewisning
 * @see android.app.Activity
 * @see android.content.Context
 */
public class ViewOnlyHabitEventFragment extends DialogFragment {

    private Habit habit;
    private HabitEvent habitEvent;
    private TextView locationTextView;
    private TextView commentsTextView;

    ConstraintLayout noPhotoFrame;
    ConstraintLayout photoFrame;
    ImageView photoView;
    ImageButton backButton;
    PhotoUtil photoUtil;

    Geocoder geocoder;
    Double latitude;
    Double longitude;

    final long ONE_MEGABYTE = 1024 * 1024;

    /**
     * Get a new fragment to display a HabitEvent
     * @param habitEvent HabitEvent to display
     * @param habit Parent habit
     */
    public ViewOnlyHabitEventFragment(HabitEvent habitEvent, Habit habit) {
        this.habitEvent = habitEvent;
        this.habit = habit;
    }

    /**
     * Set fields in fragment view
     * @param savedInstanceState Previous state
     * @return Dialog view of HabitEvent
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_view_only_habit_event, null);
        TextView title = view.findViewById(R.id.habit_event_title);
        title.setText(habit.getTitle() + " Event");

        // Get EditTexts
        locationTextView = view.findViewById(R.id.location_view_text);
        commentsTextView = view.findViewById(R.id.comments_view_text);

        // Get photo and related views
        photoFrame = view.findViewById(R.id.photoFrame);
        noPhotoFrame = view.findViewById(R.id.noPhotoFrame);
        photoView = view.findViewById(R.id.habitEventPhotoFrame);

        // Set comment field
        commentsTextView.setText(habitEvent.getComments());

        // Get location
        latitude = habitEvent.getLatitude();
        longitude = habitEvent.getLongitude();
        geocoder = new Geocoder(getActivity(), Locale.getDefault());

        // Get and display more info about the location
        if (latitude != null && longitude != null) {
            try {
                List<Address> addresses = null;
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                Address address = addresses.get(0);
                locationTextView.setText(address.getAddressLine(0));
                System.out.println("Got address");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Set photo if there is one
        PhotoUtil photoUtil = new PhotoUtil();
        if (habitEvent.getPhotoPath() != null) {
            StorageReference imageRef = photoUtil.storageRef.child(habitEvent.getPhotoPath());
            imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    // Data for "images/island.jpg" is returns, use this as needed
                    Bitmap photo = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    noPhotoFrame.setVisibility(View.INVISIBLE);
                    photoView.setImageBitmap(photo);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        }

        // Set back button
        backButton = view.findViewById(R.id.backHabitEventButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getDialog()).dismiss();
            }
        });

        // Build and return dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder.setView(view).create();
    }

    /**
     * Create a new HabitEvent fragment view
     * @param inflater Inflater to use
     * @param container Container for dialog
     * @param savedInstanceState Previous state
     * @return View for HabitEvent fragment
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        int width = getResources().getDimensionPixelSize(R.dimen.sendRequest_fragment_width);
        int height = getResources().getDimensionPixelSize(R.dimen.sendRequest_fragment_height);
        getDialog().getWindow().setLayout(width, height);
    }
}
