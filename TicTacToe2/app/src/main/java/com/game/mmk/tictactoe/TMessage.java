package com.game.mmk.tictactoe;

import java.io.Serializable;

/**
 * Created by 4gray on 08.05.15.
 */
public class TMessage implements Serializable {

    private String subject;
    private String body;
    private String from;

    private static final long serialVersionUID = 1L;

    public TMessage(String subject, String body, String from) {
        this.subject = subject;
        this.body = body;
        this.from = from;
    }


    public String getSubject() {
        return this.subject;
    }

    public String getBody() {
        return this.body;
    }

    public String getFrom() {
        return this.from;
    }


}
