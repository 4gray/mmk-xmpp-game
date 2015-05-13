package com.game.mmk.tictactoe;

import android.util.Log;

/**
 * Created by 4gray on 12.05.15.
 */
public class Turn {

    private String x;
    private String y;


    public Turn(String coordinates) {
        this.x = coordinates.substring(0,1);
        this.y = coordinates.substring(1,2);
        Log.d("x: ", this.x);
        Log.d("y: ", this.y);
    }

    public String getX() {
        return x;
    }

    public String getY() {
        return y;
    }

    public void setX(String x) {
        this.x = x;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String toString() {
        return x+y;
    }
}
