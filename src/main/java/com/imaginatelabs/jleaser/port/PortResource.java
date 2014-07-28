package com.imaginatelabs.jleaser.port;

import com.imaginatelabs.jleaser.core.Resource;

public class PortResource implements Resource {
    private String port;
    private boolean preAllocated;

    public PortResource(String port) {
        this.port = port;
        this.preAllocated = false;
    }

    public PortResource(String port, boolean preAllocated) {
        this.port = port;
        this.preAllocated = preAllocated;
    }

    @Override
    public String getResourceId() {
        return port;
    }

    @Override
    public String getIpAddress() {
        return "127.0.0.1";
    }

    @Override
    public String getResourceName() {
        return "port";
    }

    @Override
    public String getConfigId() {
        return port;
    }

    public boolean isPreAllocated() {
        return preAllocated;
    }
}
