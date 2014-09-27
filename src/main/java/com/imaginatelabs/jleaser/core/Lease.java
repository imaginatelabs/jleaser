package com.imaginatelabs.jleaser.core;

public class Lease {
    private final Resource resource;
    private boolean lease = false;

    public Lease(Resource resource) {
        this.resource = resource;
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
}