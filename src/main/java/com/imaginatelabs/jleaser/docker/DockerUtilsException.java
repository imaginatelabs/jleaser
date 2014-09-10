package com.imaginatelabs.jleaser.docker;

import com.imaginatelabs.jleaser.core.JLeaserException;

import java.io.IOException;

public class DockerUtilsException extends JLeaserException {

    public DockerUtilsException(Throwable e) {
        super(e);
    }

    public DockerUtilsException(String message, Throwable cause) {
        super(message, cause);
    }

    public DockerUtilsException(String message) {
        super(message);
    }
}
