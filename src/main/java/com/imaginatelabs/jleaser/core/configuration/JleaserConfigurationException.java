package com.imaginatelabs.jleaser.core.configuration;

import com.imaginatelabs.jleaser.core.JLeaserException;

public class JleaserConfigurationException extends JLeaserException {
    public JleaserConfigurationException(String message, Object... args) {
        super(message, args);
    }

    public JleaserConfigurationException(Throwable e) {
        super(e);
    }

    public JleaserConfigurationException(Throwable cause, String message, Object... args) {
        super(cause, message, args);
    }
}

