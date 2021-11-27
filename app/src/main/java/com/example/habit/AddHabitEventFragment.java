package com.example.habit;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.google.android.gms.maps.model.LatLng;
import java.util.Objects;

public class AddHabitEventFragment extends DialogFragment {

    private Habit habit;
    private HabitEvent habitEvent;
    private EditText locationEditText; // TODO: @qg change this
    private EditText commentsEditText;
    private Button locationb;

    ImageView photoView;
    ConstraintLayout noPhotoFrame;
    ConstraintLayout photoFrame;
    SharedPreferences sharedPref;

    ImageButton addButton;
    ImageButton backButton;
    ImageButton photoButton;
    ImageView imageView4;       // TODO: @JUSTIN this is the image we have for this event

    PhotoUtil photoUtil;
    String currentPhotoPath;
    Bitmap photo;

    private static final int REQUEST_IMAGE_CAPTURE = 1;

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

        // Get EditTexts
        locationEditText = view.findViewById(R.id.location_edit_text);
        commentsEditText = view.findViewById(R.id.comments_edit_text);
        locationb=view.findViewById(R.id.locationb);

        // Set add button
        addButton = view.findViewById(R.id.addHabitEventButton);

        // PhotoUtil class for handling photos
        photoUtil = new PhotoUtil();

        locationb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                startActivity(intent);


            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // photo = ...; // TODO: @qg Properly get and set photo here
                String location = locationEditText.getText().toString(); // TODO: @qg Properly get and set location here, will have to change from string
                String comments = commentsEditText.getText().toString();

                // Create HabitEvent and add to DB
                habitEvent = new HabitEvent(location, comments); // TODO: @qg Will have to add a "photo" property to the HabitEvent class and change the constructor
                Habit.addEvent(habit.getHabitId(), habitEvent);

                // Store photo in Firebase Storage
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                photoUtil.storePhoto(habitEvent.getHabitEventId(), photo);

                // Close dialog
                Objects.requireNonNull(getDialog()).dismiss();
            }
        });

        // Set back button
        backButton = view.findViewById(R.id.backHabitEventButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getDialog()).dismiss();
            }
        });

        // Set photo button
        photoButton = view.findViewById(R.id.habitEventPhotoButton);
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Request camera permission
                if (ContextCompat.checkSelfPermission(requireContext(),
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(requireActivity(), new String[]{
                            Manifest.permission.CAMERA
                    }, 100);
                }

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