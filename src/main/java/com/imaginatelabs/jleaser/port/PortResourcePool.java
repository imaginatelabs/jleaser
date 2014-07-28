package com.imaginatelabs.jleaser.port;

import com.imaginatelabs.jleaser.core.Lease;
import com.imaginatelabs.jleaser.core.Resource;
import com.imaginatelabs.jleaser.core.ResourcePool;
import com.imaginatelabs.jleaser.core.ResourcePoolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.*;

public class PortResourcePool implements ResourcePool {

    private static final int PORT_LIMIT_FLOOR = 1;
    private static final int PORT_LIMIT_CEILING = 65536;

    private static final int DYNAMIC_PORT_LIMIT_FLOOR = 49152;
    private static final int DYNAMIC_PORT_LIMIT_CEILING = PORT_LIMIT_CEILING;

    private Map<String, Lease> pool = new HashMap<String, Lease>();
    private Logger log = LoggerFactory.getLogger(PortResourcePool.class);
    private Random rand = new Random();

    public PortResourcePool() {
    }

    @Override
    public int getPoolSize() {
        return PORT_LIMIT_CEILING - pool.size();
    }

    @Override
    public int getPoolLimit() {
        return PORT_LIMIT_CEILING;
    }

    @Override
    public Resource acquireLeaseForResource(String configId) throws ResourcePoolException {
        //TODO Implement leasing from a port range #6
        configId = parsePort(configId);
        Lease lease = pool.get(configId);
        if (lease == null) {
            lease = new Lease(new PortResource(configId));
            pool.put(configId, lease);
        } else {
            while (lease.hasLease()) {
                log.debug("Waiting for lease on port: %s", configId);
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

    private String parsePort(String configId) throws PortRangeOutOdBoundsException, PortNumberParseException {
        if (configId.matches("\\*")) {
            return getDynamicPort();
        }

        try {
            int portInt = Integer.parseInt(configId);
            if (!(portInt > PORT_LIMIT_FLOOR && portInt < PORT_LIMIT_CEILING)) {
                throw new PortRangeOutOdBoundsException(
                        "Port %d does not fall between %d and %d",
                        portInt,
                        PORT_LIMIT_FLOOR,
                        PORT_LIMIT_CEILING);
            }
        } catch (NumberFormatException e) {
            throw new PortNumberParseException("Port %s is not a valid port string", configId);
        }
        return configId;
    }

    private String getDynamicPort() {
        for (int dynamicPort = DYNAMIC_PORT_LIMIT_FLOOR; dynamicPort < DYNAMIC_PORT_LIMIT_CEILING; ++dynamicPort) {
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
        return Integer.toString(DYNAMIC_PORT_LIMIT_FLOOR);
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
    public void update() {
        List<String> preAllocatedPorts = getPreAllocatedPorts();
        for (String port : preAllocatedPorts) {
            Lease lease = new Lease(new PortResource(port, true));
            lease.takeLease();
            pool.put(port, lease);
        }
    }

    public List<String> getPreAllocatedPorts() {
        List<String> ports = new ArrayList<String>();
        for (int port = PORT_LIMIT_FLOOR; port < PORT_LIMIT_CEILING; port++) {
            try {
                ServerSocket serverSocket = new ServerSocket(port);
                serverSocket.close();
            } catch (IOException e) {
                ports.add(Integer.toString(port));
            }
        }
        return ports;
    }
}
