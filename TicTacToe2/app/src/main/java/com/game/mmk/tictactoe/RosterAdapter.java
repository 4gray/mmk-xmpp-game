package com.game.mmk.tictactoe;

import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;

import java.util.ArrayList;

/**
 * Created by serega05 on 07.05.15.
 */
public class RosterAdapter extends ArrayAdapter<Buddy> {

    private String _userStatus = null;
    private ArrayList<Buddy> buddyList = null;
    private String _userName = null;
    private Presence _userPresence = null;


    public RosterAdapter(Context context, ArrayList<Buddy> users) {
        super(context, 0, users);
        buddyList = users;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Buddy entry = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_roster, parent, false);
        }



        // Lookup view for data population
        TextView status = (TextView) convertView.findViewById(R.id.status);
        TextView name = (TextView) convertView.findViewById(R.id.username);
        ImageView statusIcon = (ImageView) convertView.findViewById(R.id.icon);

        _userName = entry.getName();
        _userPresence = XMPP.getInstance().getUserPresence(_userName);

        _userStatus = entry.getStatus();
        name.setText(_userName);

        // set status
        if (_userStatus != null)
            status.setText(_userStatus);
        else
            status.setText("offline");

        Log.d("presence", String.valueOf(_userPresence.isAvailable()));
        // Populate the data into the template view using the data object
        if (_userPresence.isAvailable()) {

            // set icon
          //  if (_userStatus.equals("Ready for game"))
                statusIcon.setImageResource(R.drawable.online_icon);
           // else

        }
        else {
            statusIcon.setImageResource(R.drawable.offline_icon);
            //status.setClickable(false);
            //name.setClickable(false);
            //statusIcon.setClickable(false);
        }

        // Return the completed view to render on screen
        return convertView;
    }


    public void updateRosterList(ArrayList<Buddy> users) {
        buddyList.clear();
        buddyList.addAll(users);
        this.notifyDataSetChanged();
    }

}