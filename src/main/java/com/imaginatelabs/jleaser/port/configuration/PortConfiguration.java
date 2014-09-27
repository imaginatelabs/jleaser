package com.imaginatelabs.jleaser.port.configuration;

import com.imaginatelabs.jleaser.port.PortUtils;

import java.util.ArrayList;
import java.util.List;

public class PortConfiguration {

    private final List<String> includedPorts;
    private final List<String> excludedPorts;
    private List<String> validPorts;

    public PortConfiguration(List<String> includedPorts, List<String> excludedPorts) {
        this.includedPorts = includedPorts;
        this.excludedPorts = excludedPorts;
    }

    public List<String> getExcludedPorts() {
        return excludedPorts;
    }

    public List<String> getIncludedPorts() {
        return includedPorts;
    }

    public List<String> getValidPorts(){
        if(validPorts != null){
            return validPorts;
        }
        validPorts = new ArrayList<String>(includedPorts);
        for(String excludedPort: excludedPorts){
            if(validPorts.contains(excludedPort)){
                validPorts.remove(excludedPort);
            }
        }
        return validPorts;
    }
}
