package com.imaginatelabs.jleaser.docker.configuration;

import com.imaginatelabs.jleaser.core.configuration.JleaserConfigurationException;

public interface DockerConfiguration {

    DockerContainerConfiguration getDockerContainerConfiguration(String configId) throws JleaserConfigurationException;

    boolean hasConfiguration(String configId) throws JleaserConfigurationException;
}
