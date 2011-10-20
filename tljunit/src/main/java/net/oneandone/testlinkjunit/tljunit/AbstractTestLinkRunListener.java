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
 * @param <E>
 */
public abstract class AbstractTestLinkRunListener<E extends AbstractInTestLinkStrategy> extends RunListener {

    /** Strategy to be called when a {@link TestLink} annotation exists. */
    private final E inTestLinkstrategy;

    /** Strategy to be called when <em>no</em> {@link TestLink} annotation exists. */
    private final TestLinkStrategy noTestLinkStrategy;

    /**
     * Injects the strategy for the actual report.
     * 
     * @param testLinkStrategy
     *            to be called when a {@link TestLink} annotation exists.
     */
    public AbstractTestLinkRunListener(final E testLinkStrategy) {
        this.inTestLinkstrategy = testLinkStrategy;
        this.noTestLinkStrategy = new NoTestLinkStrategy();
    }

    /** {@inheritDoc} */
    @Override
    public void testStarted(Description description) throws Exception {
        super.testStarted(description);
        selectStrategy(description).addNewTestCase(description);
    }

    /** {@inheritDoc} */
    @Override
    public void testIgnored(Description description) throws Exception {
        super.testIgnored(description);
        final TestLinkStrategy strategy = selectStrategy(description);
        strategy.addNewTestCase(description);
        strategy.setBlockedWhenIgnored(description);
    }

    /** {@inheritDoc} */
    @Override
    public void testFailure(Failure failure) throws Exception {
        super.testFailure(failure);
        selectStrategy(failure.getDescription()).setFailed(failure);
    }

    /** {@inheritDoc} */
    @Override
    public void testAssumptionFailure(final Failure failure) {
        super.testAssumptionFailure(failure);
        selectStrategy(failure.getDescription()).setBlockedWhenAssumptionFailed(failure);
    }

    /** {@inheritDoc} */
    @Override
    public void testFinished(Description description) throws Exception {
        super.testFinished(description);
        selectStrategy(description).setPassedWhenNoFailure(description);
    }

    /**
     * @return the injected {@link AbstractInTestLinkStrategy}.
     */
    public E getInTestLinkStrategy() {
        return inTestLinkstrategy;
    }

    /**
     * Selects strategy for test cases with or without {@link TestLink} annotation.
     * 
     * @param description
     *            of the test case.
     * @return the currently valid strategy
     */
    TestLinkStrategy selectStrategy(Description description) {
        final TestLink testLink = description.getAnnotation(TestLink.class);
        if (testLink != null) {
            return inTestLinkstrategy;
        } else {
            return noTestLinkStrategy;
        }
    }

}
