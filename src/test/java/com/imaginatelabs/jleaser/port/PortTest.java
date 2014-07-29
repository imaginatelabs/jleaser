package com.imaginatelabs.jleaser.port;

import com.imaginatelabs.jleaser.TestUtils;
import com.imaginatelabs.jleaser.core.InvalidResourceTypeException;
import com.imaginatelabs.jleaser.core.JLeaser;
import com.imaginatelabs.jleaser.core.JLeaserException;
import com.imaginatelabs.jleaser.core.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class PortTest {

    private static Logger log = LoggerFactory.getLogger(PortTest.class);
    volatile boolean blockThread = true;

    @Test
    public void shouldCreateLeaseForSpecificPortsAndThenReturnLease() throws Exception {
        Resource port =  JLeaser.getLeaseOnPort("3000");

        Assert.assertTrue(port instanceof PortResource);
        Assert.assertTrue(JLeaser.hasLease(port));
        Assert.assertEquals(port.getResourceId(), "3000");
        Assert.assertEquals(port.getIpAddress(), "127.0.0.1");

        JLeaser.returnLease(port);

        Assert.assertFalse(JLeaser.hasLease(port));
    }

    @Test
    public void shouldRecreateLeaseForSpecificPortsAndThenReturnLease() throws Exception {
        Resource port0 =  JLeaser.getLeaseOnPort("3000");
        JLeaser.returnLease(port0);

        Resource port =  JLeaser.getLeaseOnPort("3000");
        Assert.assertTrue(port instanceof PortResource);
        Assert.assertTrue(JLeaser.hasLease(port));
        Assert.assertEquals(port.getResourceId(), "3000");
        Assert.assertEquals(port.getIpAddress(), "127.0.0.1");

        JLeaser.returnLease(port);

        Assert.assertFalse(JLeaser.hasLease(port));
    }

    @Test
    public void shouldBlockCreatingALeaseWhenLeaseExists() throws Exception {
        final List<String> order = new ArrayList<String>();


        Thread firstLease = new Thread(){

            public void run(){
                order.add("Getting first lease");
                Resource port = null;

                try {
                    port = JLeaser.getLeaseOnPort("3000");
                } catch (JLeaserException e) {
                    e.printStackTrace();
                }

                order.add("Holding first lease");
                blockThread = false;
                TestUtils.sleep(2000);
                try {
                    JLeaser.returnLease(port);
                } catch (JLeaserException e) {
                    e.printStackTrace();
                }
                order.add("Returned first lease");
            }
        };

        Thread secondLease = new Thread(){
            public void run(){
                while(blockThread){
                    TestUtils.sleep(1000);
                }
                order.add("Getting second lease");
                Resource port = null;

                try {
                    port = JLeaser.getLeaseOnPort("3000");
                } catch (JLeaserException e) {
                    e.printStackTrace();
                }

                order.add("Holding second lease");
                try {
                    JLeaser.returnLease(port);
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

        for(String message: order){
            log.trace(message);
        }

        Assert.assertEquals(order.get(0),"Getting first lease");
        Assert.assertEquals(order.get(1),"Holding first lease");
        Assert.assertEquals(order.get(2),"Getting second lease");
        Assert.assertEquals(order.get(3),"Returned first lease");
        Assert.assertEquals(order.get(4),"Holding second lease");
        Assert.assertEquals(order.get(5),"Returned second lease");
    }

    @Test
    public void shouldHavePreAllocatedPorts() throws Exception {
        PortResourcePool portResourcePool = new PortResourcePool();
        portResourcePool.update();
        log.debug("Resource Port Count {} out of {}",portResourcePool.getPoolSize(),portResourcePool.getPoolLimit());
        Assert.assertTrue((portResourcePool.getPoolLimit() - portResourcePool.getPoolSize()) > 0);
    }

    @Test
    public void shouldGetSecondLeaseFromDynamicPortRangeWithAnyPortArg() throws Exception {
        Resource portResource1 = JLeaser.getLeaseOnPort("*");
        Resource portResource2 = JLeaser.getLeaseOnPort("*");
        int port1 = Integer.parseInt(portResource1.getResourceId());
        int port2 = Integer.parseInt(portResource2.getResourceId());

        Assert.assertEquals(port1,49152);
        Assert.assertEquals(port2,49153);

        JLeaser.returnLease(portResource1);
        JLeaser.returnLease(portResource2);
    }

    @Test
    public void shouldReuseFirstLeaseFromDynamicPortRangeWithAnyPortArg() throws Exception {
        Resource portResource1 = JLeaser.getLeaseOnPort("*");
        int port1 = Integer.parseInt(portResource1.getResourceId());
        Assert.assertEquals(port1,49152);
        JLeaser.returnLease(portResource1);

        Resource portResource2 = JLeaser.getLeaseOnPort("*");
        int port2 = Integer.parseInt(portResource2.getResourceId());
        JLeaser.returnLease(portResource2);
        Assert.assertEquals(port2,49152);
    }

    @Test
    public void shouldGetFirstLeaseFromDynamicPortRangeWithAnyPortArg() throws Exception {
        Resource portResource1 = JLeaser.getLeaseOnPort("*");
        int port1 = Integer.parseInt(portResource1.getResourceId());
        Assert.assertEquals(port1,49152);
        JLeaser.returnLease(portResource1);

        Resource portResource2 = JLeaser.getLeaseOnPort("*");
        int port2 = Integer.parseInt(portResource2.getResourceId());
        Assert.assertEquals(port2,49152);
        JLeaser.returnLease(portResource2);
    }

    @Test
    public void shouldGetFirstLeaseFromSpecifiedPortRange() throws Exception {
        Resource portResource = JLeaser.getLeaseOnPort("50000-50005");
        int port = Integer.parseInt(portResource.getResourceId());

        Assert.assertEquals(port,50000);
        JLeaser.returnLease(portResource);
    }

    @Test
    public void shouldGetSecondLeaseFromSpecifiedPortRange() throws Exception {
        String portRange = "50000-50005";
        Resource portResource1 = JLeaser.getLeaseOnPort(portRange);
        Assert.assertEquals(Integer.parseInt(portResource1.getResourceId()),50000);

        Resource portResource2 = JLeaser.getLeaseOnPort(portRange);
        Assert.assertEquals(Integer.parseInt(portResource2.getResourceId()),50001);

        JLeaser.returnLease(portResource1);
        JLeaser.returnLease(portResource2);
    }

    @Test
    public void shouldReuseFirstLeaseFromSpecifiedPortRange() throws Exception {
        String portRange = "50000-50005";
        Resource portResource1 = JLeaser.getLeaseOnPort(portRange);
        Assert.assertEquals(Integer.parseInt(portResource1.getResourceId()), 50000);
        JLeaser.returnLease(portResource1);

        Resource portResource2 = JLeaser.getLeaseOnPort(portRange);
        Assert.assertEquals(Integer.parseInt(portResource2.getResourceId()), 50000);
        JLeaser.returnLease(portResource2);
    }

    //Test Exception Cases
    @Test
    public void shouldThrowExceptionWhenAlphaNumericStringIsEntered() {
        try {
            String invalidPortNumber = "5000a";
            JLeaser.getLeaseOnPort(invalidPortNumber);
            Assert.fail(String.format("Port number %s should not be possible",invalidPortNumber));
        } catch (JLeaserException e) {
            Assert.assertEquals(e.getMessage(), "Port '5000a' is not a valid port string");
        }
    }

    @Test
    public void shouldThrowExceptionWhenNegativePortNumberIsEntered() {
        try {
            String invalidPortNumber = "-5000";
            JLeaser.getLeaseOnPort(invalidPortNumber);
            Assert.fail(String.format("Port number %s should not be possible",invalidPortNumber));
        } catch (JLeaserException e) {
            Assert.assertEquals(e.getMessage(), "Port '-5000' is not a valid port string");
        }
    }

    @Test
    public void shouldThrowExceptionWhenAnInvalidBackwardsRangeIsEntered() {
        try {
            String invalidPortNumber = "6000-5000";
            JLeaser.getLeaseOnPort(invalidPortNumber);
            Assert.fail(String.format("Port range %s should not be possible",invalidPortNumber));
        } catch (JLeaserException e) {
            Assert.assertEquals(e.getMessage(), "Port range is invalid 6000-5000 - range floor 6000 is larger than range ceiling 6000");
        }
    }

    @Test
    public void shouldThrowExceptionWhenAnInvalidOutOfBoundRangeIsEntered() {
        try {
            String invalidPortNumber = "60000-90000";
            JLeaser.getLeaseOnPort(invalidPortNumber);
            Assert.fail(String.format("Port range %s should not be possible",invalidPortNumber));
        } catch (JLeaserException e) {
            Assert.assertEquals(e.getMessage(), "Port 90000 does not fall between 1 and 65536");
        }
    }

    @Test
    public void shouldThrowExceptionWhenFiveDigitPortIsOutOfPortRange() {
        try {
            String invalidPortNumber = "90000";
            JLeaser.getLeaseOnPort(invalidPortNumber);
            Assert.fail(String.format("Port range %s should not be possible",invalidPortNumber));
        } catch (JLeaserException e) {
            Assert.assertEquals(e.getMessage(), "Port 90000 does not fall between 1 and 65536");
        }
    }

    @Test
    public void shouldThrowExceptionWhenSixDigitPortIsOutOfPortRange() {
        try {
            String invalidPortNumber = "900000";
            JLeaser.getLeaseOnPort(invalidPortNumber);
            Assert.fail(String.format("Port range %s should not be possible",invalidPortNumber));
        } catch (JLeaserException e) {
            Assert.assertEquals(e.getMessage(), "Port 900000 does not fall between 1 and 65536");
        }
    }

}
