package com.imaginatelabs.jleaser.core;


public class JLeaserException extends Exception{
    public JLeaserException(String message, Object... args) {
        super(String.format(message,args));
    }

    public JLeaserException(Throwable e) {
        super(e);
    }

    public JLeaserException(Throwable cause,String message, Object... args) {
        super(String.format(message,args), cause);
    }
}
