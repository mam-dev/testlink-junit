package net.oneandone.testlinkjunit.tljunit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.Test;

/**
 * Annotation for marking {@link Test}s the results of which should be reported in testlink xml file.
 * 
 * <ul>
 * <li>
 * Anotate your tests with {@link TestLink} and provide either an {@link TestLink#externalId()} or
 * {@link TestLink#internalId()}, one of which is required.</li>
 * <li>
 * Tests annotated with {@link org.junit.Ignore} as well as tests with failing assumptions will be reported as
 * <tt>BLOCKED</tt>.</li>
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
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface TestLink {
    /** Default value for missing externalIds. */
    static String NOT_AVAILABLE = "NOT_AVAILABLE";

    /**
     * @return internal_id of a Test to be reported.
     */
    long internalId() default 0;

    /**
     * @return external_id of a Test to be reported.
     */
    String externalId() default NOT_AVAILABLE;
}
