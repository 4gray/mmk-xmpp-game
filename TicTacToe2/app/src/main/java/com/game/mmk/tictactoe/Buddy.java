package com.game.mmk.tictactoe;

/**
 * Created by 4gray on 19.05.15.
 */
public class Buddy {

    private String name = null;
    private String status = null;
    private String type = null;

    public Buddy(String name, String status, String type) {
        this.name = name;
        this.status = status;
        this.type = type;
    }

    public String getName() {
        return this.name;
    }

    public String getStatus() {
        return this.status;
    }

    public String getType() {
        return type;
    }
}
