package com.imaginatelabs.jleaser.core;

public class Lease {
    private final Resource resource;
    private boolean lease = false;
    private boolean enabledLeasing = true;

    public Lease(Resource resource) {
        this.resource = resource;
    }

    public Lease(Resource resource, boolean enabledLeasing) {
        this.resource = resource;
        this.enabledLeasing = enabledLeasing;
    }

    public Resource getResource() {
        return resource;
    }

    public void takeLease() {
        this.lease = true;
    }

    public boolean hasLease() {
        return lease;
    }

    public void returnLease() {
        this.lease = false;
    }

    public boolean isEnabledLeasing() {
        return enabledLeasing;
    }

    public void setEnabledLeasing(boolean enabledLeasing) {
        this.enabledLeasing = enabledLeasing;
    }
}