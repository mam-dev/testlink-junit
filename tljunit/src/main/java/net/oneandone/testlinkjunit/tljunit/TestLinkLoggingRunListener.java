/**
 * Copyright 2011 Mirko Friedenhagen 
 */

package net.oneandone.testlinkjunit.tljunit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class logs the start and end of executed tests to the injected {@link Logger}. The name of the {@link Logger}
 * may be set by means of the system property <tt>testlink.loggername</tt>, which defaults to "TESTLINK". For more usage
 * information, see {@link TestLinkXmlRunListener}.
 */
public class TestLinkLoggingRunListener extends AbstractTestLinkRunListener<InTestLinkLogStrategy> {

    /**
     * By default log to "TESTLINK"
     */
    public TestLinkLoggingRunListener() {
        this(LoggerFactory.getLogger(System.getProperty("testlink.loggername", "TESTLINK")));
    }

    /**
     * Logs to the injected {@link Logger}.
     * 
     * @param log
     *            to log to.
     */
    public TestLinkLoggingRunListener(final Logger log) {
        super(new InTestLinkLogStrategy(log));
    }

}
