package com.imaginatelabs.jleaser.port;

import com.imaginatelabs.jleaser.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.*;

public class PortResourcePool implements ResourcePool {

    private Logger log = LoggerFactory.getLogger(this.getClass());
    private Map<String, Lease> pool = new HashMap<String, Lease>();


    public PortResourcePool(JLeaserConfiguration configuration) {
        initialize(configuration);
    }

    private void initialize(JLeaserConfiguration configuration) {
        List<String> includedPorts = configuration.getIncludedPorts();
        List<String> excludedPorts = configuration.getExcludedPorts();
        log.trace("Initializing {} Configuration", this.getClass().getSimpleName());
        if(includedPorts.size() > 0){
            for(int i = PortValidator.FULL_PORT_LIMIT_FLOOR; i < PortValidator.FULL_PORT_LIMIT_CEILING; ++i){
                String port = Integer.toString(i);
                log.trace("Locking Port: {}", port);
                pool.put(port,new Lease(new PortResource(port),false));
            }

            for(String port: includedPorts){
                log.trace("Unlocking Port: {}", port);
                pool.get(port).setEnabledLeasing(true);
            }
        }else if(excludedPorts.size() > 0){
            for(String port : excludedPorts){
                pool.put(port,new Lease(new PortResource(port),false));
            }
        }
    }

    @Override
    public int getPoolSize() {
        return PortValidator.FULL_PORT_LIMIT_CEILING - pool.size();
    }

    @Override
    public int getPoolLimit() {
        return PortValidator.FULL_PORT_LIMIT_CEILING;
    }

    @Override
    public Resource acquireLeaseForResource(String configId) throws ResourcePoolException {
        String port = getDynamicPort(PortValidator.parsePortString(configId));
        Lease lease = pool.get(port);
        if (lease == null) {
            lease = new Lease(new PortResource(port));
            pool.put(port, lease);
        } else {
            if(!lease.isEnabledLeasing()){
                throw new ResourcePoolException("The resource Port: %s is excluded from the leasing pool", lease.getResource().getConfigId());
            }
            while (lease.hasLease()) {
                log.info("Waiting for lease on port: {}", port);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    log.error(e.getMessage());
                }
            }
        }
        lease.takeLease();
        return lease.getResource();
    }

    private String getDynamicPort(PortRange portRange) {
        for (int dynamicPort = portRange.getFloor(); dynamicPort <= portRange.getCeiling() ; ++dynamicPort) {
            String portStr = Integer.toString(dynamicPort);
            //Try to reuse a previously leased port
            if (pool.containsKey(portStr)) {
                if (!pool.get(portStr).hasLease()) {
                    return portStr;
                }
            } else { //Otherwise get a fresh port
                return portStr;
            }
        }
        //This will return the port from the start which you will now need to wait for a lease to become available
        return Integer.toString(portRange.getFloor());
    }

    @Override
    public void returnLeaseForResource(Resource resource) {
        Lease lease = pool.get(resource.getResourceId());
        if (lease != null) {
            lease.returnLease();
        }
    }

    @Override
    public boolean hasLeaseOnResource(Resource resource) {
        Lease lease = pool.get(resource.getResourceId());
        if (lease != null) {
            return lease.hasLease();
        }
        return false;
    }

    @Override
    public void update() { }
}
