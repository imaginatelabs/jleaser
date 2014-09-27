package com.imaginatelabs.jleaser.core.configuration;

import com.imaginatelabs.jleaser.docker.configuration.DockerConfiguration;
import com.imaginatelabs.jleaser.port.configuration.PortConfiguration;

public class JLeaserConfiguration {

    private PortConfiguration portConfiguration;
    private DockerConfiguration dockerConfiguration;

    public JLeaserConfiguration(PortConfiguration portConfiguration, DockerConfiguration dockerConfiguration) {
        this.portConfiguration = portConfiguration;
        this.dockerConfiguration = dockerConfiguration;
    }

    public DockerConfiguration getDockerConfiguration() {
        return dockerConfiguration;
    }

    public PortConfiguration getPortConfiguration() {
        return portConfiguration;
    }
}
