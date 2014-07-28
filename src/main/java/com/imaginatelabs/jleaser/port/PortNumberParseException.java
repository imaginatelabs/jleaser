package com.imaginatelabs.jleaser.port;

import com.imaginatelabs.jleaser.core.ResourcePoolException;

public class PortNumberParseException extends ResourcePoolException {
    public PortNumberParseException(String message, Object... args) {
        super(message,args);
    }
}
