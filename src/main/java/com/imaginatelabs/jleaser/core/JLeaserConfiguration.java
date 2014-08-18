package com.imaginatelabs.jleaser.core;

import com.imaginatelabs.jleaser.port.PortNumberParseException;
import com.imaginatelabs.jleaser.port.PortRange;
import com.imaginatelabs.jleaser.port.PortRangeOutOdBoundsException;
import com.imaginatelabs.jleaser.port.PortValidator;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JLeaserConfiguration {

    private enum FavouredPortRule {
        INCLUDED, EXCLUDED;
    }

    private XMLConfiguration xml;
    private FavouredPortRule favouredPortRulePortRules = FavouredPortRule.INCLUDED;
    private List<String> excludedPorts = new ArrayList<String>();
    private List<String> includedPorts = new ArrayList<String>();
    private Logger log = LoggerFactory.getLogger(this.getClass());


    public JLeaserConfiguration(String file) throws ConfigurationException, PortNumberParseException, PortRangeOutOdBoundsException {
        try {
            xml = new XMLConfiguration(file);
            this.favouredPortRulePortRules = parseFavourPortRule();
            this.includedPorts.addAll(parseIncludedPorts());
            this.excludedPorts.addAll(parseExcludedPorts());
            if (favouredPortRulePortRules == FavouredPortRule.EXCLUDED) {
                mergePortRules(excludedPorts, includedPorts);
            } else {
                mergePortRules(includedPorts, excludedPorts);
            }
        }catch(ConfigurationException e){
            log.info("Using default configuration, no configuration file named 'jleaser.config' found on the class path.");
        }
    }

    private void mergePortRules(List<String> primaryPorts, List<String> secondaryPorts) {
        for(String primaryPort: primaryPorts){
            if(secondaryPorts.contains(primaryPort)){
                secondaryPorts.remove(primaryPort);
            }
        }
    }

    private FavouredPortRule parseFavourPortRule() {
        return xml.getString("ports.favour")
                .equalsIgnoreCase(
                        FavouredPortRule.EXCLUDED.toString()
                ) ? FavouredPortRule.EXCLUDED : FavouredPortRule.INCLUDED;
    }


    private List<String> parsePorts(String[] portStrings) throws PortNumberParseException, PortRangeOutOdBoundsException {
        List<PortRange> portRanges = new ArrayList<PortRange>();
        for (String portString : portStrings) {
            portRanges.add(PortValidator.parsePortString(portString));
        }

        List<String> ports = new ArrayList<String>();

        for (PortRange portRange : portRanges) {
            int port = portRange.getFloor();
            while (port <= portRange.getCeiling()) {
                ports.add(Integer.toString(port));
                ++port;
            }
        }

        return ports;
    }

    private List<String> parseIncludedPorts() throws PortNumberParseException, PortRangeOutOdBoundsException {
        return parsePorts(xml.getStringArray("ports.included"));
    }

    private List<String> parseExcludedPorts() throws PortNumberParseException, PortRangeOutOdBoundsException {
        return parsePorts(xml.getStringArray("ports.excluded"));
    }

    public List<String> getExcludedPorts() {
        return excludedPorts;
    }

    public List<String> getIncludedPorts() {
        return includedPorts;
    }
}
