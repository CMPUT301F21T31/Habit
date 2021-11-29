package com.example.habit.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.habit.entities.Habit;
import com.example.habit.entities.HabitEvent;
import com.example.habit.utilities.PhotoUtil;
import com.example.habit.R;
import com.example.habit.activities.MapsActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Fragment to display a HabitEvent
 */
public class ViewEditHabitEventFragment extends DialogFragment {

    private Habit habit;
    private HabitEvent habitEvent;
    private EditText locationEditText; // TODO: @qg change this
    private EditText commentsEditText;

    ImageView photoView;
    ConstraintLayout noPhotoFrame;
    ConstraintLayout photoFrame;

    ImageButton backButton;
    ImageButton deleteButton;
    ImageButton editButton;
    ImageButton photoButton;
    ImageButton locationButton;

    PhotoUtil photoUtil;
    String currentPhotoPath;
    Bitmap photo;

    Geocoder geocoder;
    Double latitude;
    Double longitude;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_MAP_LOCATION = 2;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    final long ONE_MEGABYTE = 1024 * 1024;

    /**
     * Get a new fragment to display a HabitEvent
     * @param habitEvent HabitEvent to display
     * @param habit Parent habit
     */
    public ViewEditHabitEventFragment(HabitEvent habitEvent, Habit habit) {
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
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_view_edit_habit_event, null);
        TextView title = view.findViewById(R.id.habit_event_title);
        title.setText(habit.getTitle() + " Event");

        // Get EditTexts
        locationEditText = view.findViewById(R.id.location_edit_text);
        commentsEditText = view.findViewById(R.id.comments_edit_text);

        // Set text fields
        commentsEditText.setText(habitEvent.getComments());

        // Buttons
        backButton = view.findViewById(R.id.backHabitEventButton);
        deleteButton = view.findViewById(R.id.deleteHabitEventButton);
        editButton = view.findViewById(R.id.addHabitEventButton);
        photoButton = view.findViewById(R.id.habitEventPhotoButton);
        locationButton = view.findViewById(R.id.locationButton);

        // Get photo and related views
        photoFrame = view.findViewById(R.id.photoFrame);
        noPhotoFrame = view.findViewById(R.id.noPhotoFrame);
        photoView = view.findViewById(R.id.habitEventPhotoFrame);

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
                locationEditText.setText(address.getAddressLine(0));
                System.out.println("Got address");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Get photo
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

        // Set edit button click listener
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get and set comments
                String comments = commentsEditText.getText().toString();
                habitEvent.setComments(comments);

                // Set location
                habitEvent.setLatitude(latitude);
                habitEvent.setLongitude(longitude);

                // Update habit in DB
                Habit.updateHabitEvent(habitEvent.getHabitEventId(), habitEvent);

                // Update photo in Firebase Storage
                if (photo != null) {
                    photoUtil.storePhoto(habitEvent.getHabitEventId(), photo, true);
                }

                // Close dialog
                Objects.requireNonNull(getDialog()).dismiss();
            }
        });

        // Set delete button click listener
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Habit.deleteEvent(habit.getHabitId(), habitEvent.getHabitEventId());
                Objects.requireNonNull(getDialog()).dismiss();
            }
        });

        // Set back button click listener
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getDialog()).dismiss();
            }
        });

        // Set photo button
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Request camera permission
                if (ContextCompat.checkSelfPermission(requireContext(),
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(requireActivity(), new String[]{
                            Manifest.permission.CAMERA
                    }, 100);
                } else {

                    // Open Camera
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    Fragment frag = ViewEditHabitEventFragment.this;

                    // Get filepath
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(getContext(),
                                "com.example.android.fileprovider",
                                photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        System.out.println("PhotoURI: " + photoURI);
                        System.out.println("PhotoFile: " + photoFile.getAbsolutePath());
                    }
                    frag.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }

            }
        });

        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Request camera permission
                if (ContextCompat.checkSelfPermission(requireContext(),
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(requireActivity(), new String[]{
                            Manifest.permission.CAMERA
                    }, 100);
                } else {

                    // Open Camera
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    Fragment frag = ViewEditHabitEventFragment.this;

                    // Get filepath
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(getContext(),
                                "com.example.android.fileprovider",
                                photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        System.out.println("PhotoURI: " + photoURI);
                        System.out.println("PhotoFile: " + photoFile.getAbsolutePath());
                    }
                    frag.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });

        // Set location button click listener
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Request location permission
                if (ContextCompat.checkSelfPermission(requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(requireActivity(), new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION
                    }, 1);
                } else {
                    System.out.println("Permission granted");
                    // Get reference to this fragment and create MapsActivity
                    Fragment frag = ViewEditHabitEventFragment.this;
                    Intent intent = new Intent(getActivity(), MapsActivity.class);

                    // Include lat and lon in intent
                    intent.putExtra("lat", latitude);
                    intent.putExtra("lon", longitude);

                    frag.startActivityForResult(intent, REQUEST_MAP_LOCATION);
                }
            }
        });

        // Build and return dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder.setView(view).create();
    }

    /**
     * Create a new fragment view
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
        int width = getResources().getDimensionPixelSize(R.dimen.habitEvent_fragment_width);
        int height = getResources().getDimensionPixelSize(R.dimen.habitEvent_fragment_height);
        getDialog().getWindow().setLayout(width, height);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {

                // Hide button
                noPhotoFrame.setVisibility(View.INVISIBLE);

                // Set the picture to map
                photo = setPic();

            } else if (requestCode == REQUEST_MAP_LOCATION) {

                // Get location from intent, 200 is not a possible lat/lon val so use it as default
                latitude = data.getDoubleExtra("lat", 200);
                longitude = data.getDoubleExtra("lon", 200);

                // Set values to null if either was not set
                if (latitude == 200 || longitude == 200) {
                    latitude = null;
                    longitude = null;
                } else {

                    // Get more info about the selected location
                    try {
                        List<Address> addresses = null;
                        addresses = geocoder.getFromLocation(latitude, longitude, 1);
                        Address address = addresses.get(0);
                        locationEditText.setText(address.getAddressLine(0));
                        System.out.println("Got address");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private Bitmap setPic() {
        // Get the dimensions of the View
        int targetW = photoView.getWidth();
        int targetH = photoView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(currentPhotoPath, bmOptions);

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.max(1, Math.min(photoW/targetW, photoH/targetH));

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        photoView.setImageBitmap(bitmap);
        return bitmap;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,     /* prefix */
                ".jpg",      /* suffix */
                storageDir         /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
}