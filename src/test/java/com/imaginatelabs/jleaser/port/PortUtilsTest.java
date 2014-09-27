package com.imaginatelabs.jleaser.port;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class PortUtilsTest {

    @Test
    public void shouldReturnSinglePortWhenParsingSinglePortNumber() throws Exception {
        PortRange portRange = PortUtils.parsePortString("49152");
        Assert.assertEquals(portRange.getFloor(), 49152);
        Assert.assertEquals(portRange.getCeiling(), 49152);
    }

    @Test
    public void shouldReturnPortRangeWhenParsingPortRanges() throws Exception {
        PortRange portRange = PortUtils.parsePortString("49152-49160");
        Assert.assertEquals(portRange.getFloor(), 49152);
        Assert.assertEquals(portRange.getCeiling(), 49160);
    }

    @Test
    public void shouldReturnDefaultDynamicPortRangeWhenParsingTheAnyString() throws Exception {
        PortRange portRange = PortUtils.parsePortString("*");
        Assert.assertEquals(portRange.getFloor(), 49152);
        Assert.assertEquals(portRange.getCeiling(), 65536);
    }

    @Test
    public void shouldThrowExceptionForPortRangeWithFloorGreaterThanCeiling() throws Exception {
        try {
            PortRange portRange = PortUtils.parsePortString("49160-49152");
        } catch (PortRangeOutOdBoundsException e) {
            Assert.assertEquals(e.getMessage(),
                    "Port range is invalid 49160-49152 - range floor 49160 is larger than range ceiling 49152");
        }
    }

    @Test
    public void shouldNotThrowExceptionForValidPort() throws Exception {
        PortUtils.validatePortWithinRange(49152);
    }

    @Test
    public void shouldThrowExceptionsForInvalidPorts() throws Exception {
        try {
            PortUtils.validatePortWithinRange(65537);
        } catch (PortRangeOutOdBoundsException e) {
            Assert.assertEquals(e.getMessage(), "Port 65537 does not fall between 1 and 65536");
        }

    }

    @Test
    public void shouldReturnNumericallyOrderedListOfPortsDespiteOrderOfInsertIntoParse() throws Exception {
        List<String> ports = PortUtils.parsePorts(new String[]{
                "49152-49160",
                "49151"
        });

        for (String port : ports) {
            System.out.println(port);
        }

        Assert.assertEquals(ports.get(0), "49151");
        Assert.assertEquals(ports.get(9), "49160");
    }

    @Test
    public void shouldReturnUniqueListOfPortsDespiteDuplicateEntries() throws Exception {
        List<String> ports = PortUtils.parsePorts(new String[]{
                "49152-49160",
                "49152",
                "49154"
        });

        Assert.assertEquals(ports.size(), 9);
    }
}
