package com.game.mmk.tictactoe;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class InviteActivity extends ActionBarActivity {

    private String body = null;
    private String from = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);
        Intent intent = getIntent();

        TMessage tm = (TMessage) intent.getSerializableExtra("Message");

        body = tm.getBody();
        from = tm.getFrom();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_invite, menu);
        return true;
    }

    public void onAnnehmen (View view) {
        XMPP.getInstance().sendMessage("invite","accept",from);
    }

    public void onAblehnen (View view) {
        XMPP.getInstance().sendMessage("invite", "decline", from);
        Intent intent = new Intent();
        intent.setClass(this, BuddyListActivity.class);
        this.startActivity(intent);
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
