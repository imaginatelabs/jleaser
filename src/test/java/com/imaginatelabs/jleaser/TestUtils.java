package com.imaginatelabs.jleaser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestUtils {

    private static Logger log = LoggerFactory.getLogger(TestUtils.class);

    public static void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
            log.trace("Sleeping");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
