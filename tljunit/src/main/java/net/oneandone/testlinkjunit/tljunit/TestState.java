/**
 * Copyright 2011 Mirko Friedenhagen 
 */

package net.oneandone.testlinkjunit.tljunit;

import org.junit.Ignore;
import org.junit.Test;

/**
 * State of the current {@link Test}. Note that {@link Test}s annotated with {@link Ignore} will be marked as
 * blocked.
 */
enum TestState {
    p, // PASSED
    b, // BLOCKED
    f; // FAILED
}