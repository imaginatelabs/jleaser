package com.imaginatelabs.jleaser.docker;

import com.imaginatelabs.jleaser.core.JleaserConfigurationException;

public class DockerContainerConfigurationNotFoundException extends JleaserConfigurationException {

    public DockerContainerConfigurationNotFoundException(String message, Object... args) {
        super(message, args);
    }

    public DockerContainerConfigurationNotFoundException(Exception e) {
        super(e);
    }
}
