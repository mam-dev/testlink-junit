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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for marking {@link org.junit.Test}s the results of which should be reported in testlink xml file.
 * 
 * <ul>
 * <li>
 * Anotate your tests with {@link TestLink} and provide either an {@link TestLink#externalId()} or
 * {@link TestLink#internalId()}, one of which is required.</li>
 * <li>
 * Tests annotated with {@link org.junit.Ignore} as well as tests with failing assumptions will be reported as
 * <tt>BLOCKED</tt>, see {@link org.junit.Assume} as well.</li>
 * </ul>
 * 
 * <pre>
 * &#064;Test
 * &#064;TestLink(internalId = 1)
 * public void testPassed() {
 *     assertTrue(true);
 * }
 * </pre>
 * 
 * <pre>
 * &#064;Test
 * &#064;TestLink(internalId = 2)
 * &#064;Ignore(&quot;Just ignore this&quot;)
 * public void testIgnored() {
 *     assertTrue(true);
 * }
 * </pre>
 * 
 * <pre>
 * &#064;Test
 * &#064;TestLink(externalId = &quot;ASSUMPTION_FAILED&quot;)
 * public void testWithFailingAssumption() {
 *     assumeTrue(false);
 * }
 * </pre>
 * 
 * <pre>
 * &#064;Test
 * &#064;TestLink(externalId = &quot;PROJECT-1&quot;)
 * public void testExternalId() {
 *     assertTrue(true);
 * }
 * </pre>
 * 
 * @author Mirko Friedenhagen
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TestLink {

    /** Default value for missing externalIds. */
    String NOT_AVAILABLE = "NOT_AVAILABLE";

    /**
     * internal_id of a Test to be reported.
     */
    long internalId() default 0;

    /**
     * external_id of a Test to be reported.
     */
    String externalId() default NOT_AVAILABLE;
}
