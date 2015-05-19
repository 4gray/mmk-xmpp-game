package com.game.mmk.tictactoe;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
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
import java.util.Timer;
import java.util.TimerTask;


public class BuddyListActivity extends ActionBarActivity {

    private ChatManager chatmanager = null;
    private AlertDialog.Builder builder = null;
    private String userLogin = null;
    private Timer timer = null;
    private TimerTask tt = null;
    private AlertDialog dlg = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buddy_list);

        builder = new AlertDialog.Builder(this);
        userLogin = XMPP.getInstance().getUserLogin();

        setTitle(userLogin + "'s buddy list");

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

                // ListView Clicked item value
                RosterEntry itemValue = (RosterEntry) rosterList.getItemAtPosition(position);

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
        timer = new Timer();


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
                    .setIcon(android.R.drawable.ic_dialog_alert);
            dlg = builder.create();
            dlg.show();
        } else if (body.equals("accept")) {
            XMPP.getInstance().sendMessage("invite", "go", from);
            XMPP.getInstance().setGameOpponent(from);
            GameLogic.getInstance().setStarter(from);
            tt.cancel();
            goToGameArea();
        } else if (body.equals("go")) {
            XMPP.getInstance().setGameOpponent(from);
            goToGameArea();
        } else if (body.equals("decline")) {
            // TODO
            Toast.makeText(getApplicationContext(), "Invitation declined", Toast.LENGTH_LONG).show();
            dlg.dismiss();
            timer.cancel();
        } else if (body.equals("timeout")) {
            Toast.makeText(getApplicationContext(), "Timeout", Toast.LENGTH_LONG).show();
            dlg.dismiss();
            timer.cancel();
        }

    }


    private void sendInvitation(String name) {
        XMPP.getInstance().setGameOpponent(name);
        XMPP.getInstance().sendMessage("invite", "invitation", name);
        builder
                .setTitle("Invitation")
                .setMessage("Waiting for opponent answer.");

        dlg = builder.create();
        dlg.show();
        timer = new Timer();

        //todo initiate timeout
        tt = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dlg.dismiss();
                        timer.cancel();
                        makeToast("Timeout");
                        XMPP.getInstance().sendMessage("invite", "timeout", XMPP.getInstance().getGameOpponent());
                    }
                });
            }
        };
        timer.schedule(tt,10000);

    }

    private void makeToast (String title) {
        Toast.makeText(getApplicationContext(), title, Toast.LENGTH_LONG).show();
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
        if (id == R.id.action_logout) {
            //disconnect
            XMPP.getInstance().disconnect();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);


            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //do whatever you need for the hardware 'back' button
            Toast.makeText(getApplicationContext(), "Back clicked", Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
