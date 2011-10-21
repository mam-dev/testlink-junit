/**
 * Copyright 2011 Mirko Friedenhagen 
 */

package net.oneandone.testlinkjunit.tljunit;

import org.junit.runner.Description;

/**
 * Holder object for the id in the {@link TestLink} annotation.
 */
public abstract class TestLinkId<E> {

    /** id of the test. */
    private final E id;

    /**
     * @param id
     *            of the test.
     */
    TestLinkId(final E id) {
        this.id = id;
    }

    /**
     * @return the id.
     */
    public E getId() {
        return id;
    }

    /**
     * Returns a String representation of the current TestLink ID.
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
     * Returns the ID of the current Testcase as String.
     * 
     * @param description
     *            to extract the ID from.
     * @return the ID of the current Testcase as String.
     * @throws IllegalArgumentException
     *             when neither the {@link TestLink#externalId()} nor the {@link TestLink#internalId()} is set
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
            return "internal_id";
        }
    }
}
