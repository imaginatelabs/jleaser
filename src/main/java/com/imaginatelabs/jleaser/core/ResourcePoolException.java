package com.imaginatelabs.jleaser.core;

public class ResourcePoolException extends JLeaserException{
    public ResourcePoolException(String message, Object... args) {
        super(message, args);
    }

    public ResourcePoolException(Throwable e) {
        super(e);
    }

    public ResourcePoolException(Throwable cause, String message, Object... args) {
        super(cause, message, args);
    }
}
