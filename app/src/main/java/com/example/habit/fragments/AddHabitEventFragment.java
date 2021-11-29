package com.example.habit.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.content.SharedPreferences;
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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.habit.entities.Habit;
import com.example.habit.entities.HabitEvent;
import com.example.habit.utilities.PhotoUtil;
import com.example.habit.R;
import com.example.habit.activities.MapsActivity;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class AddHabitEventFragment extends DialogFragment {

    private Habit habit;
    private HabitEvent habitEvent;
    private EditText locationEditText;
    private EditText commentsEditText;

    ImageView photoView;
    ConstraintLayout noPhotoFrame;
    ConstraintLayout photoFrame;
    SharedPreferences sharedPref;

    ImageButton addButton;
    ImageButton backButton;
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

    /**
     * Create a new fragment to add a HabitEvent
     * @param habit Habit object to which to add event
     */
     public AddHabitEventFragment(Habit habit) {
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
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_add_habit_event, null);
        TextView title = view.findViewById(R.id.habit_event_title);
        title.setText(habit.getTitle() + " Event");
      
        // Get photo and related views
        photoFrame = view.findViewById(R.id.photoFrame);
        noPhotoFrame = view.findViewById(R.id.noPhotoFrame);
        photoView = view.findViewById(R.id.habitEventPhotoFrame);

        // Get EditTexts
        locationEditText = view.findViewById(R.id.location_edit_text);
        commentsEditText = view.findViewById(R.id.comments_edit_text);

        // Get buttons
        addButton = view.findViewById(R.id.addHabitEventButton);
        locationButton = view.findViewById(R.id.locationButton);
        backButton = view.findViewById(R.id.backHabitEventButton);
        photoButton = view.findViewById(R.id.habitEventPhotoButton);

        // PhotoUtil class for handling photos
        photoUtil = new PhotoUtil();

        // Get geocoder for extracting location details from lat/long
        geocoder = new Geocoder(getActivity(), Locale.getDefault());

        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ContextCompat.checkSelfPermission(requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(requireActivity(), new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION
                    }, 1);
                } else {
                    System.out.println("Permission granted");

                    // Get reference to this fragment and create MapsActivity
                    Fragment frag = AddHabitEventFragment.this;
                    Intent intent = new Intent(getActivity(), MapsActivity.class);

                    // Include lat and lon in intent
                    intent.putExtra("lat", latitude);
                    intent.putExtra("lon", longitude);

                    frag.startActivityForResult(intent, REQUEST_MAP_LOCATION);
                }
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get comment string
                String comment = commentsEditText.getText().toString();

                // Create HabitEvent and add to DB
                habitEvent = new HabitEvent(latitude, longitude, comment);
                Habit.addEvent(habit.getHabitId(), habitEvent);

                // Store photo in Firebase Storage
                if (photo != null) {
                    photoUtil.storePhoto(habitEvent.getHabitEventId(), photo, false);
                }

                // Close dialog
                Objects.requireNonNull(getDialog()).dismiss();
            }
        });

        // Set back button
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
                    Fragment frag = AddHabitEventFragment.this;

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
                    Fragment frag = AddHabitEventFragment.this;

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

        // Build and return dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder.setView(view).create();
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

                    // Get and display more info about the selected location
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
}