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

/**
 * Holder object for the id in the {@link TestLink} annotation. By defining only a private constructor in this abstract
 * class and having both extending classes as inner classes, we assure nobody is able to create other extending classes.
 * 
 * @param <T>
 *            of the id, either {@link Long} for internal or {@link String} for external IDs.
 *
 * @author Mirko Friedenhagen
 */
abstract class TestLinkId<T> {

    /** id of the test. */
    private final T id;

    /**
     * Private to make sure we only have {@link ExternalTestLinkId} and {@link InternalTestLinkId} as subclasses.
     *
     * @param id
     *            of the test.
     */
    private TestLinkId(final T id) {
        this.id = id;
    }

    /**
     * @return the id.
     */
    public T getId() {
        return id;
    }

    /**
     * Returns a String representation of the type of the current TestLink ID.
     *
     * @return type of the ID.
     */
    public abstract String getType();

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return String.format("%s='%s'", getType(), getId());
    }

    /**
     * Returns the ID of the current Testcase.
     * 
     * @param description
     *            to extract the ID from.
     * @return the ID of the current Testcase
     * @throws IllegalArgumentException
     *             when neither the {@link TestLink#externalId()} nor the {@link TestLink#internalId()} is set.
     */
    public static TestLinkId<?> fromDescription(Description description) {
        final TestLink testLink = description.getAnnotation(TestLink.class);
        final String externalId = testLink.externalId();
        final long internalId = testLink.internalId();
        if (!externalId.equals(TestLink.NOT_AVAILABLE)) {
            return new ExternalTestLinkId(externalId);
        } else if (internalId != 0) {
            return new InternalTestLinkId(internalId);
        } else {
            throw new IllegalArgumentException("Must set either internalId or externalId on '"
                    + description.getDisplayName() + "'");
        }
    }

    /**
     * An external Testlink ID.
     */
    static class ExternalTestLinkId extends TestLinkId<String> {

        /**
         * @param id
         *            of the testcase
         */
        ExternalTestLinkId(String id) {
            super(id);
        }

        /** {@inheritDoc} */
        @Override
        public String getType() {
            return "external_id";
        }
    }

    /**
     * An internal Testlink ID.
     */
    static class InternalTestLinkId extends TestLinkId<Long> {

        /**
         * @param id
         *            of the testcase
         */
        InternalTestLinkId(final Long id) {
            super(id);
        }

        /** {@inheritDoc} */
        @Override
        public String getType() {
            return "id";
        }
    }
}
