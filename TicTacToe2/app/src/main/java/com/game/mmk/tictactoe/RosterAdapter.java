package com.game.mmk.tictactoe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.jivesoftware.smack.roster.RosterEntry;

import java.util.ArrayList;

/**
 * Created by serega05 on 07.05.15.
 */
public class RosterAdapter  extends ArrayAdapter<RosterEntry> {
        public RosterAdapter(Context context, ArrayList<RosterEntry> users) {
            super(context, 0, users);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            RosterEntry entry = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_roster, parent, false);
            }
            // Lookup view for data population
            TextView status = (TextView) convertView.findViewById(R.id.status);
            TextView name = (TextView) convertView.findViewById(R.id.username);
            // Populate the data into the template view using the data object
            status.setText(entry.getUser());
            name.setText(entry.getName());
            // Return the completed view to render on screen
            return convertView;
        }
    }