package com.imaginatelabs.jleaser.docker;

import com.imaginatelabs.jleaser.core.Resource;

public class DockerResource implements Resource {

    private String config;
    private String ipAddress;
    private String id;
    private String image;
    private String command;
    private String created;
    private String status;
    private String ports; //We might want to validate that two containers don't try to use the same host ports and lock on this
    private String names;
    private String log; //This could have a method tail which monitors the logs in separate process


    public DockerResource(String config) {
        this.config = config;
    }

    @Override
    public String getResourceId() {
        return id;
    }

    @Override
    public String getIpAddress() {
        return ipAddress;
    }

    @Override
    public String getResourceName() {
        return "docker";
    }

    @Override
    public String getConfigId() {
        return config;
    }
}
