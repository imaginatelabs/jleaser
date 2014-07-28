package com.imaginatelabs.jleaser.core;

public class InvalidResourceTypeException extends JLeaserException {
    public InvalidResourceTypeException(String resourceType) {
        super(String.format("The resources type \"%s\" does not exist.",resourceType));
    }
}
