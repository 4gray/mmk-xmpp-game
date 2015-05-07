package com.game.mmk.tictactoe;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.ExecutionException;

/**
 * Created by 4gray on 22.04.15.
 */

// singleton class with XMPP connection
public class XMPP {

    private AbstractXMPPConnection connection = null;
    protected Context context;

    private static XMPP instance = null;

    // returns XMPP class instance
    public synchronized static XMPP getInstance() {
        if(instance==null){
            instance = new XMPP();
        }
        return instance;
    }

    public void setConnection(String login, String pass, String server, Context context) throws ExecutionException, InterruptedException {
        this.connection = (AbstractXMPPConnection) new XMPPTask(login,pass,server).execute().get();

        this.context = context;

        // set message listeners
        ChatManager chatmanager = ChatManager.getInstanceFor(connection);

        chatmanager.addChatListener(new ChatManagerListener() {
            @Override
            public void chatCreated(Chat chat, boolean createdLocally) {
                Log.d("chat:", chat.toString());
                chat.addMessageListener(new ChatMessageListener() {

                    @Override
                    public void processMessage(Chat chat, Message message) {
                        Log.d("test:", message.getBody());


                        //if (message.getBody() == "invite") {
                            forwardInvitation(message.getBody());
                        //}


                    }

                });
            }

        });

    }

    private void forwardInvitation(String message) {
        Intent intent = new Intent();
        intent.setClass(this.context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("Message",message); // define different message types
        this.context.startActivity(intent);
    }

    public AbstractXMPPConnection getConnection() {
        return this.connection;
    }

    public Collection<RosterEntry> getRoster() {
        Roster roster = Roster.getInstanceFor(this.connection);


        if (!roster.isLoaded())
            try {
                roster.reloadAndWait();
            } catch (SmackException.NotLoggedInException e) {
                e.printStackTrace();
            } catch (SmackException.NotConnectedException e) {
                e.printStackTrace();
            }

        Log.d("roster", String.valueOf(roster.getEntryCount()));

        Collection<RosterEntry> entries = roster.getEntries();


        return entries;
    }

    public class XMPPTask extends AsyncTask {

        public String username;
        public String password;
        public String server;

        public XMPPTask(String username, String password, String server) {
            this.username = username;
            this.password = password;
            this.server = server;
        }

        @Override
        protected Object doInBackground(Object[] params) {

            // create the configuration for this new connection
            XMPPTCPConnectionConfiguration.Builder configBuilder = XMPPTCPConnectionConfiguration.builder();

            // set connection variables
            configBuilder.setUsernameAndPassword(username, password);
            configBuilder.setResource("Android");
            configBuilder.setServiceName(server);

            // create connection object
            AbstractXMPPConnection connection = new XMPPTCPConnection(configBuilder.build());

            // connect to the server
            try {
                connection.connect();
            } catch (SmackException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XMPPException e) {
                e.printStackTrace();
            }

            // log into the server
            try {
                connection.login();
            } catch (XMPPException e) {
                e.printStackTrace();
            } catch (SmackException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return connection;
        }


    }


}

