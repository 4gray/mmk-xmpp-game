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
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutionException;

/**
 * Created by 4gray on 22.04.15.
 */

// singleton class with XMPP connection
public class XMPP {

    private AbstractXMPPConnection connection = null;
    protected Context context;
    private ChatManager chatmanager = null;
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
        chatmanager = ChatManager.getInstanceFor(connection);

        chatmanager.addChatListener(new ChatManagerListener() {
            @Override
            public void chatCreated(Chat chat, boolean createdLocally) {
                Log.d("chat:", chat.toString());
                chat.addMessageListener(new ChatMessageListener() {

                    @Override
                    public void processMessage(Chat chat, Message message) {
                        Log.d("test:", message.getBody());


                        //if (message.getBody() == "invite") {
                            forwardMessage(message);
                        //}


                    }

                });
            }

        });

    }

    public ChatManager getChatmanager() {
        return chatmanager;
    }

    private void forwardMessage(Message message) {

        if (message.getSubject().equals("invite")) {

            Intent intent = new Intent();
            intent.setClass(this.context, BuddyListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);

            TMessage tm = new TMessage(message.getSubject(), message.getBody(), message.getFrom());

            intent.putExtra("Message", tm);
            this.context.startActivity(intent);

        }
    }

    public void changePresence(String type) {
        Presence presence = null;
        if (type.equals("available")) {
            presence = new Presence(Presence.Type.available);
            presence.setStatus("Ready");
        } else if (type.equals("unavailable")) {
            presence = new Presence(Presence.Type.unavailable);
            presence.setStatus("In game");
        }

        try {
            connection.sendPacket(presence);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
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

    public void sendMessage(String subject, String body, String receiver) {
        Chat newChat = chatmanager.createChat(receiver);

        try {
            TMessage tm = new TMessage(subject, body, receiver);
            Message m = new Message();
            m.addSubject("",subject);
            m.addBody("",body);

            newChat.sendMessage(m);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
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

