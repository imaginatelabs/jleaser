package com.imaginatelabs.jleaser.docker;

import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.imaginatelabs.jleaser.TestUtils;
import com.imaginatelabs.jleaser.docker.configuration.DockerContainerConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;


public class DockerIntegrationTest {

    private static Logger log = LoggerFactory.getLogger(DockerIntegrationTest.class);

    @Test(groups = {TestUtils.INTEGRATION, TestUtils.DEPENDS_ON_DOCKER})
    public void shouldInitializeDockerContainer() throws Exception {

        if(DockerUtils.isUsingBoot2Docker()){
            DockerUtils.startBoot2Docker();
        }

        DockerResource resource = new DockerResource(new DockerContainerConfiguration("myId","tutum/mysql",0,"eager"));

        resource.Initialize(new DockerClientImpl(
                DockerClientConfig
                .createDefaultConfigBuilder()
                .withUri(DockerUtils.getDockerLocalApiUrl())
                .build()
            )
        );

        Assert.assertNotNull(resource.getResourceId());
        Assert.assertEquals(resource.getContainerConfig().getImage(), "tutum/mysql");
        Assert.assertTrue(resource.getState().isRunning());

        resource.kill();

        Assert.assertFalse(resource.getState().isRunning());
    }
}