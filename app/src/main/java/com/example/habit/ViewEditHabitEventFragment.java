package com.example.habit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ViewEditHabitEventFragment extends DialogFragment {

    private HabitEvent habitEvent;
    private Habit habit;

    public ViewEditHabitEventFragment(HabitEvent habitEvent, Habit habit) {
        this.habitEvent = habitEvent;
        this.habit = habit;
    }

//    public AddCityFragment(HabitEvent habitEvent, int prevCityIndex) {
//        editMode = true;
//        this.prevCity = prevCity;
//        this.prevCityIndex = prevCityIndex;
//    }

//    public interface OnFragmentInteractionListener {
//        void onOkPressed(City newCity, int prevCityIndex);
//    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            listener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + "Must implement OnFragmentInteractionListener");
//        }
//    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_view_edit_habit_event, null);
        TextView titleView = view.findViewById(R.id.habit_event_title);
        TextView commentsView = view.findViewById(R.id.comments_edit_text);
        TextView locationView = view.findViewById(R.id.location_edit_text);
        titleView.setText(habit.getTitle() + " Event");
        commentsView.setText(habitEvent.getComments());
        locationView.setText(habitEvent.getLocation());

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
//                .setTitle("Event")
//                .setNegativeButton("Cancel", null)
//                .setPositiveButton("OK", null)
                .create();
//                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        String city = cityName.getText().toString();
//                        String province = provinceName.getText().toString();
//                        if (editMode) {
//                            prevCity.setCity(city);
//                            prevCity.setProvince(province);
//                            listener.onOkPressed(prevCity, prevCityIndex);
//                        } else {
//                            listener.onOkPressed(new City(city, province), prevCityIndex);
//                        }
//                    }
//                }).create();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}