/**
 * Copyright 1&1 Internet AG, https://github.com/1and1/
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.oneandone.testlinkjunit.tljunit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeNoException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.annotation.Annotation;
import java.net.URI;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.maven.shared.utils.xml.Xpp3Dom;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
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
        final TimeZone timeZoneUTC = TimeZone.getTimeZone("UTC");
        TimeZone.setDefault(timeZoneUTC);
        final Calendar calendar = Calendar.getInstance(timeZoneUTC, Locale.US);
        calendar.setTimeInMillis(0);
        final InTestLinkXmlRunListener inTestLinkStrategy = new InTestLinkXmlRunListener("noone");
        Xpp3Dom timeStamp = inTestLinkStrategy.createTimeStamp(calendar.getTime());
        assertEquals(XML_HEADER + "<timestamp>1970-01-01 00:00:00</timestamp>", timeStamp.toString());
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
        // Servername is &auml;&ouml;&uuml;&szlig; - this file should be UTF-8.
        assumeNoException(new IllegalStateException(
                "can not proceed because server 'äöüß' not available"));
    }

    @Test(expected = IOException.class)
    public void testFinalBlockOftestRunFinished() throws Exception {
        OutputStream stream = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                throw new IOException("Oops");
            }
        };
        new TestLinkXmlRunListener(stream, "mirko").testRunFinished(new Result());
    }
}
