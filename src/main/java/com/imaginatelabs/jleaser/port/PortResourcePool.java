package com.imaginatelabs.jleaser.port;


import com.imaginatelabs.jleaser.core.Lease;
import com.imaginatelabs.jleaser.core.Resource;
import com.imaginatelabs.jleaser.core.ResourcePool;
import com.imaginatelabs.jleaser.core.ResourcePoolException;
import com.imaginatelabs.jleaser.port.configuration.PortConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class PortResourcePool implements ResourcePool {

    private Logger log = LoggerFactory.getLogger(this.getClass());
    private Map<String, Lease> portLeasePool = new HashMap<String, Lease>();
    private PortConfiguration portConfiguration;

    public PortResourcePool(PortConfiguration portConfiguration) {
        this.portConfiguration = portConfiguration;
    }

    @Override
    public int getLeaseCount() {
        int count = 0;
        for(Lease lease: portLeasePool.values()){
            if(lease.hasLease()){
                ++count;
            }
        }
        return count;
    }

    @Override
    public int getLeaseLimit() {
        return portConfiguration.getValidPorts().size();
    }

    @Override
    public Resource acquireLeaseForResource(String configId) throws ResourcePoolException {
        String port = getDynamicPort(PortUtils.parsePortString(configId));
        Lease lease = portLeasePool.get(port);
        if (lease == null) {
            lease = new Lease(new PortResource(port));
            portLeasePool.put(port, lease);
        } else {
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
        for (int dynamicPort = portRange.getFloor(); dynamicPort <= portRange.getCeiling(); ++dynamicPort) {
            String portStr = Integer.toString(dynamicPort);
            //Try to reuse a previously leased port
            if (portLeasePool.containsKey(portStr)) {
                if (!portLeasePool.get(portStr).hasLease()) {
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
        Lease lease = portLeasePool.get(resource.getResourceId());
        if (lease != null) {
            lease.returnLease();
        }
    }

    @Override
    public boolean hasLeaseOnResource(Resource resource) {
        Lease lease = portLeasePool.get(resource.getResourceId());
        if (lease != null) {
            return lease.hasLease();
        }
        return false;
    }
}
