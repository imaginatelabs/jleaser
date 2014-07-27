package com.imaginatelabs.jleaser.localhost;

import com.imaginatelabs.jleaser.core.Resource;

public class LocalhostResource implements Resource {

    private static final String LOCALHOST = "localhost";
    private String ipAddress = "127.0.0.1";

    @Override
    public String getResourceId() {
        return LOCALHOST;
    }

    @Override
    public String getIpAddress() {
        return ipAddress;
    }

    @Override
    public String getResourceName() {
        return LOCALHOST;
    }

    @Override
    public String getConfigId() {
        return LOCALHOST;
    }
}
