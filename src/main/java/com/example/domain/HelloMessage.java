package com.example.domain;

/**
 * Created by Xue on 02/17/17.
 */
public class HelloMessage {
    private String command;

    public HelloMessage() {
    }

    public HelloMessage(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
