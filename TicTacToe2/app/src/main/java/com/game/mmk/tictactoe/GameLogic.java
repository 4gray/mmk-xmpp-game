package com.game.mmk.tictactoe;

import android.util.Log;

/**
 * Created by 4gray on 13.05.15.
 */
public class GameLogic {

    private boolean[] _gameArea = null;
    private int _turnCounter = 0;
    private String _starter = null;

    private static GameLogic ourInstance = new GameLogic();

    public static GameLogic getInstance() {
        return ourInstance;
    }

    private GameLogic() {
        initNewGame();
    }

    /*
        Initiate new game - reset all variables to default
     */

    public void initNewGame() {
        _gameArea = new boolean[9];
        _turnCounter = 0;

        for (int i=0;i<=8;i++) {
            _gameArea[i] = false;
        }
    }

    /*
        Method is called on each turn and fills array with coordinates, which are checked on game area
     */

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

    /*
        Method is calling on each turn and contains all win cases, which returns true
     */
    public boolean gameDecision() {
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

        return false;
    }

    /*
        Method counts all turns - important for draw decision
     */
    public int getTurnCounter() {
        return _turnCounter;
    }

    /*
        Method sets who start with the first turn
     */
    public void setStarter(String starter) {
        this._starter = starter;
    }

    /*
        Returns user who start at first
     */
    public String getStarter() {
        return _starter;
    }

}
