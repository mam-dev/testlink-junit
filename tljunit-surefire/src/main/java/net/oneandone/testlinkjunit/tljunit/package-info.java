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
/**
 * <p>
 * By annotating your tests with {@link net.oneandone.testlinkjunit.tljunit.TestLink} 
 * test results will be written to a <tt>XML</tt> file which may be imported to a running  
 * <a href="http://www.teamst.org/">Testlink</a> instance as described in the 
 * <a href="http://www.teamst.org/_tldoc/1.8/user_manual.pdf">user manual</a>. 
 * </p> 
 * <h2>Usage</h2> 
 * <h3>Annotating your tests</h3>
 * <p>
 * You have to annotate your tests with {@link net.oneandone.testlinkjunit.tljunit.TestLink} and
 * provide either an <tt>externalId</tt> or <tt>internalId</tt>.
 * Tests annotated with {@link org.junit.Ignore} will be marked as <tt>BLOCKED</tt> as well as
 * tests with failing assumptions.
 * </p>
 * <h3>Running tests with the maven-surefire-plugin</h3>
 * <p>
 * You have to configure the surefire plugin to use the additional
 * {@link net.oneandone.testlinkjunit.tljunit.TestLinkXmlRunListener} or 
 * {@link net.oneandone.testlinkjunit.tljunit.TestLinkLoggingRunListener}.
 * See documentation in these classes.</p>
 *
 * <h2>Running tests in Eclipse</h2>
 * <p>
 * To run a test from Eclipse, add a main method which will collect the tests:
 * </p>
 * <pre>
 *    public static void main(String[] args) throws FileNotFoundException {
 *        final JUnitCore core = new JUnitCore();
 *        core.addListener(new TestLinkXmlRunListener());
 *        core.run(EclipseTest.class);
 *    }
 * </pre>
 *
 * @author Mirko Friedenhagen
 */
package net.oneandone.testlinkjunit.tljunit;

