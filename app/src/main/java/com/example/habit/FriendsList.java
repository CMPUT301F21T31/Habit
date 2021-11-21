package com.example.habit;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class FriendsList extends ArrayAdapter<User> {

    private Context context;
    private ArrayList<User> users;
    private int type;

    /**
     *
     * @param context Context about where the list is located
     * @param resource Resource number
     * @param users User objects in list
     */
    public FriendsList(@NonNull Context context, int resource, ArrayList<User> users) {
        super(context, resource);
        this.context = context;
        this.users = users;
        this.type = type;
    }

    /**
     * Get a View for element in list
     * @param position Integer position in list
     * @param convertView View to use
     * @param parent Parent view of the element
     * @return View for element
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;

        // Get element layout
        if (view == null) {
            Log.i("HABIT VIEW NOTE", "VIEW NULL");
            view = LayoutInflater.from(context).inflate(R.layout.user_list_content, parent, false);
        }

        // Get single User
        User user = users.get(position);

        // Get and set fields
        TextView displayName = view.findViewById(R.id.user_name_text);
        displayName.setText(user.getDisplayName());

        return view;
    }
}
