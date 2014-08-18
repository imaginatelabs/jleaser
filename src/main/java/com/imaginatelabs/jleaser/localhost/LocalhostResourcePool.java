package com.imaginatelabs.jleaser.localhost;

import com.imaginatelabs.jleaser.core.Lease;
import com.imaginatelabs.jleaser.core.Resource;
import com.imaginatelabs.jleaser.core.ResourcePool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalhostResourcePool implements ResourcePool {

    private Lease localhostLease = new Lease(new LocalhostResource());
    private Logger log = LoggerFactory.getLogger(LocalhostResourcePool.class);

    @Override
    public int getPoolSize() {
        return localhostLease.hasLease()? 1: 0;
    }

    @Override
    public int getPoolLimit() {
        return 1;
    }

    @Override
    public Resource acquireLeaseForResource(String configId) {
        while (localhostLease.hasLease()) {
            log.info("Waiting for lease on localhost");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                log.error(e.getMessage());
            }
        }
        log.info("Localhost is free taking lease");
        localhostLease.takeLease();
        return localhostLease.getResource();
    }

    @Override
    public void returnLeaseForResource(Resource resource) {
        localhostLease.returnLease();
    }

    @Override
    public boolean hasLeaseOnResource(Resource resource) {
        return localhostLease.hasLease();
    }

    @Override
    public void update() {

    }
}
