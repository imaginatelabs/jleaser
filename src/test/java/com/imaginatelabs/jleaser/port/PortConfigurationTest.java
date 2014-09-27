package com.imaginatelabs.jleaser.port;

import com.imaginatelabs.jleaser.port.configuration.PortConfiguration;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class PortConfigurationTest {

    @Test
    public void shouldHaveValidPortsThatMatchTheIncludesPortParameter() throws Exception {
        PortConfiguration portConfiguration = new PortConfiguration(
                new ArrayList<String>() {{
                    add("49152");
                    add("49153");
                    add("49154");
                }},
                new ArrayList<String>()
        );
        List<String> validPorts = portConfiguration.getValidPorts();
        Assert.assertEquals(validPorts.get(0), "49152");
        Assert.assertEquals(validPorts.get(1), "49153");
        Assert.assertEquals(validPorts.get(2), "49154");
    }

    @Test
    public void shouldHaveValidPortsExcludeValueOverWritesTheIncludesPortParameter() throws Exception {
        PortConfiguration portConfiguration = new PortConfiguration(
                new ArrayList<String>() {{
                    add("49152");
                    add("49153");
                    add("49154");
                }},
                new ArrayList<String>() {{
                    add("49153");
                }}
        );
        List<String> validPorts = portConfiguration.getValidPorts();
        Assert.assertEquals(validPorts.get(0), "49152");
        Assert.assertEquals(validPorts.get(1), "49154");
    }
}