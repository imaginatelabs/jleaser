package com.imaginatelabs.jleaser.docker;

import com.imaginatelabs.jleaser.core.ResourcePoolException;

public class DockerResourcePoolException extends ResourcePoolException {
    public DockerResourcePoolException(String message, Object... args) {
        super(message, args);
    }

    public DockerResourcePoolException(Throwable e) {
        super(e);
    }

    public DockerResourcePoolException(Throwable cause, String message, Object... args) {
        super(cause, message, args);
    }
}
