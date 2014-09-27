package com.imaginatelabs.jleaser.localhost;

import com.imaginatelabs.jleaser.TestUtils;
import com.imaginatelabs.jleaser.core.*;
import com.imaginatelabs.jleaser.core.configuration.JLeaserConfiguration;
import com.imaginatelabs.jleaser.core.configuration.JLeaserConfigurationFactory;
import com.imaginatelabs.jleaser.port.PortNumberParseException;
import com.imaginatelabs.jleaser.port.PortRangeOutOdBoundsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LocalhostTest {

    private static Logger log = LoggerFactory.getLogger(LocalhostTest.class);

    //Helper Method
    private JLeaser getNewDefaultLocalHostOnlyJLeaserInstance() throws PortNumberParseException, PortRangeOutOdBoundsException {
        final JLeaserConfiguration configuration = JLeaserConfigurationFactory.getConfigurationFromDefaults();
        return new JLeaser(
                configuration,
                new ResourceLocatorService(new HashMap<Class, ResourcePool>() {{
                    put(LocalhostResource.class, new LocalhostResourcePool());
                }})
        );
    }

    @Test
    public void shouldCreateLeaseForLocalhostAndThenReturnIt() throws Exception {
        JLeaser leaser = getNewDefaultLocalHostOnlyJLeaserInstance();

        Resource localhost =  leaser.getLease(LocalhostResource.class,"localhost");

        Assert.assertTrue(localhost instanceof LocalhostResource);
        Assert.assertTrue(leaser.hasLease(localhost));
        Assert.assertEquals(localhost.getResourceId(), "localhost");
        Assert.assertEquals(localhost.getIpAddress(), "127.0.0.1");

        leaser.returnLease(localhost);

        Assert.assertFalse(leaser.hasLease(localhost));
    }

    //Setup for shouldBlockCreatingALeaseWhenLeaseExists
    volatile boolean blockThread = true;

    @Test
    public void shouldBlockCreatingALeaseWhenLeaseExists() throws Exception {
        final JLeaser leaser = getNewDefaultLocalHostOnlyJLeaserInstance();

        final List<String> order = new ArrayList<String>();

        Thread firstLease = new Thread(){

            public void run(){
                order.add("Getting first lease");
                Resource localhost = null;

                try {
                    localhost = leaser.getLease(LocalhostResource.class,"localhost");
                } catch (JLeaserException e) {
                    e.printStackTrace();
                }

                order.add("Holding first lease");
                blockThread = false;
                TestUtils.sleep(2000);
                try {
                    leaser.returnLease(localhost);
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
                Resource localhost = null;

                try {
                    localhost = leaser.getLease(LocalhostResource.class,"localhost");
                } catch (JLeaserException e) {
                    e.printStackTrace();
                }

                order.add("Holding second lease");
                try {
                    leaser.returnLease(localhost);
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
}