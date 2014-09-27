package com.imaginatelabs.jleaser.docker.configuration;

import com.imaginatelabs.jleaser.core.JLeaserException;

public class DockerContainerConfiguration {

    enum Initialization{
        LAZY, EAGER
    }

    private final String id;
    private final String baseImage;
    private final int defaultPoolSize;
    private Initialization initialization;

    public DockerContainerConfiguration(String id, String baseImage, int defaultPoolSize, String initialization) throws JLeaserException {
        this.id = id;
        this.baseImage = baseImage;
        this.defaultPoolSize = defaultPoolSize;
        for(Initialization init : Initialization.values()){
            if(init.toString().equalsIgnoreCase(initialization)){
                this.initialization = init;
            }
        }
        if(this.initialization == null){
            throw new DockerContainerConfigurationException(
                    "Initialization must be set to %s or %s but was set to %s",
                    Initialization.EAGER,
                    Initialization.LAZY,
                    initialization
            );
        }
    }

    public String getBaseImage() {
        return baseImage;
    }

    public String getId() {
        return id;
    }

    public int getDefaultPoolSize() {
        return defaultPoolSize;
    }
}
