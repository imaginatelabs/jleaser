package com.imaginatelabs.jleaser.docker;

import com.imaginatelabs.jleaser.core.Resource;
import com.imaginatelabs.jleaser.core.ResourcePool;

public class DockerResourcePool implements ResourcePool {

    public DockerResourcePool() {

    }

    @Override
    public int getPoolSize() {
        return 0;
    }

    @Override
    public Resource acquireLeaseForResource(String configId) {
        return null;
    }

    @Override
    public boolean hasLeaseOnResource(Resource resource) {
        return false;
    }

    @Override
    public void returnLeaseForResource(Resource resource) {

    }

    @Override
    public boolean checkInOnResourcePool() {
        return false;
    }
}
