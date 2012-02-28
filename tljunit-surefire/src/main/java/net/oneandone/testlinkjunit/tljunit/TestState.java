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

/**
 * State of the current {@link org.junit.Test}. Note that {@link org.junit.Test}s annotated with
 * {@link org.junit.Ignore} or with a failing Assumption will be marked as blocked.
 *
 * @author Mirko Friedenhagen
 */
enum TestState {

    /** Test passed. */
    passed("PASSED", 'p'),

    /** Test blocked as test is ignored or Assumption failed. */
    blocked("BLOCKED", 'b'),

    /** Test failed. */
    failed("FAILED", 'f');

    /** Description shown in XML output. */
    private final String description;

    /** One letter state shown in XML output. */
    private final char state;

    /**
     * Constructor.
     *
     * @param description shown in XML output.
     * @param state       one letter state in XML output.
     */
    TestState(final String description, final char state) {
        this.description = description;
        this.state = state;
    }

    /**
     * @return the description.
     */
    String getDescription() {
        return description;
    }

    /**
     * @return the one letter state.
     */
    String getState() {
        return String.valueOf(state);
    }
}