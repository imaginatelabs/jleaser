package com.imaginatelabs.jleaser.core;

public class JleaserConfigurationException extends JLeaserException{
    public JleaserConfigurationException(String message, Object... args) {
        super(message, args);
    }

    public JleaserConfigurationException(Exception e) {
        super(e);
    }
}
