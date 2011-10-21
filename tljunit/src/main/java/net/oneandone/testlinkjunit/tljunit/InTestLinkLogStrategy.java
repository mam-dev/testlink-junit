/**
 * Copyright 2011 Mirko Friedenhagen 
 */

package net.oneandone.testlinkjunit.tljunit;

import java.net.URI;

import org.junit.Ignore;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.slf4j.Logger;

/**
 * {@link TestLinkStrategy} which logs events to the injected logger.
 */
final class InTestLinkLogStrategy extends AbstractInTestLinkStrategy {

    /** default when no URI is given during instantiation. */
    private static final URI NULL_URI = URI.create("");

    /** URI of the TestLink instance. */
    private final URI uri;

    /** logger to logger to. */
    private final Logger logger;

    /**
     * @param logger
     *            to logger to.
     */
    InTestLinkLogStrategy(final Logger logger) {
        this(logger, NULL_URI);
    }

    /**
     * @param logger
     *            to logger to.
     * @param testlinkUri
     *            for linking to the testcases in the testlink instance.
     */
    InTestLinkLogStrategy(final Logger logger, final URI testlinkUri) {
        this.logger = logger;
        this.uri = testlinkUri;
    }

    /** {@inheritDoc} */
    @Override
    public void setPassedWhenNoFailure(Description description) {
        if (hasPassed()) {
            logger.info("END Testcase '{}' {} PASSED", getId(description), description.getDisplayName());
        }
    }

    /** {@inheritDoc} */
    @Override
    public void setFailed(Failure failure) {
        setCurrentFailure(failure);
        final String message = failure.getMessage();
        if (message != null) {
            logger.error("END Testcase '{}' '{}' FAILED because '{}'.", new Object[] { getId(failure.getDescription()),
                    failure.getTestHeader(), message });
        } else {
            logger.error("END Testcase '{}' '{}' FAILED because '{}'.", new Object[] { getId(failure.getDescription()),
                    failure.getTestHeader(), failure.getException() });
        }
    }

    /** {@inheritDoc} */
    @Override
    public void setBlockedWhenIgnored(Description description) {
        final String message = description.getAnnotation(Ignore.class).value();
        logger.warn("END Testcase '{}' '{}' BLOCKED because '{}'.",
                new Object[] { getId(description), description.getDisplayName(), message });
    }

    /** {@inheritDoc} */
    @Override
    public void setBlockedWhenAssumptionFailed(Failure failure) {
        setCurrentFailure(failure);
        logger.warn("END Testcase '{}' '{}' BLOCKED because '{}'.", new Object[] { getId(failure.getDescription()),
                failure.getTestHeader(), failure.getMessage() });

    }

    /** {@inheritDoc} */
    @Override
    public void addNewTestCase(Description description) {
        setCurrentFailure(NO_FAILURE);
        final String id = getId(description);
        logger.info("START Testcase '{}' '{}'.", id, description.getDisplayName());
        if (uri != NULL_URI) {
            logger.info("START Testcase '{}' '{}'.", id, uri.resolve(id));
        }
    }

    /**
     * Returns the {@link TestLinkId} from the {@link TestLink} annotation of the description.
     *
     * @param description
     *            to exctract the id from.
     * @return the {@link TestLinkId} from the {@link TestLink} annotation of the description.
     */
    String getId(Description description) {
        return String.valueOf(TestLinkId.fromDescription(description));
    }
    // http://testlink.sourceforge.net/demo/lib/testcases/tcPrint.php?testcase_id=2750
}
