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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
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

    String _coordinate = null;
    RelativeLayout _layout = null;
    String _gameOpponent = null;
    ImageView _turnIndicatorImg = null;
    TextView _turnIndicator = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _layout = (RelativeLayout) findViewById(R.id.gameLayout);
        _turnIndicator = (TextView) findViewById(R.id.turnIndicator);
        _turnIndicatorImg = (ImageView) findViewById(R.id.turnIndicatorImg);
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
                case "0":
                    imgBtn = (ImageButton) findViewById(R.id.button0);
                    imgBtn.setImageResource(R.drawable.x1);
                    imgBtn.setEnabled(false);
                    break;
                case "1":
                    imgBtn = (ImageButton) findViewById(R.id.button1);
                    imgBtn.setImageResource(R.drawable.x1);
                    imgBtn.setEnabled(false);
                    break;
                case "2":
                    imgBtn = (ImageButton) findViewById(R.id.button2);
                    imgBtn.setImageResource(R.drawable.x1);
                    imgBtn.setEnabled(false);
                    break;
                case "3":
                    imgBtn = (ImageButton) findViewById(R.id.button3);
                    imgBtn.setImageResource(R.drawable.x1);
                    imgBtn.setEnabled(false);
                    break;
                case "4":
                    imgBtn = (ImageButton) findViewById(R.id.button4);
                    imgBtn.setImageResource(R.drawable.x1);
                    imgBtn.setEnabled(false);
                    break;
                case "5":
                    imgBtn = (ImageButton) findViewById(R.id.button5);
                    imgBtn.setImageResource(R.drawable.x1);
                    imgBtn.setEnabled(false);
                    break;
                case "6":
                    imgBtn = (ImageButton) findViewById(R.id.button6);
                    imgBtn.setImageResource(R.drawable.x1);
                    imgBtn.setEnabled(false);
                    break;
                case "7":
                    imgBtn = (ImageButton) findViewById(R.id.button7);
                    imgBtn.setImageResource(R.drawable.x1);
                    imgBtn.setEnabled(false);
                    break;
                case "8":
                    imgBtn = (ImageButton) findViewById(R.id.button8);
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
        _coordinate = view.getTag().toString();

        // get game opponent
        _gameOpponent = XMPP.getInstance().getGameOpponent();

        // send message with you game turn
        XMPP.getInstance().sendMessage("game", _coordinate, _gameOpponent);

        ImageButton btn = (ImageButton) findViewById(view.getId());
        btn.setImageResource(R.drawable.circle1);
        btn.setEnabled(false);

        // block UI after my turn and wait for opponent
        blockUI();

        //call gameLogic

        Boolean gameDecision = GameLogic.getInstance().play(Integer.parseInt(_coordinate));
        if (gameDecision == true) {
            Toast.makeText(getApplicationContext(), "WIN!!!!", Toast.LENGTH_LONG).show();
        }


    }

    //lockUI() - lock my UI and unlock opponent UI, if opponent turn
    public void blockUI() {
        for (int i = 0; i < _layout.getChildCount(); i++) {
            View child = _layout.getChildAt(i);
            child.setEnabled(false);
        }
        _turnIndicator.setText("Gegner ist dran");
        _turnIndicatorImg.setImageResource(R.drawable.x1);
        //Toast.makeText(getApplicationContext(), "UI was blocked. Waiting for opponent turn.", Toast.LENGTH_SHORT).show();
    }

    //unlockUI - unlock my UI and lock opponent UI, if my turn
    public void unlockUI() {
        for (int i = 0; i < _layout.getChildCount(); i++) {
            View child = _layout.getChildAt(i);
            child.setEnabled(true);
        }
        _turnIndicator.setText("Du bist dran");
        _turnIndicatorImg.setImageResource(R.drawable.circle1);
        //Toast.makeText(getApplicationContext(), "UI was unlocked. Now it's your turn.", Toast.LENGTH_SHORT).show();
    }

    // initGame() - clear array and set all btns to enabled
    // set person who start with first turn

}
