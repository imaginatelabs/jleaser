package com.imaginatelabs.jleaser.core;

import com.imaginatelabs.jleaser.docker.DockerResource;
import com.imaginatelabs.jleaser.docker.DockerResourcePool;
import com.imaginatelabs.jleaser.localhost.LocalhostResource;
import com.imaginatelabs.jleaser.localhost.LocalhostResourcePool;
import com.imaginatelabs.jleaser.port.PortResource;
import com.imaginatelabs.jleaser.port.PortResourcePool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class JLeaser {

    public enum ResourceType{
        LOCALHOST(LocalhostResource.class),
        DOCKER(DockerResource.class),
        PORT(PortResource.class);

        Class aClass;
        ResourceType(Class aClass){
            this.aClass = aClass;
        }

        boolean instanceOfClass(Class aClass){
            return this.aClass == aClass;
        }
    }

    private static JLeaser singleton = null;

    private Logger log = LoggerFactory.getLogger(this.getClass());
    private Map<ResourceType, ResourcePool> resourcePools = new HashMap<ResourceType, ResourcePool>(){{
        put(ResourceType.LOCALHOST,new LocalhostResourcePool());
        put(ResourceType.DOCKER, new DockerResourcePool());
        put(ResourceType.PORT, new PortResourcePool());
    }};

    private JLeaser() { }

    public static Resource getLeaseOn(String resourceType, String configId) throws JLeaserException{
        JLeaser leaser = getInstance();
        ResourceType type = validateResourceType(resourceType);
        leaser.log.debug("ResourceType: "+type);
        ResourcePool resourcePool = singleton.resourcePools.get(type);
        leaser.log.debug("Is ResourcePool null: "+(resourcePool == null));
        return resourcePool.acquireLeaseForResource(configId);
    }

    private static JLeaser getInstance() throws JLeaserException {
        if (singleton == null) {
            singleton = new JLeaser();
            singleton.log.debug("Creating singleton");
        }else{
            singleton.log.debug("Using existing singleton");
        }
        singleton.update();
        return singleton;
    }

    public static Resource getLeaseOnLocalHost() throws JLeaserException{
        return getLeaseOn(ResourceType.LOCALHOST.toString(), "localhost");
    }

    public static Resource getLeaseOnPort(String portNumber) throws JLeaserException {
        return getLeaseOn(ResourceType.PORT.toString(), portNumber);
    }

    public static void returnLease(Resource resource) throws JLeaserException {
        JLeaser leaser = getInstance();
        ResourcePool resourcePool = leaser.resourcePools.get(getResourceTypeFromInstanceOf(resource.getClass()));
        resourcePool.returnLeaseForResource(resource);
    }

    public static boolean hasLease(Resource resource) throws JLeaserException {
        JLeaser leaser = getInstance();
        ResourcePool resourcePool = leaser.resourcePools.get(getResourceTypeFromInstanceOf(resource.getClass()));
        return resourcePool.hasLeaseOnResource(resource);
    }

    private static ResourceType validateResourceType(String resourceType) throws InvalidResourceTypeException {
        for(ResourceType type : ResourceType.values()){
            if(resourceType.equalsIgnoreCase(type.toString())){
                return type;
            }
        }
        throw new InvalidResourceTypeException(resourceType);
    }

    private static ResourceType getResourceTypeFromInstanceOf(Class aClass) throws InvalidResourceTypeException {
        for(ResourceType type: ResourceType.values()){
            if(type.instanceOfClass(aClass)){
                return type;
            }
        }
        throw new InvalidResourceTypeException(aClass.toString());
    }

    private void update() throws ResourcePoolException {
        for(ResourcePool resourcePool : resourcePools.values()){
            resourcePool.update();
        }
    }
}
