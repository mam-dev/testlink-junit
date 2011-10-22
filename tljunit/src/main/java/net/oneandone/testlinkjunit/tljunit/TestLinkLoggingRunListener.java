/**
 * Copyright 2011 Mirko Friedenhagen 
 */

package net.oneandone.testlinkjunit.tljunit;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class logs the start and end of executed tests to the injected {@link Logger}.
 * 
 * <ul>
 * <li>The name of the {@link Logger} may be set by means of the system property <tt>testlink.loggername</tt>, which
 * defaults to "TESTLINK".</li>
 * <li>A system property called <tt>testlink.uri</tt> will be used to log a direct link to the test case in your
 * Testlink instance. When unavailable or empty will suppress the link.</li>
 * </ul>
 * For more usage information, see {@link TestLinkXmlRunListener}.
 */
public class TestLinkLoggingRunListener extends AbstractTestLinkRunListener<InTestLinkLogStrategy> {

    /**
     * By default log to "TESTLINK"
     */
    public TestLinkLoggingRunListener() {
        this(LoggerFactory.getLogger(System.getProperty("testlink.loggername", "TESTLINK")), 
             URI.create(System.getProperty("testlink.uri", "")));
    }

    /**
     * Logs to the injected {@link Logger}.
     * 
     * @param logger
     *            to log to.
     * @param testLinkUri
     *            will be used to log a direct link to the test case in your Testlink instance. An {@link URI} created
     *            from an empty string will suppress the link.
     */
    public TestLinkLoggingRunListener(final Logger logger, final URI testLinkUri) {
        super(new InTestLinkLogStrategy(logger, testLinkUri));
    }

}
