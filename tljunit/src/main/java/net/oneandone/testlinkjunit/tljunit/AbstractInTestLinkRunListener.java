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
 * Provides some basic methods mainly to set and get the current failure in a thread safe fashion.
 * {@link AbstractInTestLinkRunListener#getCurrentFailure()} may be used in
 * {@link RunListener#testFinished(Description)} as there is no other way to determine whether a test
 * failed at this point.
 *
 * @author Mirko Friedenhagen
 */
public abstract class AbstractInTestLinkRunListener extends RunListener {

    /** Null-Object to check against. */
    private static final Failure NO_FAILURE = new Failure(Description.EMPTY, null);

    /** Holds the current failure. */
    private final ThreadLocal<Failure> currentFailure = new ThreadLocal<Failure>();

    /**
     * Returns the failure in the current {@link Thread}.
     *
     * @return the currentFailure
     */
    protected final Failure getCurrentFailure() {
        return currentFailure.get();
    }

    /**
     * Sets the failure in the current {@link Thread}.
     *
     * @param currentFailure
     *            the currentFailure to set
     */
    protected final void setCurrentFailure(Failure currentFailure) {
        this.currentFailure.set(currentFailure);
    }

    /**
     * Resets the current failure, eg there is none.
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
