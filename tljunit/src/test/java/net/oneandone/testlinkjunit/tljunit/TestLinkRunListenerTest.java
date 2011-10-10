/**
 * Copyright 2011 Mirko Friedenhagen 
 */

package net.oneandone.testlinkjunit.tljunit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.annotation.Annotation;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import net.oneandone.testlinkjunit.tljunit.TestLink;
import net.oneandone.testlinkjunit.tljunit.TestLinkRunListener;
import net.oneandone.testlinkjunit.tljunit.TestLinkRunListener.InTestLinkStrategy;

import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.JUnitCore;


public class TestLinkRunListenerTest {

    private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + System.getProperty("line.separator");

    @Test
    public void test() {
        final JUnitCore core = new JUnitCore();
        final TestLinkRunListener listener = new TestLinkRunListener(System.out);
        core.addListener(listener);
        core.run(SUTTestLinkRunListener.class);
        final Xpp3Dom results = listener.getResults();
        assertEquals(6, results.getChildCount());
        int testCasesWithExternalId = 0;
        for (final Xpp3Dom testCase : results.getChildren()) {
            if (testCase.getAttribute("external_id") != null) {
                testCasesWithExternalId++;
            }
            hasValue(testCase, "tester");
            hasValue(testCase, "timestamp");
            hasValue(testCase, "result");
            hasValue(testCase, "notes");
        }
        assertEquals(4, testCasesWithExternalId);
    }

    void hasValue(final Xpp3Dom testCase, final String name) {
        final Xpp3Dom[] children = testCase.getChildren(name);
        assertEquals(1, children.length);
        assertFalse(children[0].getValue().isEmpty());
    }

    @Test
    @TestLink(externalId="testCreateTimeStamp")
    public void testCreateTimeStamp() {
        final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.US);
        calendar.setTimeInMillis(0);
        final InTestLinkStrategy inTestLinkStrategy = new TestLinkRunListener.InTestLinkStrategy();
        Xpp3Dom timeStamp = inTestLinkStrategy.createTimeStamp(calendar.getTime());
        assertEquals(XML_HEADER + "<timestamp>1970-01-01 01:00:00</timestamp>", timeStamp.toString());
    }

    @Test
    @TestLink(externalId="testTestLinkAnnotationWithoutId")
    public void testTestLinkAnnotationWithoutId() {
        final Description description = Description.createSuiteDescription("foo", new TestLink() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return TestLink.class;
            }

            @Override
            public long internalId() {
                return 0;
            }

            @Override
            public String externalId() {
                return TestLink.NOT_AVAILABLE;
            }
        });
        final InTestLinkStrategy inTestLinkStrategy = new TestLinkRunListener.InTestLinkStrategy();
        try {
            inTestLinkStrategy.addNewTestCase(description);
            fail("IllegalArgumentException should be thrown!");
        } catch (IllegalArgumentException e) {
            assertEquals("java.lang.IllegalArgumentException: Must set either internalId or externalId on 'foo'", e.toString());
        }

    }

    @Test
    @TestLink(internalId=1)
    @Ignore("Just ignore this")
    public void testIgnored() {
        assertTrue(true);
    }
}
