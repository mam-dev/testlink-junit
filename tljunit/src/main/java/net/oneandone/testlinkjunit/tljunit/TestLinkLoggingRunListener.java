/*
 * Copyright 2012 1&1.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
 *
 * @author Mirko Friedenhagen
 */
public class TestLinkLoggingRunListener extends AbstractTestLinkRunListener<InTestLinkLogRunListener> {

    /**
     * By default log to "TESTLINK".
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
        super(new InTestLinkLogRunListener(logger, testLinkUri));
    }

}
