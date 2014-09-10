package com.imaginatelabs.jleaser.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse.ContainerState;
import com.github.dockerjava.api.model.ContainerConfig;
import com.imaginatelabs.jleaser.core.Resource;

public class DockerResource implements Resource {

    private final String configId;
    private final String baseImage;
    private String containerId;
    private DockerClient dockerClient;
    private CreateContainerResponse container;

    public DockerResource(DockerContainerConfiguration containerConfiguration) {
        this.configId = containerConfiguration.getId();
        this.baseImage =containerConfiguration.getBaseImage();
    }

    public DockerResource(String  configId) {
        this.configId = configId;
        this.baseImage = configId;
    }

    public void Initialize(DockerClient dockerClient){
        this.dockerClient = dockerClient;
        this.container = this.dockerClient.createContainerCmd(this.baseImage).exec();
        this.containerId = container.getId();
        this.dockerClient.startContainerCmd(containerId).exec();
    }

    @Override
    public String getResourceId() {
        return containerId;
    }

    @Override
    public String getIpAddress() {
        return dockerClient.inspectContainerCmd(containerId).exec().getConfig().getDomainName();
    }

    @Override
    public String getResourceName() {
        return dockerClient.inspectContainerCmd(containerId).exec().getConfig().getHostName();
    }

    @Override
    public String getConfigId() {
        return configId;
    }

    public boolean waitUntilResourceIsReady() {
        try{
            return dockerClient.waitContainerCmd(containerId).exec() == 0;
        }catch(Exception e){
            return false;
        }
    }

    public ContainerConfig getContainerConfig() {
        return dockerClient.inspectContainerCmd(containerId).exec().getConfig();
    }

    public DockerClient getDockerClient() {
        return dockerClient;
    }

    public CreateContainerResponse getContainer() {
        return container;
    }

    public void kill() {
        dockerClient.stopContainerCmd(containerId).exec();
        dockerClient.killContainerCmd(containerId).exec();
    }

    public ContainerState getState() {
        return dockerClient.inspectContainerCmd(containerId).exec().getState();
    }
}
