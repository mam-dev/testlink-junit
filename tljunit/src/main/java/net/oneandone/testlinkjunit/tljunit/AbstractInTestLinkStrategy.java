/**
 * Copyright 2011 Mirko Friedenhagen 
 */

package net.oneandone.testlinkjunit.tljunit;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;

/**
 * @author mirko
 * 
 */
public abstract class AbstractInTestLinkStrategy implements TestLinkStrategy {

    /** Null-Object to check against. */
    protected static final Failure NO_FAILURE = new Failure(Description.EMPTY, null);

    /** Holds the current failure. */
    private ThreadLocal<Failure> currentFailure = new ThreadLocal<Failure>();

    /**
     * @return the currentFailure
     */
    protected Failure getCurrentFailure() {
        return currentFailure.get();
    }

    /**
     * @param currentFailure
     *            the currentFailure to set
     */
    protected void setCurrentFailure(Failure currentFailure) {
        this.currentFailure.set(currentFailure);
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
