package com.imaginatelabs.jleaser.core;

import com.imaginatelabs.jleaser.core.configuration.JLeaserConfiguration;
import com.imaginatelabs.jleaser.core.configuration.JLeaserConfigurationFactory;
import com.imaginatelabs.jleaser.docker.DockerResource;
import com.imaginatelabs.jleaser.docker.DockerResourcePool;
import com.imaginatelabs.jleaser.localhost.LocalhostResource;
import com.imaginatelabs.jleaser.localhost.LocalhostResourcePool;
import com.imaginatelabs.jleaser.port.PortResource;
import com.imaginatelabs.jleaser.port.PortResourcePool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.HashMap;

public class JLeaser {

    private static JLeaser singleton = null;
    private static Logger sLog = LoggerFactory.getLogger(JLeaser.class);

    private JLeaserConfiguration configuration;
    private ResourceLocatorService resourceLocatorService;

    /**
     * Provides an instance of Jleaser that can manage leases across threads.
     *
     * @return singleton instance of JLeaser
     * @throws JLeaserException
     */
    private static JLeaser getInstance() throws JLeaserException {
        return (singleton == null) ? setSingletonInstance() : singleton;
    }

    private static JLeaser setSingletonInstance() throws JLeaserException {
        sLog.trace("Initialising new {} singleton ", JLeaser.class);
        //TODO Pass in config via -DjLeaserConfigFile property
        final JLeaserConfiguration xmlConfig = JLeaserConfigurationFactory.getConfigurationFromXml("jleaser.config");

        singleton =  new JLeaser(
                xmlConfig,
                new ResourceLocatorService(new HashMap<Class, ResourcePool>() {{
                    put(LocalhostResource.class, new LocalhostResourcePool());
                    put(DockerResource.class, new DockerResourcePool(xmlConfig.getDockerConfiguration()));
                    put(PortResource.class, new PortResourcePool(xmlConfig.getPortConfiguration()));
                }})
        );
        return singleton;
    }

    public static void destroyJleaser(){
        singleton = null;
    }

    public static Resource getLeaseOn(Class<? extends Resource> resourceType, String configId) throws JLeaserException{
        sLog.trace("Getting leaser on ResourceType: {}", resourceType);
        return getInstance().getLease(resourceType, configId);
    }

    public static void returnLeaseOn(Resource resource) throws JLeaserException {
        getInstance().returnLease(resource);
    }

    public static boolean hasLeaseOn(Resource resource) throws JLeaserException {
        return getInstance().hasLease(resource);
    }

    //Helper Methods
    public static Resource getLeaseOn(String resourceType, String configId) throws JLeaserException{
        return getLeaseOn(getInstance().getResourceLocatorService().getValidResource(resourceType), configId);
    }

    public static Resource getLeaseOnLocalHost() throws JLeaserException{
        return getLeaseOn(LocalhostResource.class, "localhost");
    }

    public static Resource getLeaseOnPort(String portNumber) throws JLeaserException {
        return getLeaseOn(PortResource.class, portNumber);
    }

    public static Resource getLeaseOnDockerInstance(String configId) throws JLeaserException {
        return getLeaseOn(DockerResource.class, configId);
    }

    /**
     * JLeaser Constructor
     *
     * Use the static method JLeaser.getInstance() to manage leases across threads.
     *
     * @param configuration
     * @param resourceLocatorService
     */

    public JLeaser(JLeaserConfiguration configuration, ResourceLocatorService resourceLocatorService) {
        this.configuration = configuration;
        this.resourceLocatorService = resourceLocatorService;
    }

    private ResourcePool getResourcePool(Class<? extends Resource> aClass) throws JLeaserException {
        return resourceLocatorService.getResourcePool(aClass);
    }

    public ResourceLocatorService getResourceLocatorService() {
        return resourceLocatorService;
    }

    public JLeaserConfiguration getConfiguration() {
        return configuration;
    }

    public Resource getLease(Class<? extends Resource> resourceType, String configId) throws JLeaserException{
        return this.getResourcePool(resourceType).acquireLeaseForResource(configId);
    }

    public void returnLease(Resource resource) throws JLeaserException {
        this.getResourcePool(resource.getClass()).returnLeaseForResource(resource);
    }

    public boolean hasLease(Resource resource) throws JLeaserException {
        return this.getResourcePool(resource.getClass()).hasLeaseOnResource(resource);
    }
}
