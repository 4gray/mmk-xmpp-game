package com.game.mmk.tictactoe;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

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
    private String gameOpponent = null;
    private String login = null;
    private Roster _roster = null;
    private Collection<RosterEntry> _rosterEntries = null;
    private String _userStatus = null;
    private Presence _presence = null;


    // returns XMPP class instance
    public synchronized static XMPP getInstance() {
        if(instance==null){
            instance = new XMPP();
        }
        return instance;
    }

    /*
        XMPP method which uses AsyncTask to connect with XMPP server
     */

    public void setConnection(LoginActivity caller, final String login, String pass, String server, Context context) throws ExecutionException, InterruptedException {

        new XMPPTask(caller, login, pass, server).execute();

        setUserLogin(login);
        this.context = context;

       /* connection.addConnectionListener(new ConnectionListener() {
            @Override
            public void connected(XMPPConnection connection) {

            }

            @Override
            public void authenticated(XMPPConnection connection, boolean resumed) {

            }

            @Override
            public void connectionClosed() {

            }

            @Override
            public void connectionClosedOnError(Exception e) {

            }

            @Override
            public void reconnectionSuccessful() {

            }

            @Override
            public void reconnectingIn(int seconds) {

            }

            @Override
            public void reconnectionFailed(Exception e) {

            }
        });
        */

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
        else if (message.getSubject().equals("game")) {
            Intent intent = new Intent();
            intent.setClass(this.context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);

            TMessage tm = new TMessage(message.getSubject(), message.getBody(), message.getFrom());

            intent.putExtra("Message", tm);
            this.context.startActivity(intent);
        }
    }

    public void changePresence(String type) throws SmackException.NotConnectedException {


        Presence presence = null;
        /*
        if (type.equals("available")) {
            presence = new Presence(Presence.Type.available);
            presence.setStatus("Ready");
            presence.setPriority(1);
            presence.setMode(Presence.Mode.available);
        } else if (type.equals("unavailable")) {
            presence = new Presence(Presence.Type.unavailable);
            presence.setStatus("In game");
        }

        try {
            connection.sendPacket(presence);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
        */

        presence = new Presence(Presence.Type.available, "Spielbereit", 42, Presence.Mode.available);
        this.connection.sendPacket(presence);
    }

    /*
        Returns XMPPConnection object for handling in activities
     */
    public AbstractXMPPConnection getConnection() {
        return this.connection;
    }

    /*
        Returns buddy list (roster) for connected user
     */
    public Collection<RosterEntry> getRosterEntries() {
        _roster = Roster.getInstanceFor(this.connection);

        if (!_roster.isLoaded())
            try {
                _roster.reloadAndWait();
            } catch (SmackException.NotLoggedInException e) {
                e.printStackTrace();
            } catch (SmackException.NotConnectedException e) {
                e.printStackTrace();
            }

        _rosterEntries = _roster.getEntries();

        return _rosterEntries;
    }


    public void sendMessage(String subject, String body, String receiver) {
        Chat newChat = chatmanager.createChat(receiver);

        try {
            Message m = new Message();
            m.addSubject("",subject);
            m.addBody("",body);

            newChat.sendMessage(m);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }

    public void setUserLogin(String login) {
        this.login = login;
    }

    public String getUserLogin() {
        return login;
    }

    public void disconnect() {
        this.connection.disconnect();
    }

    public void setGameOpponent(String from) {
        this.gameOpponent = from;
    }

    public String getGameOpponent() {
        return this.gameOpponent;
    }

    public Roster getRoster() {
        return _roster;
    }

    public Presence getUserPresence(String user) {
        _roster = Roster.getInstanceFor(XMPP.getInstance().getConnection());
        _presence = _roster.getPresence(user);
        return _presence;
    }

    public String getUserStatus(String user) {
        _roster = Roster.getInstanceFor(XMPP.getInstance().getConnection());
        _userStatus = getUserPresence(user).getStatus();
        return _userStatus;
    }

    public ArrayList getBuddyList() {
        ArrayList<Buddy> buddyList = new ArrayList<>();

        for(RosterEntry entry : getRosterEntries()) {
            _presence = _roster.getPresence(entry.getUser());
            buddyList.add(new Buddy(entry.getUser(), _presence.getStatus(), _presence.getType().name()));
        }
        return buddyList;
    }


    /*
        AsyncTask class as extra thread for connection to XMPP server in background
     */
    private class XMPPTask extends AsyncTask {

        public String username;
        public String password;
        public String server;
        public LoginActivity caller;
        private ProgressDialog dialog = null;

        public XMPPTask(LoginActivity caller, String username, String password, String server) {
            this.username = username;
            this.password = password;
            this.server = server;
            this.caller = caller;
            dialog = new ProgressDialog(caller);
        }


        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Bitte warten!");
            this.dialog.show();
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


        @Override
        protected void onPostExecute(Object result) {

            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            connection = (AbstractXMPPConnection) result;

            // set message listeners
            chatmanager = ChatManager.getInstanceFor(connection);

            chatmanager.addChatListener(new ChatManagerListener() {
                @Override
                public void chatCreated(Chat chat, boolean createdLocally) {
                    Log.d("chat:", chat.toString());
                    chat.addMessageListener(new ChatMessageListener() {

                        @Override
                        public void processMessage(Chat chat, Message message) {
                            forwardMessage(message);
                        }

                    });
                }

            });

            try {
                caller.onResponseReceived(result);
            } catch (SmackException.NotConnectedException e) {
                e.printStackTrace();
            }
        }
    }


}

