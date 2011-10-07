package com.github.oneandone.testlinkjunit.tljunit;

import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.JUnitCore;

/**
 * Unit test for simple App.
 */
public class SUTTestLinkRunListener {

    @Test
    @TestLink(externalId = "T1")
    public void testSuccessExternal() {
        assertTrue(true);
    }

    @Test
    @TestLink(internalId = 4)
    public void testSuccessInternal() {
        assertTrue(true);
    }

    @Test
    @TestLink(externalId = "T2")
    public void testFailed() {
        assertTrue("Failed", false);
    }

    @Test
    @TestLink(externalId = "T3")
    @Ignore("Does not run.")
    public void testIgnore() {
        assertTrue(false);
    }

    public static void main(String[] args) {
        final JUnitCore core = new JUnitCore();
        core.addListener(new TestLinkRunListener());
        core.run(SUTTestLinkRunListener.class);
    }
}
