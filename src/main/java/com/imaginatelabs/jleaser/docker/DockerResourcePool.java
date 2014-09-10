package com.imaginatelabs.jleaser.docker;

import com.imaginatelabs.jleaser.core.Lease;
import com.imaginatelabs.jleaser.core.Resource;
import com.imaginatelabs.jleaser.core.ResourcePool;

import java.util.HashMap;
import java.util.Map;

public class DockerResourcePool implements ResourcePool {

    private final DockerConfiguration dockerConfiguration;
    private Map<String,Lease> pool = new HashMap<String, Lease>();
    private Map<String,Map<String,Lease>> templatePool = new HashMap<String, Map<String, Lease>>();



    private int poolLimit = 0; //unlimited
    private Map<String,DockerTemplate> templates;

    public DockerResourcePool(DockerConfiguration dockerConfiguration) {
        this.dockerConfiguration = dockerConfiguration;
    }

    @Override
    public int getPoolSize() {
        return pool.size();
    }

    @Override
    public int getPoolLimit() {
        return poolLimit;
    }

    @Override
    public Resource acquireLeaseForResource(String configId) throws DockerResourcePoolException {
        DockerUtils.validateDockerStatus();
        //if existing template
            //if I can reuse a lease from template pool
                //return lease

        //if I can create a new lease in the template pool
            //create and return new lease
        //else
            //wait for lease
        //return lease

        if(templatePool.containsKey(configId)){
           for(Lease lease: templatePool.get(configId).values()){
               if(!lease.hasLease()){
                   lease.takeLease();
                   //TODO Wait until resource is ready
                   return lease.getResource();
               }
           }
        }

        if(canAddNewLease(configId)){
            DockerResource resource;
            if(dockerConfiguration.hasConfiguration(configId)){
                try {
                    resource = new DockerResource(dockerConfiguration.getDockerContainerConfiguration(configId));
                } catch (DockerContainerConfigurationNotFoundException e) {
                    resource= new DockerResource(configId);
                }
            }else{
                resource= new DockerResource(configId);
            }

            while(!resource.waitUntilResourceIsReady()){
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {

                }
            }
            Lease lease = new Lease(resource);
            addToTemplatePool(configId,resource.getResourceId(),lease);
            pool.put(resource.getResourceId(), lease);
        } else {

        }


        return null;
    }

    private void addToTemplatePool(String configId, String resourceId, Lease lease) {
        if(!templatePool.containsKey(configId)){
            templatePool.put(configId,new HashMap<String, Lease>());
        }
        Map<String, Lease> leaseMap = templatePool.get(configId);
        leaseMap.put(resourceId,lease);
    }

    private boolean canAddNewLease(String configId ) {
        if(pool.size() < poolLimit){
            if(templates.containsKey(configId) && templatePool.containsKey(configId)){
                if(templatePool.get(configId).size() < templates.get(configId).getPoolLimit()){
                    return true;
                }
            }else{
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasLeaseOnResource(Resource resource) {
        return false;
    }

    @Override
    public void returnLeaseForResource(Resource resource) {

    }

    @Override
    public void update() {

    }
}
