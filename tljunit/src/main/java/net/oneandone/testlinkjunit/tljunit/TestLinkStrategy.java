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
    void setBlockedWhenIgnored(Description description);

    /**
     * Adds failure information to the current &lt;testcase&gt; which is marked as BLOCKED as an assumption failed.
     * 
     * @param failure
     *            to report.
     */
    void setBlockedWhenAssumptionFailed(Failure failure);

    /**
     * Adds failure information to the current &lt;testcase&gt;.
     * 
     * @param failure
     *            to report.
     */
    void setFailed(Failure failure);

    /**
     * Adds success information to the current &lt;testcase&gt;.
     * 
     * @param description
     *            of the testcase.
     */
    void setPassedWhenNoFailure(Description description);

}
