package com.game.mmk.tictactoe;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.ExecutionException;


public class LoginActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

    public void login(View view) throws ExecutionException, InterruptedException {

        // get values from text fields on the UI
        EditText username = (EditText) findViewById(R.id.usernameTxt);
        EditText password= (EditText) findViewById(R.id.passwordTxt);
        EditText server = (EditText) findViewById(R.id.serverTxt);

        // create XMPP connection and get connection object
        XMPP.getInstance().setConnection(username.getText().toString(), password.getText().toString(), server.getText().toString(), getApplicationContext());
        AbstractXMPPConnection connection = (AbstractXMPPConnection) new XMPP().getInstance().getConnection();

        Log.d("isConnected: ", String.valueOf(connection.isConnected()));
        if (connection.isConnected() == true) {
            Toast.makeText(getApplicationContext(), "Connected!", Toast.LENGTH_LONG).show();

            //go to buddy list activity
            Intent intent = new Intent(this, BuddyListActivity.class);
            startActivity(intent);
        }
        else {
            // TODO: show error
            Toast.makeText(getApplicationContext(), "Connection problem", Toast.LENGTH_LONG).show();
        }

        // set presence
        /*
        Presence presence = new Presence(Presence.Type.unavailable);

        presence.setStatus("Gone fishing");
        try {
            connection.sendPacket(presence);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
        */

        // create chat and send message
        /*
        Chat newChat = chatmanager.createChat("alice@planetjabber.de");

        try {
            newChat.sendMessage("Goodbye World!");
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
        */


    }
}
