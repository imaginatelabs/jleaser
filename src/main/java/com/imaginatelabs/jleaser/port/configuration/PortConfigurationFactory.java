package com.imaginatelabs.jleaser.port.configuration;

import com.imaginatelabs.jleaser.port.*;
import org.apache.commons.configuration.XMLConfiguration;

import java.util.ArrayList;
import java.util.List;

public class PortConfigurationFactory {

    public static PortConfiguration getConfigurationFromXml(XMLConfiguration xml) throws PortNumberParseException, PortRangeOutOdBoundsException {
        return getConfiguration(
                PortUtils.parsePorts(xml.getStringArray("ports.included")),
                PortUtils.parsePorts(xml.getStringArray("ports.excluded"))
        );
    }


    public static PortConfiguration getConfigurationFromDefaults() throws PortNumberParseException, PortRangeOutOdBoundsException {
        return getConfiguration(
                PortUtils.parsePorts(new String[]{String.format(
                        "%d-%d",
                        PortUtils.DYNAMIC_PORT_LIMIT_FLOOR,
                        PortUtils.DYNAMIC_PORT_LIMIT_CEILING)}),
                new ArrayList<String>()
        );
    }

    private static PortConfiguration getConfiguration(List<String> includedPorts, List<String> excludedPorts){
        return new PortConfiguration(
                includedPorts,
                excludedPorts
        );
    }
}
