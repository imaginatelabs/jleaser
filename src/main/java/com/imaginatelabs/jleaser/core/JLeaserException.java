package com.imaginatelabs.jleaser.core;

import org.apache.commons.configuration.ConfigurationException;

public class JLeaserException extends Exception{
    public JLeaserException(String message, Object... args) {
        super(String.format(message,args));
    }

    public JLeaserException(Exception e) {
        super(e);
    }
}
