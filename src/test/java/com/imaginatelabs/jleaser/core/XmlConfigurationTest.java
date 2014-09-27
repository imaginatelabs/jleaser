package com.imaginatelabs.jleaser.core;

import com.imaginatelabs.jleaser.TestUtils;
import com.imaginatelabs.jleaser.core.configuration.JLeaserConfiguration;
import com.imaginatelabs.jleaser.core.configuration.JLeaserConfigurationFactory;
import com.imaginatelabs.jleaser.docker.configuration.DockerConfiguration;
import com.imaginatelabs.jleaser.docker.configuration.DockerContainerConfiguration;
import com.imaginatelabs.jleaser.docker.configuration.DockerContainerConfigurationNotFoundException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class XmlConfigurationTest {

    @Test
    public void shouldReadValidConfigurationFromXmlFileForPorts() throws Exception {
        JLeaserConfiguration config = JLeaserConfigurationFactory.getConfigurationFromXml("config/jleaser.config");

        // Test this section of the config file
        //<ports>
        //      <included> 16384 + 1 + 6 + 11 = 16402
        //          *,
        //          49151,
        //          49140-49145,
        //          39140-39150,
        //      </included>
        //      <excluded>
        //          49153,
        //          49160-49170,
        //          39143-39146
        //      </excluded>
        //</ports>

        List<String> excludedPorts = config.getPortConfiguration().getExcludedPorts();
        List<String> includedPorts = config.getPortConfiguration().getIncludedPorts();
        List<String> validPorts = config.getPortConfiguration().getValidPorts();

        Assert.assertTrue(validPorts.contains("49151"));
        Assert.assertTrue(validPorts.contains("49152"));
        Assert.assertTrue(validPorts.contains("49154"));
        Assert.assertTrue(validPorts.contains("39140"));
        Assert.assertTrue(validPorts.contains("39150"));

        Assert.assertFalse(validPorts.contains("49153"));
        Assert.assertFalse(validPorts.contains("49160"));
        Assert.assertFalse(validPorts.contains("49170"));
        Assert.assertFalse(validPorts.contains("39143"));
        Assert.assertFalse(validPorts.contains("39146"));
    }

    @Test
    public void shouldUseDefaultConfigValuesIfConfigFileIsAbsent() throws Exception {
        try {
            JLeaserConfigurationFactory.getConfigurationFromXml("foo.config");
        }catch (Exception e){
            Assert.fail();
        }
    }

    @Test
    public void shouldReadDockerConfigCorrectly() throws Exception {
        DockerConfiguration dockerConfiguration = JLeaserConfigurationFactory
                .getConfigurationFromXml("config/jleaser.config")
                .getDockerConfiguration();

        DockerContainerConfiguration mysqlContainerConfiguration = dockerConfiguration.getDockerContainerConfiguration("tutum/mysql");
        DockerContainerConfiguration mongoContainerConfiguration = dockerConfiguration.getDockerContainerConfiguration("tutum/mongodb");

        Assert.assertEquals(mysqlContainerConfiguration.getId(),"tutum/mysql");
        Assert.assertEquals(mysqlContainerConfiguration.getBaseImage(),"tutum/mysql");
        Assert.assertEquals(mysqlContainerConfiguration.getDefaultPoolSize(),1);

        Assert.assertEquals(mongoContainerConfiguration.getId(),"tutum/mongodb");
        Assert.assertEquals(mongoContainerConfiguration.getBaseImage(),"tutum/mongodb");
        Assert.assertEquals(mongoContainerConfiguration.getDefaultPoolSize(),0);

        try{
            dockerConfiguration.getDockerContainerConfiguration("foo/bar");
            Assert.fail("Get Docker Container Configuration should fail when searching for a config item that doesn't exits");
        }catch(DockerContainerConfigurationNotFoundException e){
            Assert.assertEquals(e.getMessage(),"Configuration for a Docker container with id 'foo/bar' was not found.");
        }
    }
}