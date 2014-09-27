package com.imaginatelabs.jleaser.port;

import com.imaginatelabs.jleaser.TestUtils;
import com.imaginatelabs.jleaser.core.*;
import com.imaginatelabs.jleaser.core.configuration.JLeaserConfiguration;
import com.imaginatelabs.jleaser.core.configuration.JLeaserConfigurationFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class LeasePortTest {

    private static Logger log = LoggerFactory.getLogger(LeasePortTest.class);
    volatile boolean blockThread = true;

    //Helper Method
    private JLeaser getNewDefaultPortOnlyJLeaserInstance() throws PortNumberParseException, PortRangeOutOdBoundsException {
        final JLeaserConfiguration configuration = JLeaserConfigurationFactory.getConfigurationFromDefaults();
        return new JLeaser(
                configuration,
                new ResourceLocatorService(new HashMap<Class, ResourcePool>() {{
                    put(PortResource.class, new PortResourcePool(configuration.getPortConfiguration()));
                }})
        );
    }

    @Test
    public void shouldCreateLeaseForSpecificPortsAndThenReturnLease() throws Exception {
        JLeaser leaser = getNewDefaultPortOnlyJLeaserInstance();
        Resource port = leaser.getLease(PortResource.class,"49152");

        Assert.assertTrue(port instanceof PortResource);
        Assert.assertTrue(leaser.hasLease(port));
        Assert.assertEquals(port.getResourceId(), "49152");
        Assert.assertEquals(port.getIpAddress(), "127.0.0.1");

        leaser.returnLease(port);

        Assert.assertFalse(leaser.hasLease(port));
    }

    @Test
    public void shouldRecreateLeaseForSpecificPortsAndThenReturnLease() throws Exception {
        JLeaser leaser = getNewDefaultPortOnlyJLeaserInstance();
        Resource port0 = leaser.getLease(PortResource.class,"49152");
        leaser.returnLease(port0);

        Resource port = leaser.getLease(PortResource.class,"49152");
        Assert.assertTrue(port instanceof PortResource);
        Assert.assertTrue(leaser.hasLease(port));
        Assert.assertEquals(port.getResourceId(), "49152");
        Assert.assertEquals(port.getIpAddress(), "127.0.0.1");

        leaser.returnLease(port);

        Assert.assertFalse(leaser.hasLease(port));
    }

    @Test
    public void shouldBlockCreatingALeaseWhenLeaseExists() throws Exception {
        final List<String> order = new ArrayList<String>();
        final JLeaser leaser = getNewDefaultPortOnlyJLeaserInstance();

        Thread firstLease = new Thread() {

            public void run() {
                order.add("Getting first lease");
                Resource port = null;

                try {
                    port = leaser.getLease(PortResource.class,"49152");
                } catch (JLeaserException e) {
                    e.printStackTrace();
                }

                order.add("Holding first lease");
                blockThread = false;
                TestUtils.sleep(2000);
                try {
                    leaser.returnLease(port);
                } catch (JLeaserException e) {
                    e.printStackTrace();
                }
                order.add("Returned first lease");
            }
        };

        Thread secondLease = new Thread() {
            public void run() {
                while (blockThread) {
                    TestUtils.sleep(1000);
                }
                order.add("Getting second lease");
                Resource port = null;

                try {
                    port = leaser.getLease(PortResource.class,"49152");
                } catch (JLeaserException e) {
                    e.printStackTrace();
                }

                order.add("Holding second lease");
                try {
                    leaser.returnLease(port);
                } catch (JLeaserException e) {
                    e.printStackTrace();
                }
                order.add("Returned second lease");
            }
        };

        firstLease.start();
        secondLease.start();

        firstLease.join();
        secondLease.join();

        for (String message : order) {
            log.trace(message);
        }

        Assert.assertEquals(order.get(0), "Getting first lease");
        Assert.assertEquals(order.get(1), "Holding first lease");
        Assert.assertEquals(order.get(2), "Getting second lease");
        Assert.assertEquals(order.get(3), "Returned first lease");
        Assert.assertEquals(order.get(4), "Holding second lease");
        Assert.assertEquals(order.get(5), "Returned second lease");
    }

    @Test
    public void shouldGetSecondLeaseFromDynamicPortRangeWithAnyPortArg() throws Exception {
        JLeaser leaser = getNewDefaultPortOnlyJLeaserInstance();

        Resource portResource1 = leaser.getLease(PortResource.class, "*");
        Resource portResource2 = leaser.getLease(PortResource.class, "*");

        Assert.assertEquals(portResource1.getResourceId(), "49152");
        Assert.assertEquals(portResource2.getResourceId(), "49153");
    }


    @Test
    public void shouldReuseFirstLeaseFromDynamicPortRangeWithAnyPortArg() throws Exception {
        JLeaser leaser = getNewDefaultPortOnlyJLeaserInstance();

        Resource portResource1 = leaser.getLease(PortResource.class, "*");
        Assert.assertEquals(portResource1.getResourceId(), "49152");
        leaser.returnLease(portResource1);

        Resource portResource2 = leaser.getLease(PortResource.class, "*");
        leaser.returnLease(portResource2);
        Assert.assertEquals(portResource2.getResourceId(), "49152");
    }

    @Test
    public void shouldGetFirstLeaseFromDynamicPortRangeWithAnyPortArg() throws Exception {
        JLeaser leaser = getNewDefaultPortOnlyJLeaserInstance();

        Resource portResource1 = leaser.getLease(PortResource.class, "*");
        Assert.assertEquals(portResource1.getResourceId(), "49152");
        leaser.returnLease(portResource1);

        Resource portResource2 = leaser.getLease(PortResource.class, "*");
        Assert.assertEquals(portResource2.getResourceId(), "49152");
        leaser.returnLease(portResource2);
    }

    @Test
    public void shouldGetFirstLeaseFromSpecifiedPortRange() throws Exception {
        JLeaser leaser = getNewDefaultPortOnlyJLeaserInstance();
        Resource portResource = leaser.getLease(PortResource.class, "50000-50005");

        Assert.assertEquals(portResource.getResourceId(), "50000");
    }

    @Test
    public void shouldGetSecondLeaseFromSpecifiedPortRange() throws Exception {
        JLeaser leaser = getNewDefaultPortOnlyJLeaserInstance();
        String portRange = "50000-50005";

        Resource portResource1 = leaser.getLease(PortResource.class, portRange);
        Assert.assertEquals(portResource1.getResourceId(), "50000");

        Resource portResource2 = leaser.getLease(PortResource.class, portRange);
        Assert.assertEquals(portResource2.getResourceId(), "50001");
    }

    @Test
    public void shouldReuseFirstLeaseFromSpecifiedPortRange() throws Exception {
        JLeaser leaser = getNewDefaultPortOnlyJLeaserInstance();
        String portRange = "50000-50005";

        Resource portResource1 = leaser.getLease(PortResource.class, portRange);
        Assert.assertEquals(portResource1.getResourceId(), "50000");
        leaser.returnLease(portResource1);

        Resource portResource2 = leaser.getLease(PortResource.class, portRange);
        Assert.assertEquals(portResource2.getResourceId(), "50000");
        leaser.returnLease(portResource2);
    }

    //Test Exception Cases
    @Test
    public void shouldThrowExceptionWhenAlphaNumericStringIsEntered() {
        try {
            String invalidPortNumber = "5000a";
            getNewDefaultPortOnlyJLeaserInstance().getLease(PortResource.class, invalidPortNumber);
            Assert.fail(String.format("Port number %s should not be possible", invalidPortNumber));
        } catch (JLeaserException e) {
            Assert.assertEquals(e.getMessage(), "Port '5000a' is not a valid port string");
        }
    }

    @Test
    public void shouldThrowExceptionWhenNegativePortNumberIsEntered() {
        try {
            String invalidPortNumber = "-5000";
            getNewDefaultPortOnlyJLeaserInstance().getLease(PortResource.class, invalidPortNumber);
            Assert.fail(String.format("Port number %s should not be possible", invalidPortNumber));
        } catch (JLeaserException e) {
            Assert.assertEquals(e.getMessage(), "Port '-5000' is not a valid port string");
        }
    }

    @Test
    public void shouldThrowExceptionWhenAnInvalidBackwardsRangeIsEntered() {
        try {
            String invalidPortNumber = "6000-5000";
            getNewDefaultPortOnlyJLeaserInstance().getLease(PortResource.class, invalidPortNumber);
            Assert.fail(String.format("Port range %s should not be possible", invalidPortNumber));
        } catch (JLeaserException e) {
            Assert.assertEquals(e.getMessage(), "Port range is invalid 6000-5000 - range floor 6000 is larger than range ceiling 5000");
        }
    }

    @Test
    public void shouldThrowExceptionWhenAnInvalidOutOfBoundRangeIsEntered() {
        try {
            String invalidPortNumber = "60000-90000";
            getNewDefaultPortOnlyJLeaserInstance().getLease(PortResource.class, invalidPortNumber);
            Assert.fail(String.format("Port range %s should not be possible", invalidPortNumber));
        } catch (JLeaserException e) {
            Assert.assertEquals(e.getMessage(), "Port 90000 does not fall between 1 and 65536");
        }
    }

    @Test
    public void shouldThrowExceptionWhenFiveDigitPortIsOutOfPortRange() {
        try {
            String invalidPortNumber = "90000";
            getNewDefaultPortOnlyJLeaserInstance().getLease(PortResource.class, invalidPortNumber);
            Assert.fail(String.format("Port range %s should not be possible", invalidPortNumber));
        } catch (JLeaserException e) {
            Assert.assertEquals(e.getMessage(), "Port 90000 does not fall between 1 and 65536");
        }
    }

    @Test
    public void shouldThrowExceptionWhenSixDigitPortIsOutOfPortRange() {
        try {
            String invalidPortNumber = "900000";
            getNewDefaultPortOnlyJLeaserInstance().getLease(PortResource.class, invalidPortNumber);
            Assert.fail(String.format("Port range %s should not be possible", invalidPortNumber));
        } catch (JLeaserException e) {
            Assert.assertEquals(e.getMessage(), "Port 900000 does not fall between 1 and 65536");
        }
    }
}