/**
 * Copyright 2011 Mirko Friedenhagen 
 */

package net.oneandone.testlinkjunit.tljunit;

import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;

/**
 * TestLinkStrategy to be used when a {@link Test} is <em>not</em> annotated with {@link TestLink}. This strategy just does
 * nothing.
 */
class NoTestLinkStrategy implements TestLinkStrategy {

    /** {@inheritDoc} */
    @Override
    public void addNewTestCase(Description description) {
    }

    /** {@inheritDoc} */
    @Override
    public void addIgnore(Description description) {
    }

    /** {@inheritDoc} */
    @Override
    public void addFailure(Failure failure) {
    }

    /** {@inheritDoc} */
    @Override
    public void addFinished(Description description) {
    }
}