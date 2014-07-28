package com.imaginatelabs.jleaser.core;

public interface ResourcePool {
    int getPoolSize();

    int getPoolLimit();

    Resource acquireLeaseForResource(String configId) throws ResourcePoolException;

    void returnLeaseForResource(Resource resource) throws ResourcePoolException;;

    boolean hasLeaseOnResource(Resource resource) throws ResourcePoolException;;

    void update() throws ResourcePoolException;;
}
