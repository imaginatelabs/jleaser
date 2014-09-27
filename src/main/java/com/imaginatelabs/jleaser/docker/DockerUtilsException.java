package com.imaginatelabs.jleaser.docker;

import com.imaginatelabs.jleaser.core.JLeaserException;

import java.io.IOException;

public class DockerUtilsException extends JLeaserException {

    public DockerUtilsException(String message, Object... args) {
        super(message, args);
    }

    public DockerUtilsException(Throwable e) {
        super(e);
    }

    public DockerUtilsException(Throwable cause, String message, Object... args) {
        super(cause, message, args);
    }
}
