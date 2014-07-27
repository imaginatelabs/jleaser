package com.imaginatelabs.jleaser.localhost;

import com.imaginatelabs.jleaser.TestUtils;
import com.imaginatelabs.jleaser.core.InvalidResourceTypeException;
import com.imaginatelabs.jleaser.core.JLeaser;
import com.imaginatelabs.jleaser.core.Resource;
import com.imaginatelabs.jleaser.docker.DockerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;


public class LocalhostTests {

    private static Logger log = LoggerFactory.getLogger(LocalhostTests.class);

    @Test
    public void shouldCreateLeaseForLocalhostAndThenReturnIt() throws Exception {
        Resource localhost =  JLeaser.getLeaseOnLocalHost();

        Assert.assertTrue(localhost instanceof LocalhostResource);
        Assert.assertTrue(JLeaser.hasLease(localhost));
        Assert.assertEquals(localhost.getResourceId(), "localhost");
        Assert.assertEquals(localhost.getIpAddress(), "127.0.0.1");

        JLeaser.returnLease(localhost);

        Assert.assertFalse(JLeaser.hasLease(localhost));
    }


    volatile boolean blockThread = true;

    @Test
    public void shouldBlockCreatingALeaseWhenLeaseExists() throws Exception {
        final List<String> order = new ArrayList<String>();


        Thread firstLease = new Thread(){

            public void run(){
                order.add("Getting first lease");
                Resource localhost = null;

                try {
                    localhost = JLeaser.getLeaseOnLocalHost();
                } catch (InvalidResourceTypeException e) {
                    e.printStackTrace();
                }

                order.add("Holding first lease");
                blockThread = false;
                TestUtils.sleep(2000);
                try {
                    JLeaser.returnLease(localhost);
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
                Resource localhost = null;

                try {
                    localhost = JLeaser.getLeaseOnLocalHost();
                } catch (InvalidResourceTypeException e) {
                    e.printStackTrace();
                }

                order.add("Holding second lease");
                try {
                    JLeaser.returnLease(localhost);
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
