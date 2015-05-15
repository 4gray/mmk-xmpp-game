package com.game.mmk.tictactoe;

import android.util.Log;

/**
 * Created by 4gray on 13.05.15.
 */
public class GameLogic {

    private boolean[] _gameArea = null;
    private int _turnCounter = 0;

    private static GameLogic ourInstance = new GameLogic();

    public static GameLogic getInstance() {
        return ourInstance;
    }

    private GameLogic() {
        initNewGame();
    }

    public void initNewGame() {
        _gameArea = new boolean[9];
        _turnCounter = 0;

        for (int i=0;i<=8;i++) {
            _gameArea[i] = false;
        }
    }

    public boolean play(int coord) {
        _turnCounter++;
        _gameArea[coord] = true;
        Boolean gameDecision = gameDecision();

        if (gameDecision == true) {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean gameDecision() {
        // win cases
        if (_gameArea[0] == true && _gameArea[1] == true && _gameArea[2] == true) {
            return true;
        }
        else if (_gameArea[0] == true && _gameArea[4] == true && _gameArea[8] == true) {
            return true;
        }
        else if (_gameArea[3] == true && _gameArea[4] == true && _gameArea[5] == true) {
            return true;
        }
        else if (_gameArea[6] == true && _gameArea[7] == true && _gameArea[8] == true) {
            return true;
        }
        else if (_gameArea[0] == true && _gameArea[3] == true && _gameArea[6] == true) {
            return true;
        }
        else if (_gameArea[1] == true && _gameArea[4] == true && _gameArea[7] == true) {
            return true;
        }
        else if (_gameArea[2] == true && _gameArea[5] == true && _gameArea[8] == true) {
            return true;
        }
        else if (_gameArea[6] == true && _gameArea[4] == true && _gameArea[2] == true) {
            return true;
        }

        // draw
        return false;
    }

    public int getTurnCounter() {
        Log.d("turncounter: ", String.valueOf(_turnCounter));
        return _turnCounter;
    }

}
