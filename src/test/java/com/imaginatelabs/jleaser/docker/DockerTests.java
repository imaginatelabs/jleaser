package com.imaginatelabs.jleaser.docker;

import com.imaginatelabs.jleaser.core.JLeaser;
import com.imaginatelabs.jleaser.core.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.*;
import org.testng.annotations.Test;


public class DockerTests {

    private static Logger log = LoggerFactory.getLogger(DockerTests.class);

    @Test
    public void shouldCreateLeaseForDockerContainerAndThenReturnIt() throws Exception {
        Resource docker = JLeaser.getLeaseOn("docker", "tutum/mysql");

        Assert.assertTrue(docker instanceof DockerResource);
        Assert.assertTrue(JLeaser.hasLease(docker));
        Assert.assertEquals(docker.getResourceName(), "mysql1");

        JLeaser.returnLease(docker);

        Assert.assertFalse(JLeaser.hasLease(docker));
    }

}
