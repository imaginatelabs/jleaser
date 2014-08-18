package com.imaginatelabs.jleaser.core;

public class InvalidResourceTypeException extends JLeaserException {
    public InvalidResourceTypeException(Class<? extends Resource> aClass) {
        super(String.format("The resources type \"%s\" does not exist.",aClass));
    }

    public InvalidResourceTypeException(String resource) {
        super(String.format("The resources type \"%s\" does not exist.",resource));
    }
}
