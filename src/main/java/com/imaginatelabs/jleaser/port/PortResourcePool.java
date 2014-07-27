package com.imaginatelabs.jleaser.port;

import com.imaginatelabs.jleaser.core.Lease;
import com.imaginatelabs.jleaser.core.Resource;
import com.imaginatelabs.jleaser.core.ResourcePool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class PortResourcePool implements ResourcePool {

    private Map<String,Lease> pool = new HashMap<String, Lease>();
    private Logger log = LoggerFactory.getLogger(PortResourcePool.class);

    @Override
    public int getPoolSize() {
        //TODO Calculate this based on the ports running on the OS and the total ports
        return 0;
    }

    @Override
    public Resource acquireLeaseForResource(String configId) {
        PortResource portResource;

        if(!isValidPortRequest(configId)){
//            throw new Exception();
        }
        Lease lease = pool.get(configId);
        if(lease == null){
            lease = new Lease(new PortResource(configId));
            pool.put(configId,lease);
        }
        else{
            while (lease.hasLease()) {
                log.debug("Waiting for lease on port: %s",configId);
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

    private boolean isValidPortRequest(String configId) {
        try {
            Integer.parseInt(configId);
            return true;
        }
        catch(NumberFormatException e){}
        return false;
    }

    @Override
    public void returnLeaseForResource(Resource resource) {
        Lease lease = pool.get(resource.getResourceId());
        if(lease != null){
            lease.returnLease();
        }
    }

    @Override
    public boolean hasLeaseOnResource(Resource resource) {
        Lease lease = pool.get(resource.getResourceId());
        if(lease != null){
            return lease.hasLease();
        }
        return false;
    }

    @Override
    public boolean checkInOnResourcePool() {
        return false;
    }
}
