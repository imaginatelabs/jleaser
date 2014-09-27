package com.imaginatelabs.jleaser.docker.configuration;

import com.imaginatelabs.jleaser.core.configuration.JleaserConfigurationException;

public class DockerContainerConfigurationException extends JleaserConfigurationException {
    public DockerContainerConfigurationException(String message, Object... args) {
        super(message, args);
    }

    public DockerContainerConfigurationException(Throwable e) {
        super(e);
    }

    public DockerContainerConfigurationException(Throwable cause, String message, Object... args) {
        super(cause, message, args);
    }
}
