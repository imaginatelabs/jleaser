package com.imaginatelabs.jleaser.docker.configuration;

import com.imaginatelabs.jleaser.core.JLeaserException;
import org.apache.commons.configuration.XMLConfiguration;

public class DockerConfigurationFactory {
    public static DockerConfiguration getConfigurationFromXml(XMLConfiguration xml) throws JLeaserException {
        return new XmlDockerConfiguration().withContainersFromXml(xml.configurationsAt("docker.containers.container"));
    }

    public static DockerConfiguration getConfigurationFromDefaults() {
        return null;
    }
}
