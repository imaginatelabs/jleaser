package com.imaginatelabs.jleaser.docker;

import com.imaginatelabs.jleaser.TestUtils;
import com.imaginatelabs.jterminalexec.JTerminalExec;
import org.testng.Assert;
import org.testng.annotations.Test;

public class DockerUtilsIntegrationTest {

    @Test(groups = {TestUtils.INTEGRATION,
            TestUtils.DEPENDS_ON_DOCKER, TestUtils.BOOT_2_DOCKER,
            TestUtils.DEPENDS_ON_OS, TestUtils.RUNS_ON_MAC_OSX, TestUtils.RUNS_ON_WINDOWS})
    public void shouldBeUsingBoot2DockerOnWindowsAndMacOSX() throws Exception {
        Assert.assertTrue(DockerUtils.isUsingBoot2Docker());
    }

    @Test(groups = {TestUtils.INTEGRATION,
            TestUtils.DEPENDS_ON_DOCKER, TestUtils.BOOT_2_DOCKER,
            TestUtils.DEPENDS_ON_OS, TestUtils.RUNS_ON_MAC_OSX, TestUtils.RUNS_ON_WINDOWS})
    public void shouldGetBoot2DockerUrlOnWindowsAndMacOSX() throws Exception {
        Assert.assertEquals(DockerUtils.getDockerLocalApiUrl(), "http://192.168.59.103:2375");
    }

    @Test(groups = {TestUtils.INTEGRATION,
            TestUtils.DEPENDS_ON_DOCKER, TestUtils.BOOT_2_DOCKER,
            TestUtils.DEPENDS_ON_OS, TestUtils.RUNS_ON_MAC_OSX, TestUtils.RUNS_ON_WINDOWS})
    public void shouldThrowExceptionOnWindowsAndMacOSXWhenBoo2DockerIsNotRunning() throws Exception {
        //Setup
        boolean restartDocker = false;
        if(JTerminalExec.exec("boot2docker status").stdOutContains("running")){
            JTerminalExec.exec("boot2docker poweroff");
            restartDocker = true;
        }
        //SUT
        try {
            DockerUtils.validateDockerStatus();
            Assert.fail();
        }catch (DockerResourcePoolException e){
            Assert.assertEquals(e.getMessage(),"Docker is not currently running.");
        }
        //Tear Down
        if(restartDocker) {
            JTerminalExec.exec("boot2docker up");
        }
    }

    @Test(groups = {TestUtils.INTEGRATION,
            TestUtils.DEPENDS_ON_DOCKER, TestUtils.BOOT_2_DOCKER,
            TestUtils.DEPENDS_ON_OS, TestUtils.UNIX})
    public void shouldNotBeUsingBoot2DockerOnUnixSystem() throws Exception {
        Assert.assertFalse(DockerUtils.isUsingBoot2Docker());
    }

    @Test(groups = {TestUtils.INTEGRATION,
            TestUtils.DEPENDS_ON_DOCKER, TestUtils.DOCKER,
            TestUtils.DEPENDS_ON_OS, TestUtils.UNIX})
    public void shouldGetBoot2DockerUrlOnUnixSystem() throws Exception {
        Assert.assertEquals(DockerUtils.getDockerLocalApiUrl(),"http://127.0.0.1:2375");
    }
}