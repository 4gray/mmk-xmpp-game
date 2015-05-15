package com.game.mmk.tictactoe;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

    private String _coordinate = null;
    private String _gameOpponent = null;
    private String _starter = null;
    private Boolean _gameDecision = null;
    private Integer _turnLimit = 0;
    private TMessage _tmessage = null;
    private AlertDialog.Builder _builder = null;
    private ImageButton imgBtn = null;
    private RelativeLayout _layout = null;
    private ImageView _turnIndicatorImg = null;
    private TextView _turnIndicator = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _layout = (RelativeLayout) findViewById(R.id.gameLayout);
        _turnIndicator = (TextView) findViewById(R.id.turnIndicator);
        _turnIndicatorImg = (ImageView) findViewById(R.id.turnIndicatorImg);

        _builder = new AlertDialog.Builder(this);

        _builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
               goToBuddyList();
            }
        });

        _starter = GameLogic.getInstance().getStarter();

        // get game opponent
        _gameOpponent = XMPP.getInstance().getGameOpponent();

        // block one UI
        if (_starter == _gameOpponent) {
            blockUI();
        }
    }

    public void goToBuddyList() {
        Intent intent = new Intent(this, BuddyListActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        // listen for intents with game turns
        // message handler

        _tmessage = (TMessage) intent.getSerializableExtra("Message");

        //Toast.makeText(getApplicationContext(), body, Toast.LENGTH_LONG).show();

        // TODO: accept only game turns, ignore all other messages
        if (_tmessage.getSubject().equals("game")) {

            // unlock UI
            unlockUI();

            switch (_tmessage.getBody()) {
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
                case "lose":
                    _builder.setTitle("Ohhh")
                        .setMessage("You lose!")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                    GameLogic.getInstance().initNewGame();
                    break;
                case "draw":
                    _builder
                            .setTitle("WOW")
                            .setMessage("Draw!")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                    GameLogic.getInstance().initNewGame();
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

        // send message with you game turn
        XMPP.getInstance().sendMessage("game", _coordinate, _gameOpponent);

        ImageButton btn = (ImageButton) findViewById(view.getId());
        btn.setImageResource(R.drawable.circle1);
        btn.setEnabled(false);

        // block UI after my turn and wait for opponent
        blockUI();

        // starter makes 5 turns
        if (XMPP.getInstance().getUserLogin() == GameLogic.getInstance().getStarter()) {
            _turnLimit = 4;
        }
        else {
            _turnLimit = 5;
        }

        //call gameLogic
        _gameDecision = GameLogic.getInstance().play(Integer.parseInt(_coordinate));
        if (_gameDecision == true) {
            _builder
                    .setTitle("Congratulations")
                    .setMessage("You win!")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            XMPP.getInstance().sendMessage("game", "lose", XMPP.getInstance().getGameOpponent());
            GameLogic.getInstance().initNewGame();
        }
        else if (GameLogic.getInstance().getTurnCounter() == _turnLimit) {
            _builder
                    .setTitle("WOW")
                    .setMessage("Draw!")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            XMPP.getInstance().sendMessage("game", "draw", XMPP.getInstance().getGameOpponent());
            GameLogic.getInstance().initNewGame();
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
