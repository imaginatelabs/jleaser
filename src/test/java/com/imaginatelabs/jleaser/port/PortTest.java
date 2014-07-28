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
    public void shouldGetFirstLeaseFromDynamicPortRangeWithAnyPortArg() throws Exception {
        Resource portResource = JLeaser.getLeaseOnPort("*");
        int port = Integer.parseInt(portResource.getResourceId());

        Assert.assertEquals(port,49152);
    }

    @Test
    public void shouldGetSecondLeaseFromDynamicPortRangeWithAnyPortArg() throws Exception {
        Resource portResource1 = JLeaser.getLeaseOnPort("*");
        Resource portResource2 = JLeaser.getLeaseOnPort("*");
        int port1 = Integer.parseInt(portResource1.getResourceId());
        int port2 = Integer.parseInt(portResource2.getResourceId());

        Assert.assertEquals(port1,49152);
        Assert.assertEquals(port2,49153);
    }

    @Test
    public void shouldReuseFirstLeaseFromDynamicPortRangeWithAnyPortArg() throws Exception {
        Resource portResource1 = JLeaser.getLeaseOnPort("*");
        int port1 = Integer.parseInt(portResource1.getResourceId());
        Assert.assertEquals(port1,49152);
        JLeaser.returnLease(portResource1);

        Resource portResource2 = JLeaser.getLeaseOnPort("*");
        int port2 = Integer.parseInt(portResource2.getResourceId());
        Assert.assertEquals(port2,49152);
    }

    @Test
    public void shouldTestRandom() throws Exception {
        List<Integer> excludeRows = new ArrayList<Integer>(){{
            add(10);
            add(11);
            add(12);
            add(13);
            add(15);
        }};
        Random rand = new Random();

        int start = 10;
        int end = 15;
        int range = end - start + 1;
        log.debug("\nStart {} \nEnd {} \nRange {}",start,end,range);
        int random;
        do{
            random = start + rand.nextInt(range);
            log.debug("Random: {}",random);
        }while(excludeRows.contains(random));

        log.debug("Random Number is between {} and {}", start,end);
        Assert.assertEquals(random,14);
    }
}
