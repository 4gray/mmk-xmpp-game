package com.game.mmk.tictactoe;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    Turn _turn = null;
    RelativeLayout _layout = null;
    String _gameOpponent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _layout = (RelativeLayout) findViewById(R.id.gameLayout);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        // listen for intents with game turns
        // message handler

        TMessage tm = (TMessage) intent.getSerializableExtra("Message");

        String body = tm.getBody();
        String from = tm.getFrom();
        String subject = tm.getSubject();

        ImageButton imgBtn = null;

        //Toast.makeText(getApplicationContext(), body, Toast.LENGTH_LONG).show();

        // TODO: accept only game turns ignore all other messages
        if (subject.equals("game")) {

            // unlock UI
            unlockUI();

            switch (body) {
                case "00":
                    imgBtn = (ImageButton) findViewById(R.id.button00);
                    imgBtn.setImageResource(R.drawable.x1);
                    imgBtn.setEnabled(false);
                    break;
                case "01":
                    imgBtn = (ImageButton) findViewById(R.id.button01);
                    imgBtn.setImageResource(R.drawable.x1);
                    imgBtn.setEnabled(false);
                    break;
                case "02":
                    imgBtn = (ImageButton) findViewById(R.id.button02);
                    imgBtn.setImageResource(R.drawable.x1);
                    imgBtn.setEnabled(false);
                    break;
                case "10":
                    imgBtn = (ImageButton) findViewById(R.id.button10);
                    imgBtn.setImageResource(R.drawable.x1);
                    imgBtn.setEnabled(false);
                    break;
                case "11":
                    imgBtn = (ImageButton) findViewById(R.id.button11);
                    imgBtn.setImageResource(R.drawable.x1);
                    imgBtn.setEnabled(false);
                    break;
                case "12":
                    imgBtn = (ImageButton) findViewById(R.id.button12);
                    imgBtn.setImageResource(R.drawable.x1);
                    imgBtn.setEnabled(false);
                    break;
                case "20":
                    imgBtn = (ImageButton) findViewById(R.id.button20);
                    imgBtn.setImageResource(R.drawable.x1);
                    imgBtn.setEnabled(false);
                    break;
                case "21":
                    imgBtn = (ImageButton) findViewById(R.id.button21);
                    imgBtn.setImageResource(R.drawable.x1);
                    imgBtn.setEnabled(false);
                    break;
                case "22":
                    imgBtn = (ImageButton) findViewById(R.id.button22);
                    imgBtn.setImageResource(R.drawable.x1);
                    imgBtn.setEnabled(false);
                    break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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


    // tic or tac click
    public void gameTurn(View view) {
        // get field coordinates from clicked button
        _turn = new Turn(view.getTag().toString());

        // get game opponent
        _gameOpponent = XMPP.getInstance().getGameOpponent();

        // send message with you game turn
        XMPP.getInstance().sendMessage("game", _turn.toString(), _gameOpponent); // TODO: implement global function getGamePartner()

        ImageButton btn = (ImageButton) findViewById(view.getId());
        btn.setImageResource(R.drawable.circle1);
        btn.setEnabled(false);

        // block UI after my turn and wwait for opponent
        blockUI();
    }

    public void blockUI() {
        for (int i = 0; i < _layout.getChildCount(); i++) {
            View child = _layout.getChildAt(i);
            child.setEnabled(false);
        }
        Toast.makeText(getApplicationContext(), "UI was blocked. Waiting for opponent turn.", Toast.LENGTH_SHORT).show();
    }

    public void unlockUI() {
        for (int i = 0; i < _layout.getChildCount(); i++) {
            View child = _layout.getChildAt(i);
            child.setEnabled(true);
        }
        Toast.makeText(getApplicationContext(), "UI was unlocked. Now it's your turn.", Toast.LENGTH_SHORT).show();
    }

    //initGame() - clear array and set all btns to enabled

    //lockUI() - lock my UI and unlock opponent UI, if opponent turn


    //unlockUI - unlock my UI and lock opponent UI, if my turn


    //who start with first turn

}
