package com.imaginatelabs.jleaser.port;

import com.imaginatelabs.jleaser.core.ResourcePoolException;

public class PortNumberParseException extends ResourcePoolException {
    public PortNumberParseException(String message, Object... args) {
        super(message, args);
    }

    public PortNumberParseException(Throwable e) {
        super(e);
    }

    public PortNumberParseException(Throwable cause, String message, Object... args) {
        super(cause, message, args);
    }
}
