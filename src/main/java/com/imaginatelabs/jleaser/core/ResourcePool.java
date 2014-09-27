package com.imaginatelabs.jleaser.core;

public interface ResourcePool {
    int getLeaseCount();

    int getLeaseLimit();

    Resource acquireLeaseForResource(String configId) throws ResourcePoolException;

    void returnLeaseForResource(Resource resource) throws ResourcePoolException;

    boolean hasLeaseOnResource(Resource resource) throws ResourcePoolException;
}
