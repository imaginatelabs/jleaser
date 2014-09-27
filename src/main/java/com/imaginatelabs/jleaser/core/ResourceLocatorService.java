package com.imaginatelabs.jleaser.core;

import java.util.HashMap;

public class ResourceLocatorService {

    private HashMap<Class, ResourcePool> resourcePools;

    public ResourceLocatorService(HashMap<Class, ResourcePool> resourcePools) {
        this.resourcePools = resourcePools;
    }

    public ResourcePool getResourcePool(Class<? extends Resource> aClass) throws JLeaserException {
        if(!resourcePools.containsKey(aClass)){
            throw new InvalidResourceTypeException(aClass);
        }
        return resourcePools.get(aClass);
    }

    public Class<? extends Resource> getValidResource(String resourceType) throws InvalidResourceTypeException {
        for(Class aClass : resourcePools.keySet()){
            if(aClass.getSimpleName().equalsIgnoreCase(resourceType + "Resource")){
                return aClass;
            }
        }
        throw new InvalidResourceTypeException(resourceType);
    }
}
