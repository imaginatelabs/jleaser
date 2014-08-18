package com.imaginatelabs.jleaser.port;

import com.imaginatelabs.jleaser.core.Resource;

public class PortResource implements Resource {
    private String port;

    public PortResource(String port) {
        this.port = port;
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

}
