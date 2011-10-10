package net.oneandone.testlinkjunit.tljunit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.Test;

/**
 * Annotation for marking {@link Test}s the results of which should be reported in testlink xml file.
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
