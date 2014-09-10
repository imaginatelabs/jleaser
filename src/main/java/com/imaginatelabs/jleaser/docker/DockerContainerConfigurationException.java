package com.imaginatelabs.jleaser.docker;

import com.imaginatelabs.jleaser.core.JLeaserException;

public class DockerContainerConfigurationException extends JLeaserException {

    public DockerContainerConfigurationException(String message, Object... args) {
        super(message, args);
    }

    public DockerContainerConfigurationException(Exception e) {
        super(e);
    }
}
