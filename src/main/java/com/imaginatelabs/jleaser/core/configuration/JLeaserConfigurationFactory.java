package com.imaginatelabs.jleaser.core.configuration;

import com.imaginatelabs.jleaser.core.JLeaserException;
import com.imaginatelabs.jleaser.docker.configuration.DockerConfiguration;
import com.imaginatelabs.jleaser.docker.configuration.DockerConfigurationFactory;

import com.imaginatelabs.jleaser.port.PortNumberParseException;
import com.imaginatelabs.jleaser.port.PortRangeOutOdBoundsException;
import com.imaginatelabs.jleaser.port.configuration.PortConfiguration;
import com.imaginatelabs.jleaser.port.configuration.PortConfigurationFactory;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JLeaserConfigurationFactory {




    public static JLeaserConfiguration getConfigurationFromXml(String filePath) throws JLeaserException {
        Logger log = LoggerFactory.getLogger(JLeaserConfigurationFactory.class);

        try {
            XMLConfiguration xml = new XMLConfiguration(filePath);
            log.debug("Loading Configuration from {}.",xml.getFile().getAbsolutePath());
            return new JLeaserConfiguration(
                    PortConfigurationFactory.getConfigurationFromXml(xml),
                    DockerConfigurationFactory.getConfigurationFromXml(xml));
        } catch (ConfigurationException e) {
            log.warn("No Configuration file detected, using default configuration.");
        }
        return getConfigurationFromDefaults();
    }


    public static JLeaserConfiguration getConfigurationFromDefaults() throws PortNumberParseException, PortRangeOutOdBoundsException {
        return new JLeaserConfiguration(
                PortConfigurationFactory.getConfigurationFromDefaults(),
                DockerConfigurationFactory.getConfigurationFromDefaults());

    }
}
