/**
 * Copyright 2011 Mirko Friedenhagen 
 */

package com.github.oneandone.testlinkjunit.tljunit;

import org.junit.Test;
import org.junit.runner.JUnitCore;

public class TestLinkRunListenerTest {

    @Test
    public void test() {
        final JUnitCore core = new JUnitCore();
        core.addListener(new TestLinkRunListener());
        core.run(SUTTestLinkRunListener.class);
    }

}
