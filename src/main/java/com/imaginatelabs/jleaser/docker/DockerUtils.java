package com.imaginatelabs.jleaser.docker;

import com.imaginatelabs.jterminalexec.*;
import org.apache.commons.exec.CommandLine;

import java.io.IOException;
import java.util.HashMap;

public class DockerUtils {

    public static final String BOOT2DOCKER_URL = "http://192.168.59.103:2375";
    public static final String DOCKER_URL = "http://127.0.0.1:2375";

    static void validateDockerStatus() throws DockerResourcePoolException {
        MultiPlatformCommandLine dockerStatus = new MultiPlatformCommandLine(
                new HashMap<OperatingSystem, CommandLine>(){{
                    put(OperatingSystem.Windows, CommandLine.parse("boot2docker status"));
                    put(OperatingSystem.MacOSX, CommandLine.parse("boot2docker status"));
                    put(OperatingSystem.Default, CommandLine.parse("docker version"));
                }}
        );
        try{
           Results results = JTerminalExec.exec(dockerStatus);
           switch(OperatingSystemUtils.detectOperatingSystem()){
               case Windows: {}
               case MacOSX: {
                   if (results.stdOutContains("running")) {
                       return;
                   }
                   break;
               }
               default: {
                   if (results.stdOutContains("Usage: docker")) {
                       return;
                   }
               }
           }
        }catch(Exception e){
           throw new DockerResourcePoolException("Something went wrong with Docker see stack trace", e);
        }
        throw new DockerResourcePoolException("Docker is not currently running.");
    }

    public static boolean isUsingBoot2Docker(){
        try {
            Results boot2docker = JTerminalExec.exec("boot2docker");
            return boot2docker.stdErrContains("Usage: boot2docker");
        } catch (IOException e) {}
        return false;
    }

    public static String getDockerLocalApiUrl(){
        if(isUsingBoot2Docker()){
            return BOOT2DOCKER_URL;
        }
        return DOCKER_URL;
    }

    public static void startBoot2Docker() throws DockerUtilsException {
        try {
            JTerminalExec.exec("boot2docker up");
        } catch (IOException e) {
            throw new DockerUtilsException(e);
        }
    }
}
