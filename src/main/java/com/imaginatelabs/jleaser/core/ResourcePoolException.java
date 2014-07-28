package com.imaginatelabs.jleaser.core;

public class ResourcePoolException extends JLeaserException{
    public ResourcePoolException(String message, Object... args) {
        super(String.format(message,args));
    }
}
