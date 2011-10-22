/**
 * Copyright 2011 Mirko Friedenhagen 
 */

package net.oneandone.testlinkjunit.tljunit;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

/**
 * Abstract class which capsulates mostly the switching between Tests with and without {@link TestLink} annotations.
 * 
 * @param <T>
 *            type of the listener to be called when a {@link TestLink} annotation exists.
 */
public abstract class AbstractTestLinkRunListener<T extends AbstractInTestLinkRunListener> extends RunListener {

    /** Listener to be called when a {@link TestLink} annotation exists. */
    private final T inTestLinkListener;

    /** Listener to be called when <em>no</em> {@link TestLink} annotation exists. */
    private final RunListener noTestLinkListener = new RunListener();

    /**
     * Injects the listener for the actual report.
     * 
     * @param testLinkRunListener
     *            to be called when a {@link TestLink} annotation exists.
     */
    public AbstractTestLinkRunListener(final T testLinkRunListener) {
        this.inTestLinkListener = testLinkRunListener;
    }

    /** {@inheritDoc} */
    @Override
    public void testStarted(Description description) throws Exception {
        super.testStarted(description);
        selectListener(description).testStarted(description);
    }

    /** {@inheritDoc} */
    @Override
    public void testIgnored(Description description) throws Exception {
        super.testIgnored(description);
        final RunListener listener = selectListener(description);
        listener.testStarted(description);
        listener.testIgnored(description);
    }

    /** {@inheritDoc} */
    @Override
    public void testFailure(Failure failure) throws Exception {
        super.testFailure(failure);
        selectListener(failure.getDescription()).testFailure(failure);
    }

    /** {@inheritDoc} */
    @Override
    public void testAssumptionFailure(final Failure failure) {
        super.testAssumptionFailure(failure);
        selectListener(failure.getDescription()).testAssumptionFailure(failure);
    }

    /** {@inheritDoc} */
    @Override
    public void testFinished(Description description) throws Exception {
        super.testFinished(description);
        selectListener(description).testFinished(description);
    }

    /**
     * @return the injected {@link AbstractInTestLinkRunListener}.
     */
    public T getInTestLinkListener() {
        return inTestLinkListener;
    }

    /**
     * Selects strategy for test cases with or without {@link TestLink} annotation.
     * 
     * @param description
     *            of the test case.
     * @return the currently valid strategy
     */
    private RunListener selectListener(Description description) {
        final TestLink testLink = description.getAnnotation(TestLink.class);
        if (testLink != null) {
            return inTestLinkListener;
        } else {
            return noTestLinkListener;
        }
    }

}
