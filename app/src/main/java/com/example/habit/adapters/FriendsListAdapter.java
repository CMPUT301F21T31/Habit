package com.example.habit.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.habit.R;
import com.example.habit.entities.User;

import java.util.ArrayList;

/**
 * Adapter for displaying users you follow and requests from other users to follow you
 * US 05.02.02
 */
public class FriendsListAdapter extends ArrayAdapter<User> {

    private Context context;
    private ArrayList<User> users;
    private int type;

    /**
     *
     * @param context Context about where the list is located
     * @param resource Resource number
     * @param users User objects in list
     * @param type 0 == following list, 1 == follow request list
     */
    public FriendsListAdapter(@NonNull Context context, int resource, ArrayList<User> users, int type) {
        super(context, 0, users);
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

        // Get element layout based on type, either following or requests list
        if (view == null) {
            Log.i("HABIT VIEW NOTE", "VIEW NULL");
            if (type == 0 ) {
                view = LayoutInflater.from(context).inflate(R.layout.following_user_list_content, parent, false);
            } else {
                view = LayoutInflater.from(context).inflate(R.layout.follow_request_user_list_content, parent, false);
            }
        }

        // Get single User
        User user = users.get(position);

        // Get and set fields
        TextView displayName = view.findViewById(R.id.user_name_text);
        displayName.setText(user.getDisplayName());

        return view;
    }
}
