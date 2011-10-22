/**
 * Copyright 2011 Mirko Friedenhagen 
 */

package net.oneandone.testlinkjunit.tljunit;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;

/**
 * Provides some basic methods mainly to set and get the current failure in a thread safe fashion.
 * {@link AbstractInTestLinkStrategy#getCurrentFailure()} may be used in
 * {@link TestLinkStrategy#setPassedWhenNoFailure(Description)} as there is no other way to determine whether a test
 * failed at this point.
 */
public abstract class AbstractInTestLinkStrategy implements TestLinkStrategy {

    /** Null-Object to check against. */
    private static final Failure NO_FAILURE = new Failure(Description.EMPTY, null);

    /** Holds the current failure. */
    private final ThreadLocal<Failure> currentFailure = new ThreadLocal<Failure>();

    /**
     * @return the currentFailure
     */
    protected final Failure getCurrentFailure() {
        return currentFailure.get();
    }

    /**
     * @param currentFailure
     *            the currentFailure to set
     */
    protected final void setCurrentFailure(Failure currentFailure) {
        this.currentFailure.set(currentFailure);
    }

    /**
     * Reset the current failure, eg there is none.
     */
    protected final void resetCurrentFailure() {
        this.currentFailure.set(NO_FAILURE);
    }
    /**
     * Has the the current test passed or failed (an assumption)?
     * 
     * @return true when the current test passed.
     */
    protected boolean hasPassed() {
        return getCurrentFailure().equals(NO_FAILURE);
    }

}
