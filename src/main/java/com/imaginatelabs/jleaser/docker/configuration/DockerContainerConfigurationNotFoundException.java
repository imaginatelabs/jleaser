package com.imaginatelabs.jleaser.docker.configuration;

public class DockerContainerConfigurationNotFoundException extends DockerContainerConfigurationException {
    public DockerContainerConfigurationNotFoundException(String message, Object... args) {
        super(message, args);
    }

    public DockerContainerConfigurationNotFoundException(Throwable e) {
        super(e);
    }

    public DockerContainerConfigurationNotFoundException(Throwable cause, String message, Object... args) {
        super(cause, message, args);
    }
}
