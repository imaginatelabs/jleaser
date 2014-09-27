package com.imaginatelabs.jleaser.docker.configuration;

import com.imaginatelabs.jleaser.core.JLeaserException;
import org.apache.commons.configuration.HierarchicalConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XmlDockerConfiguration implements DockerConfiguration {

    enum Xml{
        ID("id"),
        BASE_IMAGE("baseImage"),
        CONTAINER_POOL_LIMIT("containerPoolLimit"),
        INITIALIZATION("eager");

        String element;
        Xml(String element){
            this.element = element;
        }
    }

    private Map<String, DockerContainerConfiguration> containers = new HashMap<String, DockerContainerConfiguration>();

    public XmlDockerConfiguration withContainersFromXml(List<HierarchicalConfiguration> containers) throws JLeaserException {
        for(HierarchicalConfiguration container: containers)
        {
            this.containers.put(
                    container.getString(Xml.ID.element),
                    new DockerContainerConfiguration(
                            container.getString(Xml.ID.element),
                            container.getString(Xml.BASE_IMAGE.element),
                            container.getInt(Xml.CONTAINER_POOL_LIMIT.element),
                            Xml.INITIALIZATION.element));
        }
        return this;
    }

    public DockerContainerConfiguration getDockerContainerConfiguration(String configId) throws DockerContainerConfigurationNotFoundException {
        if(!containers.containsKey(configId)){
            throw new DockerContainerConfigurationNotFoundException("Configuration for a Docker container with id '%s' was not found.",configId);
        }
        return containers.get(configId);
    }

    public boolean hasConfiguration(String configId) {
        return containers.containsKey(configId);
    }
}
