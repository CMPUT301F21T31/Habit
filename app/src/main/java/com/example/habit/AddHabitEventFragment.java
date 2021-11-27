package com.example.habit;

import static android.graphics.Bitmap.CompressFormat.PNG;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.Objects;

public class AddHabitEventFragment extends DialogFragment {

    private Habit habit;
    private HabitEvent habitEvent;
    private EditText locationEditText; // TODO: @qg change this
    private EditText commentsEditText;
    private Button locationb;

    SharedPreferences sharedPref;

    ImageButton addButton;
    ImageButton backButton;
    ImageButton photoButton;
    ImageView imageView4;       // TODO: @JUSTIN this is the image we have for this event


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
        imageView4= view.findViewById(R.id.imageView4);
        // Get EditTexts

        locationEditText = view.findViewById(R.id.location_edit_text);
        commentsEditText = view.findViewById(R.id.comments_edit_text);
        locationb=view.findViewById(R.id.locationb);
        // Set add button
        addButton = view.findViewById(R.id.addHabitEventButton);

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

              //  Intent intent = new Intent(getActivity(), camera.class);
               // startActivity(intent);
                // photo = ...; // TODO: @qg Properly get and set photo here
                String location = locationEditText.getText().toString(); // TODO: @qg Properly get and set location here, will have to change from string
                String comments = commentsEditText.getText().toString();

                // Create HabitEvent and add to DB
                habitEvent = new HabitEvent(location, comments); // TODO: @qg Will have to add a "photo" property to the HabitEvent class and change the constructor
                Habit.addEvent(habit.getHabitId(), habitEvent);

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
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,100);

                // TODO: @qg Add what happens when use clicks the photo button in HabitEvent fragment
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
        imageView4.setVisibility(View.INVISIBLE);

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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            //Get Capture Image
            Bitmap captureImage = (Bitmap) data.getExtras().get("data");
            //Set Capture Image to ImageView
            //  imageView.setImageBitmap(captureImage);
            Intent intent = new Intent(getActivity(),camera.class);
            intent.putExtra("bitmap", captureImage);
            imageView4.setImageBitmap(intent.getParcelableExtra("bitmap"));
            imageView4.setVisibility(View.VISIBLE);
        }

    }
}