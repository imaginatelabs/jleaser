package com.imaginatelabs.jleaser.core;

import com.imaginatelabs.jleaser.TestUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class ConfigurationIntegrationTest {

    @Test(groups = {TestUtils.INTEGRATION})
    public void shouldReadValidConfigurationFromXmlFileForPortsIncludedAndExcluded() throws Exception {
        JLeaserConfiguration config = new JLeaserConfiguration("config/jleaser.config");

        // Test this portion of the config file
        //<ports>
        //    <favour>included</favour>
        //      <included>
        //          2220,
        //          2222,
        //          1000-1020,
        //          2000-2020
        //      </included>
        //      <excluded>
        //          2221,
        //          2010-2030,
        //          3000-3020
        //      </excluded>
        //</ports>

        List<String> excludedPorts = config.getExcludedPorts();
        List<String> includedPorts = config.getIncludedPorts();

        Assert.assertTrue(includedPorts.contains("2220"));
        Assert.assertTrue(includedPorts.contains("2222"));

        Assert.assertTrue(includedPorts.contains("1000"));
        Assert.assertTrue(includedPorts.contains("1001"));
        Assert.assertTrue(includedPorts.contains("1002"));
        Assert.assertTrue(includedPorts.contains("1003"));
        Assert.assertTrue(includedPorts.contains("1004"));
        Assert.assertTrue(includedPorts.contains("1005"));
        Assert.assertTrue(includedPorts.contains("1006"));
        Assert.assertTrue(includedPorts.contains("1007"));
        Assert.assertTrue(includedPorts.contains("1008"));
        Assert.assertTrue(includedPorts.contains("1009"));
        Assert.assertTrue(includedPorts.contains("1010"));
        Assert.assertTrue(includedPorts.contains("1011"));
        Assert.assertTrue(includedPorts.contains("1012"));
        Assert.assertTrue(includedPorts.contains("1013"));
        Assert.assertTrue(includedPorts.contains("1014"));
        Assert.assertTrue(includedPorts.contains("1015"));
        Assert.assertTrue(includedPorts.contains("1016"));
        Assert.assertTrue(includedPorts.contains("1017"));
        Assert.assertTrue(includedPorts.contains("1018"));
        Assert.assertTrue(includedPorts.contains("1019"));
        Assert.assertTrue(includedPorts.contains("1020"));

        Assert.assertTrue(includedPorts.contains("2000"));
        Assert.assertTrue(includedPorts.contains("2001"));
        Assert.assertTrue(includedPorts.contains("2002"));
        Assert.assertTrue(includedPorts.contains("2003"));
        Assert.assertTrue(includedPorts.contains("2004"));
        Assert.assertTrue(includedPorts.contains("2005"));
        Assert.assertTrue(includedPorts.contains("2006"));
        Assert.assertTrue(includedPorts.contains("2007"));
        Assert.assertTrue(includedPorts.contains("2008"));
        Assert.assertTrue(includedPorts.contains("2009"));
        Assert.assertTrue(includedPorts.contains("2010"));
        Assert.assertTrue(includedPorts.contains("2011"));
        Assert.assertTrue(includedPorts.contains("2012"));
        Assert.assertTrue(includedPorts.contains("2013"));
        Assert.assertTrue(includedPorts.contains("2014"));
        Assert.assertTrue(includedPorts.contains("2015"));
        Assert.assertTrue(includedPorts.contains("2016"));
        Assert.assertTrue(includedPorts.contains("2017"));
        Assert.assertTrue(includedPorts.contains("2018"));
        Assert.assertTrue(includedPorts.contains("2019"));
        Assert.assertTrue(includedPorts.contains("2020"));

        Assert.assertTrue(excludedPorts.contains("2221"));

        //This is tests the intersection of included and excluded favours the included.
        Assert.assertFalse(excludedPorts.contains("2010"));
        Assert.assertFalse(excludedPorts.contains("2011"));
        Assert.assertFalse(excludedPorts.contains("2012"));
        Assert.assertFalse(excludedPorts.contains("2013"));
        Assert.assertFalse(excludedPorts.contains("2014"));
        Assert.assertFalse(excludedPorts.contains("2015"));
        Assert.assertFalse(excludedPorts.contains("2016"));
        Assert.assertFalse(excludedPorts.contains("2017"));
        Assert.assertFalse(excludedPorts.contains("2018"));
        Assert.assertFalse(excludedPorts.contains("2019"));
        Assert.assertFalse(excludedPorts.contains("2020"));

        Assert.assertTrue(excludedPorts.contains("2021"));
        Assert.assertTrue(excludedPorts.contains("2022"));
        Assert.assertTrue(excludedPorts.contains("2023"));
        Assert.assertTrue(excludedPorts.contains("2024"));
        Assert.assertTrue(excludedPorts.contains("2025"));
        Assert.assertTrue(excludedPorts.contains("2026"));
        Assert.assertTrue(excludedPorts.contains("2027"));
        Assert.assertTrue(excludedPorts.contains("2028"));
        Assert.assertTrue(excludedPorts.contains("2029"));
        Assert.assertTrue(excludedPorts.contains("2030"));

        Assert.assertTrue(excludedPorts.contains("3000"));
        Assert.assertTrue(excludedPorts.contains("3001"));
        Assert.assertTrue(excludedPorts.contains("3002"));
        Assert.assertTrue(excludedPorts.contains("3003"));
        Assert.assertTrue(excludedPorts.contains("3004"));
        Assert.assertTrue(excludedPorts.contains("3005"));
        Assert.assertTrue(excludedPorts.contains("3006"));
        Assert.assertTrue(excludedPorts.contains("3007"));
        Assert.assertTrue(excludedPorts.contains("3008"));
        Assert.assertTrue(excludedPorts.contains("3009"));
        Assert.assertTrue(excludedPorts.contains("3010"));
        Assert.assertTrue(excludedPorts.contains("3011"));
        Assert.assertTrue(excludedPorts.contains("3012"));
        Assert.assertTrue(excludedPorts.contains("3013"));
        Assert.assertTrue(excludedPorts.contains("3014"));
        Assert.assertTrue(excludedPorts.contains("3015"));
        Assert.assertTrue(excludedPorts.contains("3016"));
        Assert.assertTrue(excludedPorts.contains("3017"));
        Assert.assertTrue(excludedPorts.contains("3018"));
        Assert.assertTrue(excludedPorts.contains("3019"));
        Assert.assertTrue(excludedPorts.contains("3020"));
    }

    @Test(groups = {TestUtils.INTEGRATION})
    public void shouldUseDefaultConfigValuesIfConfigFileIsAbsent() throws Exception {
        try {
            new JLeaserConfiguration("foo.config");
        }catch (Exception e){
            Assert.fail();
        }
    }
}
