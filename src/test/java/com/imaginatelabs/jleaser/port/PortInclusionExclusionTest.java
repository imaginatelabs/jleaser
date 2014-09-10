package com.imaginatelabs.jleaser.port;

import com.imaginatelabs.jleaser.TestUtils;
import com.imaginatelabs.jleaser.core.JLeaser;
import com.imaginatelabs.jleaser.core.JLeaserConfiguration;
import com.imaginatelabs.jleaser.core.JLeaserException;
import com.imaginatelabs.jleaser.core.Resource;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PortInclusionExclusionTest {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    /*
        TODO - Gather Non Thread Safe tests into one class
        I have isolated this test because it was failing when run with the other tests
        the reason is that mocking out the config doesn't happen because the Jleaser is
        initialised in a previous test in another thread.
        When I get a few more non thread safe tests I will put them in a special class
        that can be run with a destroy method before each test.
        http://java.dzone.com/articles/testng-run-tests-sequentially
     */
    //TODO try using a port config class
    @Test(groups = {TestUtils.NON_THREAD_SAFE})
    public void shouldIncludeOnlyPortsToThoseSpecifiedSingularlyForIncludesConfig(@Mocked final JLeaserConfiguration config) throws Exception {
        new NonStrictExpectations(){{
            config.getIncludedPorts(); result = new String[]{"3000"};
        }};

        try {
            Resource leaseOnPort = JLeaser.getLeaseOnPort("3000");
            Assert.assertEquals(leaseOnPort.getResourceId(), "3000");
            JLeaser.returnLease(leaseOnPort);
            JLeaser.getLeaseOnPort("3001");
            Assert.fail("Port 3001 should be blocked for leasing and throw an exception.");
        }catch(JLeaserException e){
            Assert.assertEquals(e.getMessage(), "The resource Port: 3001 is excluded from the leasing pool");
        }
    }
}
