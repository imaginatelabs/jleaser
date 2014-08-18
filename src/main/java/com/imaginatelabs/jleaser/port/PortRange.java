package com.imaginatelabs.jleaser.port;

public class PortRange {
    private final int floor;
    private final int ceiling;

    public PortRange(int floor, int ceiling) {
        this.floor = floor;
        this.ceiling = ceiling;
    }


    public int getFloor() {
        return floor;
    }

    public int getCeiling() {
        return ceiling;
    }
}
