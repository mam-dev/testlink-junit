/**
 * Copyright 2011 Mirko Friedenhagen 
 */

package net.oneandone.testlinkjunit.tljunit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeNoException;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.annotation.Annotation;
import java.net.URI;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.slf4j.LoggerFactory;


public class TestLinkRunListenerTest extends AbstractTestLinkRunListenerTest {

    private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + System.getProperty("line.separator");

    @Test
    public void test() {
        final JUnitCore core = new JUnitCore();
        final TestLinkXmlRunListener xmlListener = new TestLinkXmlRunListener(new PrintStream(new ByteArrayOutputStream()), "goofy");
        core.addListener(xmlListener);
        core.addListener(new TestLinkLoggingRunListener());
        core.addListener(new TestLinkLoggingRunListener(LoggerFactory.getLogger("MINE"), URI.create("http://testlink.sourceforge.net/demo/")));
        core.run(SUTTestLinkRunListener.class);
        final Xpp3Dom results = xmlListener.getResults();
        assertEquals(7, results.getChildCount());
        assertAllTestCasesHaveRequiredElements(results);
        assertEquals(5, countTestsWithExternalIdfinal(results));
        assertEquals(2, countIgnoredTests(results));
    }

    @Test
    @TestLink(externalId="testCreateTimeStamp")
    public void testCreateTimeStamp() {
        final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.US);
        calendar.setTimeInMillis(0);
        final InTestLinkXmlRunListener inTestLinkStrategy = new InTestLinkXmlRunListener("noone");
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
        final InTestLinkXmlRunListener inTestLinkStrategy = new InTestLinkXmlRunListener("noone");
        try {
            inTestLinkStrategy.testStarted(description);
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

    @Test
    @TestLink(externalId="ASSUMPTION_FAILED")
    public void testBlockedBecauseOfFailingAssumption() {
        assumeNoException(new IllegalStateException("can not proceed because server not available"));
    }

}
