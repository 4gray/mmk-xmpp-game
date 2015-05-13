package com.game.mmk.tictactoe;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.roster.RosterEntry;

import java.util.ArrayList;
import java.util.Collection;


public class BuddyListActivity extends ActionBarActivity {

    private ChatManager chatmanager = null;
    AlertDialog.Builder builder = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buddy_list);

        builder = new AlertDialog.Builder(this);

        // get roster
        this.chatmanager = XMPP.getInstance().getChatmanager();
        Collection<RosterEntry> entries = XMPP.getInstance().getRoster();
        ArrayList<RosterEntry> list = new ArrayList<RosterEntry>();
        list.addAll(entries);

        final ListView rosterList = (ListView) findViewById(R.id.listView);
        RosterAdapter adapter = new RosterAdapter(this, list);


        // Assign adapter to ListView
        rosterList.setAdapter(adapter);
        rosterList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;

                // ListView Clicked item value
                RosterEntry itemValue = (RosterEntry) rosterList.getItemAtPosition(position);

                // Show Alert
                Toast.makeText(getApplicationContext(),
                        "Position :" + itemPosition + "  ListItem : " + itemValue.getName(), Toast.LENGTH_LONG)
                        .show();

                //if (itemValue.getStatus() != null && !itemValue.getStatus().toString().equals("unavilable")) {
                sendInvitation(itemValue.getUser());
            }

        });




    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        TMessage tm = (TMessage) intent.getSerializableExtra("Message");

        String body = tm.getBody();
        final String from = tm.getFrom();

        if (body.equals("invitation")) {
            builder
                    .setTitle("Invitation")
                    .setMessage("Do you want to join the game?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            XMPP.getInstance().sendMessage("invite", "accept", from);
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            XMPP.getInstance().sendMessage("invite", "decline", from);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else if (body.equals("accept")) {
            XMPP.getInstance().sendMessage("invite", "go", from);
            XMPP.getInstance().setGameOpponent(from);
            goToGameArea();
        } else if (body.equals("go")) {
            XMPP.getInstance().setGameOpponent(from);
            goToGameArea();
        } else if (body.equals("decline")) {
            Toast.makeText(getApplicationContext(), "Invitation declined", Toast.LENGTH_LONG).show();
        }

    }


    private void sendInvitation(String name) {
        XMPP.getInstance().setGameOpponent(name);
        XMPP.getInstance().sendMessage("invite", "invitation", name);
        builder
                .setTitle("Invitation")
                .setMessage("Waiting for opponent answer.")
                .show();
        //todo initiate timeout
    }

    private void goToGameArea() {
        //XMPP.getInstance().changePresence("unavailable");
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_buddy_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
