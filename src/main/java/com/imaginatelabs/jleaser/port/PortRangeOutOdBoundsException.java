package com.imaginatelabs.jleaser.port;

import com.imaginatelabs.jleaser.core.ResourcePoolException;

public class PortRangeOutOdBoundsException extends ResourcePoolException {
    public PortRangeOutOdBoundsException(String message, Object... args) {
        super(message,args);
    }
}
