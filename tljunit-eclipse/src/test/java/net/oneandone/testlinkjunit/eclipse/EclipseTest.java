/**
 * Copyright 2011 Mirko Friedenhagen 
 */

package net.oneandone.testlinkjunit.eclipse;

import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;

import net.oneandone.testlinkjunit.tljunit.TestLink;
import net.oneandone.testlinkjunit.tljunit.TestLinkLoggingRunListener;
import net.oneandone.testlinkjunit.tljunit.TestLinkXmlRunListener;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.slf4j.LoggerFactory;

public class EclipseTest {

    @Test
    @TestLink(externalId="ECLIPSE_TEST")
    public void test() {
        assertTrue(true);
    }

    public static void main(String[] args) throws FileNotFoundException {
        final JUnitCore core = new JUnitCore();
        core.addListener(new TestLinkXmlRunListener());
        core.addListener(new TestLinkLoggingRunListener(LoggerFactory.getLogger("MYTESTLINK")));
        core.run(EclipseTest.class);
    }
}
