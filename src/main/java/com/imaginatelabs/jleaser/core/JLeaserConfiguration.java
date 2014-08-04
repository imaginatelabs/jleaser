package com.imaginatelabs.jleaser.core;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

public class JLeaserConfiguration {

    private final XMLConfiguration xmlConfiguration;


    public JLeaserConfiguration(String file) throws ConfigurationException {
        this.xmlConfiguration = new XMLConfiguration(file);
    }
}
