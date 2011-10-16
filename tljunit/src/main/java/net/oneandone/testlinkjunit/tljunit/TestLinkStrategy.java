/**
 * Copyright 2011 Mirko Friedenhagen 
 */

package net.oneandone.testlinkjunit.tljunit;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;

/**
 * Used by {@link InTestLinkStrategy} and {@link NoTestLinkStrategy}.
 */
interface TestLinkStrategy {

    /**
     * Adds a new <em>not ignored</em> &lt;testcase&gt; element to results.
     * 
     * @param description
     *            of the testcase.
     */
    void addNewTestCase(Description description);

    /**
     * Adds a new <em>ignored</em> &lt;testcase&gt; element to results.
     * 
     * @param description
     *            of the testcase.
     */
    void addIgnore(Description description);

    /**
     * Adds failure information to the current &lt;testcase&gt;. Use {@link TestState#b}, when an assumption failed,
     * otherwise {@link TestState#f}.
     * 
     * @param failure
     *            to report.
     * @param testState
     *            one of {@link TestState#b} or {@link TestState#f}.
     */
    void addFailureOrAssumptionFailure(Failure failure, TestState testState);

    /**
     * Adds success information to the current &lt;testcase&gt;.
     * 
     * @param description
     *            of the testcase.
     */
    void addFinished(Description description);

}
