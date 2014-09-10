package com.imaginatelabs.jleaser.core;

import com.imaginatelabs.jleaser.docker.DockerResource;
import com.imaginatelabs.jleaser.docker.DockerResourcePool;
import com.imaginatelabs.jleaser.localhost.LocalhostResource;
import com.imaginatelabs.jleaser.localhost.LocalhostResourcePool;
import com.imaginatelabs.jleaser.port.PortNumberParseException;
import com.imaginatelabs.jleaser.port.PortRangeOutOdBoundsException;
import com.imaginatelabs.jleaser.port.PortResource;
import com.imaginatelabs.jleaser.port.PortResourcePool;
import org.apache.commons.configuration.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class JLeaser {

    private static JLeaser singleton = null;

    private Logger log = LoggerFactory.getLogger(this.getClass());
    private JLeaserConfiguration config;
    private HashMap<Class, ResourcePool> resourcePools;

    private static JLeaser getInstance() throws JLeaserException {
        try {
            if (singleton == null) {
                singleton = new JLeaser();
                singleton.log.trace("Initialising {} singleton ", singleton.getClass().getSimpleName());
                singleton.initializeConfiguration();
            }else{
                singleton.log.trace("Using existing {} singleton", singleton.getClass().getSimpleName());
            }
            singleton.update();
            return singleton;
        } catch (Exception e) {
            throw new JLeaserException(e);
        }
    }

    private void initializeConfiguration() throws JLeaserException {
        log.trace("Initialising Jleaser configuration");
        config = new JLeaserConfiguration("jleaser.config");
        resourcePools = new HashMap<Class, ResourcePool>(){{
            put(LocalhostResource.class,new LocalhostResourcePool());
            put(DockerResource.class, new DockerResourcePool(config.getDockerConfiguration()));
            put(PortResource.class, new PortResourcePool(config));
        }};
    }

    private void update() throws ResourcePoolException {
        log.trace("Updating resource pools");
        for(ResourcePool resourcePool : resourcePools.values()){
            log.trace("Updating resource pool: {}", resourcePool.getClass().getSimpleName());
            resourcePool.update();
        }
    }

    private ResourcePool getResourcePool(String resourceType) throws JLeaserException {
        for(Class aClass : resourcePools.keySet()){
            if(aClass.getSimpleName().equalsIgnoreCase(resourceType + "Resource")){
                return this.resourcePools.get(aClass);
            }
        }
        throw new InvalidResourceTypeException(resourceType);
    }

    private static ResourcePool getResourcePool(Class<? extends Resource> aClass) throws JLeaserException {
        JLeaser leaser = getInstance();
        if(!leaser.resourcePools.containsKey(aClass)){
            throw new InvalidResourceTypeException(aClass);
        }
        return leaser.resourcePools.get(aClass);
    }

    public static Resource getLeaseOn(String resourceType, String configId) throws JLeaserException{
        JLeaser leaser = getInstance();
        leaser.log.trace("Getting leaser on ResourceType: {}", resourceType);
        ResourcePool resourcePool = leaser.getResourcePool(resourceType);
        leaser.log.trace("Acquired ResourcePool: {}", resourcePool.getClass());
        return resourcePool.acquireLeaseForResource(configId);
    }

    public static Resource getLeaseOnLocalHost() throws JLeaserException{
        return getLeaseOn("localhost", "localhost");
    }

    public static Resource getLeaseOnPort(String portNumber) throws JLeaserException {
        return getLeaseOn("port", portNumber);
    }

    public static Resource getLeaseOnDockerInstance(String configId) throws JLeaserException {
        return getLeaseOn("docker", configId);
    }

    public static void returnLease(Resource resource) throws JLeaserException {
        getResourcePool(resource.getClass()).returnLeaseForResource(resource);
    }

    public static boolean hasLease(Resource resource) throws JLeaserException {
        return getResourcePool(resource.getClass()).hasLeaseOnResource(resource);
    }

    public static void destroyJleaser(){
        singleton = null;
    }
}
