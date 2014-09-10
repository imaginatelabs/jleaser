package com.imaginatelabs.jleaser.docker;

import com.imaginatelabs.jleaser.core.ResourcePoolException;

public class DockerResourcePoolException extends ResourcePoolException {

    public DockerResourcePoolException(String message, Object... args) {
        super(message, args);
    }

    public DockerResourcePoolException(String message, Throwable cause) {
        super(message, cause);
    }
}
