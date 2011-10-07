package com.github.oneandone.testlinkjunit.tljunit;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface TestLink {
    final static String NOT_AVAILABLE = "NOT_AVAILABLE";
    long internalId() default 0;
    String externalId() default NOT_AVAILABLE;
}
