package com.imaginatelabs.jleaser.core;

public interface ResourcePool {
    int getPoolSize();

    Resource acquireLeaseForResource(String configId);

    void returnLeaseForResource(Resource resource);

    boolean hasLeaseOnResource(Resource resource);

    boolean checkInOnResourcePool();
}
