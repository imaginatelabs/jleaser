package com.imaginatelabs.jleaser.core;

public class ResourcePoolException extends JLeaserException{
    public ResourcePoolException(String message, Object... args) {
        super(String.format(message,args));
    }

    public ResourcePoolException(Exception e) {
        super(e);
    }

    public ResourcePoolException(String message, Throwable cause) {
        super(message, cause);
    }
}
