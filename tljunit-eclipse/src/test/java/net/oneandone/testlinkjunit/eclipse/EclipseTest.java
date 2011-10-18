/**
 * Copyright 2011 Mirko Friedenhagen 
 */

package net.oneandone.testlinkjunit.eclipse;

import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;

import net.oneandone.testlinkjunit.tljunit.TestLink;
import net.oneandone.testlinkjunit.tljunit.TestLinkXmlRunListener;

import org.junit.Test;
import org.junit.runner.JUnitCore;

public class EclipseTest {

    @Test
    @TestLink(externalId="ECLIPSE_TEST")
    public void test() {
        assertTrue(true);
    }

    public static void main(String[] args) throws FileNotFoundException {
        final JUnitCore core = new JUnitCore();
        core.addListener(new TestLinkXmlRunListener());
        core.run(EclipseTest.class);
    }
}
