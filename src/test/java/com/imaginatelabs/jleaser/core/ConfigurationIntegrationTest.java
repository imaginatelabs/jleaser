package com.imaginatelabs.jleaser.core;

import com.imaginatelabs.jleaser.TestUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ConfigurationIntegrationTest {

    @Test(groups = {TestUtils.INTEGRATION})
    public void shouldReadValidConfigurationFromXmlFile() throws Exception {
        JLeaserConfiguration config = new JLeaserConfiguration("jleaser.config");
    }
}
