package com.imaginatelabs.jleaser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestUtils {

    //Test Groups
    public static final String INTEGRATION = "integration";
    public static final String NON_THREAD_SAFE = "nonThreadSafe";
    public static final String DEPENDS_ON_CONFIG_FILE = "dependsOnConfigFile";
    public static final String DEPENDS_ON_DOCKER = "dependsOnDocker";
    public static final String DEPENDS_ON_OS = "dependsOnOs";
    public static final String DOCKER = "docker";
    public static final String BOOT_2_DOCKER = "boot2Docker";
    public static final String RUNS_ON_WINDOWS = "windows";
    public static final String RUNS_ON_MAC_OSX = "macOsx";
    public static final String UNIX = "unix";

    private static Logger log = LoggerFactory.getLogger("TEST");

    public static void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
            log.trace("Sleeping");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void trace(String format, Object... arguments){
        log.trace(format,arguments);
    }

    private void debug(String format, Object... arguments){
        log.debug(format,arguments);
    }
}
