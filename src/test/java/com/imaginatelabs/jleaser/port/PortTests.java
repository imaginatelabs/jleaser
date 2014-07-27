package com.imaginatelabs.jleaser.port;

import com.imaginatelabs.jleaser.TestUtils;
import com.imaginatelabs.jleaser.core.InvalidResourceTypeException;
import com.imaginatelabs.jleaser.core.JLeaser;
import com.imaginatelabs.jleaser.core.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;


public class PortTests {

    private static Logger log = LoggerFactory.getLogger(PortTests.class);
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
                } catch (InvalidResourceTypeException e) {
                    e.printStackTrace();
                }

                order.add("Holding first lease");
                blockThread = false;
                TestUtils.sleep(2000);
                try {
                    JLeaser.returnLease(port);
                } catch (InvalidResourceTypeException e) {
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
                } catch (InvalidResourceTypeException e) {
                    e.printStackTrace();
                }

                order.add("Holding second lease");
                try {
                    JLeaser.returnLease(port);
                } catch (InvalidResourceTypeException e) {
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

}
