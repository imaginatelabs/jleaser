package com.imaginatelabs.jleaser.port;

import com.imaginatelabs.jleaser.core.ResourcePoolException;

public class PortRangeOutOdBoundsException extends ResourcePoolException {
    public PortRangeOutOdBoundsException(String message, Object... args) {
        super(message, args);
    }

    public PortRangeOutOdBoundsException(Throwable e) {
        super(e);
    }

    public PortRangeOutOdBoundsException(Throwable cause, String message, Object... args) {
        super(cause, message, args);
    }
}
