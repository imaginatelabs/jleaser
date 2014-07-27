package com.imaginatelabs.jleaser.docker;

import com.imaginatelabs.jleaser.core.InvalidResourceTypeException;
import com.imaginatelabs.jleaser.core.JLeaser;
import com.imaginatelabs.jleaser.core.Resource;
import com.imaginatelabs.jleaser.localhost.LocalhostResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.*;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;


public class DockerTests {

    private static Logger log = LoggerFactory.getLogger(DockerTests.class);

    @Test
    public void shouldCreateLeaseForDockerContainerAndThenReturnIt() throws Exception {
        Resource docker = JLeaser.getLease("docker","tutum/mysql","mysql1");

        Assert.assertTrue(docker instanceof DockerResource);
        Assert.assertTrue(JLeaser.hasLease(docker));
        Assert.assertEquals(docker.getResourceName(), "mysql1");

        JLeaser.returnLease(docker);

        Assert.assertFalse(JLeaser.hasLease(docker));
    }

}
