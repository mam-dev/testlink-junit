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
 * {@link org.junit.Ignore} will be marked as blocked.
 *
 * @author Mirko Friedenhagen
 */
enum TestState {
    /** Test passed. */
    p("PASSED"),

    /** Test blocked as test is ignored. */
    b("BLOCKED"),

    /** Test failed. */
    f("FAILED");

    /** description shown in XML output. */
    private final String description;

    /**
     * Constructor.
     *
     * @param description shown in XML output.
     */
    TestState(final String description) {
        this.description = description;
    }

    /**
     * @return the description
     */
    String getDescription() {
        return description;
    }
}