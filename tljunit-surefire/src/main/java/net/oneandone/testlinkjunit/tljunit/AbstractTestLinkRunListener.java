/*
 * Copyright 2012 1&1.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.oneandone.testlinkjunit.tljunit;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

/**
 * Abstract class which encapsulates the switching between Tests with and without {@link TestLink} annotations.
 *
 * Overriding of behaviour must be accomplished by injecting a specific {@link AbstractInTestLinkRunListener} as
 * all methods defined here are final. Note that {@link RunListener#testRunStarted(org.junit.runner.Description)}
 * and {@link RunListener#testRunFinished(org.junit.runner.Result)} are not <tt>final</tt> as these probably
 * must be extended for flushing and closing a stream etc.
 * 
 * @param <T>
 *            type of the listener to be called when a {@link TestLink} annotation exists.
 *
 * @author Mirko Friedenhagen
 */
public abstract class AbstractTestLinkRunListener<T extends AbstractInTestLinkRunListener> extends RunListener {

    /** Listener to be called when a {@link TestLink} annotation exists. */
    private final T inTestLinkListener;

    /**
     * Listener to be called when <em>no</em> {@link TestLink} annotation exists.
     * Note that RunListener just does nothing which is fine as executing
     * an empty method allows us to avoid cyclomatic complexity in all methods
     * but {@link AbstractTestLinkRunListener#selectListener(org.junit.runner.Description)}.
     */
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
    public final void testStarted(Description description) throws Exception {
        super.testStarted(description);
        selectListener(description).testStarted(description);
    }

    /** {@inheritDoc} */
    @Override
    public final void testIgnored(Description description) throws Exception {
        super.testIgnored(description);
        selectListener(description).testIgnored(description);
    }

    /** {@inheritDoc} */
    @Override
    public final void testFailure(Failure failure) throws Exception {
        super.testFailure(failure);
        selectListener(failure.getDescription()).testFailure(failure);
    }

    /** {@inheritDoc} */
    @Override
    public final void testAssumptionFailure(final Failure failure) {
        super.testAssumptionFailure(failure);
        selectListener(failure.getDescription()).testAssumptionFailure(failure);
    }

    /** {@inheritDoc} */
    @Override
    public final void testFinished(Description description) throws Exception {
        super.testFinished(description);
        selectListener(description).testFinished(description);
    }

    /**
     * @return the injected {@link AbstractInTestLinkRunListener}.
     */
    public final T getInTestLinkListener() {
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
