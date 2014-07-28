package com.imaginatelabs.jleaser.core;

public class JLeaserException extends Exception{
    public JLeaserException(String message, Object... args) {
        super(String.format(message,args));
    }
}
