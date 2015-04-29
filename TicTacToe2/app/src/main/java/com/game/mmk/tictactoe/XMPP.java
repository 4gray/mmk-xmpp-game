package com.game.mmk.tictactoe;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import java.io.IOException;

/**
 * Created by 4gray on 22.04.15.
 */

public class XMPP extends AsyncTask {

    public String username;
    public String password;
    public String server;

    public XMPP(String username, String password, String server) {
        this.username = username;
        this.password = password;
        this.server = server;
    }

    @Override
    protected Object doInBackground(Object[] params) {

        Log.d("username", username);
        Log.d("password", password);
        Log.d("server", server);

        //SmackConfiguration.DEBUG = true;

        // Create the configuration for this new connection
        XMPPTCPConnectionConfiguration.Builder configBuilder = XMPPTCPConnectionConfiguration.builder();

        //configBuilder.setDebuggerEnabled(true);
        configBuilder.setUsernameAndPassword("bob", "S3#fsd24_Fa3");
        configBuilder.setResource("SomeResource");
        configBuilder.setServiceName("planetjabber.de");

        System.setProperty("java.net.preferIPv6Addresses", "false");

        AbstractXMPPConnection connection = new XMPPTCPConnection(configBuilder.build());
        // Connect to the server

        try {
            connection.connect();
            Log.d("isConnected: ", String.valueOf(connection.isConnected()));
        } catch (SmackException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XMPPException e) {
            e.printStackTrace();
        }

        // Log into the server

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