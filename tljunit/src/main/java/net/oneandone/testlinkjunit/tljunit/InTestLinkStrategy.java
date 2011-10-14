/**
 * Copyright 2011 Mirko Friedenhagen 
 */

package net.oneandone.testlinkjunit.tljunit;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

/**
 * TestLinkStrategy to be used when a {@link Test} is annotated with {@link TestLink}.
 */
class InTestLinkStrategy implements TestLinkStrategy {

    private final Xpp3Dom results;

    private final String userName;

    private Xpp3Dom currentTestCase;

    private Failure currentFailure;

    /**
     * 
     */
    public InTestLinkStrategy() {
        results = new Xpp3Dom("results");
        userName = System.getProperty("testlink.userName", System.getProperty("user.name", "UNKNOWN"));
    }

    /** {@inheritDoc}
     *
     * {@link TestLink} annotation must have either {@link TestLink#internalId()} or {@link TestLink#externalId()} set.
     */
    @Override
    public void addNewTestCase(Description description) {
        currentFailure = null;
        final TestLink testLink = description.getAnnotation(TestLink.class);
        currentTestCase = new Xpp3Dom("testcase");
        currentTestCase.addChild(createTester(userName));
        currentTestCase.addChild(createTimeStamp(new Date()));
        results.addChild(currentTestCase);
        final String externalId = testLink.externalId();
        final long internalId = testLink.internalId();
        if (!externalId.equals(TestLink.NOT_AVAILABLE)) {
            currentTestCase.setAttribute("external_id", externalId);
        } else if (internalId != 0) {
            currentTestCase.setAttribute("internal_id", String.valueOf(internalId));
        } else {
            throw new IllegalArgumentException("Must set either internalId or externalId on '"
                    + description.getDisplayName() + "'");
        }
    }

    /**
     * {@inheritDoc}
     *
     * We have to call {@link InTestLinkStrategy#addNewTestCase(Description)}, as
     * {@link RunListener#testStarted(Description)} is not called for tests annotated with {@link Ignore}.
     */
    @Override
    public void addIgnore(Description description) {
        addNewTestCase(description);
        currentTestCase.addChild(createResult(TestState.b));
        final String ignoreValue = description.getAnnotation(Ignore.class).value();
        currentTestCase.addChild(createNotes(String.format("'%s' BLOCKED because '%s'.",
                description.getDisplayName(), ignoreValue)));
    }

    /** {@inheritDoc} */
    @Override
    public void addFailure(Failure failure) {
        currentFailure = failure;
        currentTestCase.addChild(createResult(TestState.f));
        final String message = failure.getMessage();
        if (message != null) {
            currentTestCase.addChild(createNotes(String.format("'%s' FAILED because '%s'.",
                    failure.getTestHeader(), message)));
        } else {
            currentTestCase.addChild(createNotes(String.format("'%s' FAILED because '%s'.",
                    failure.getTestHeader(), failure.getTrace())));
        }
    }

    /** {@inheritDoc} */
    @Override
    public void addFinished(Description description) {
        if (currentFailure == null) {
            currentTestCase.addChild(createResult(TestState.p));
            currentTestCase.addChild(createNotes(String.format("'%s' PASSED.", description.getDisplayName())));
        }
    }

    /** {@inheritDoc} */
    public String toString() {
        return String.valueOf(results);
    }

    /**
     * Creates a new tester element filled with the tester.
     * 
     * @param tester
     *            name of the user got from system property {@code testlink.userName} or {@code user.name}.
     * @return &lt;tester&gt; element.
     */
    Xpp3Dom createTester(final String userName) {
        final Xpp3Dom tester = new Xpp3Dom("tester");
        tester.setValue(userName);
        return tester;
    }

    /**
     * Creates a new timestamp element.
     * 
     * @param date
     *            of the test run.
     * @return &lt;timestamp&gt; element.
     */
    Xpp3Dom createTimeStamp(final Date date) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        final Xpp3Dom timestamp = new Xpp3Dom("timestamp");
        timestamp.setValue(dateFormat.format(date));
        return timestamp;
    }

    /**
     * Creates a new notes element.
     * 
     * @param notesValue
     *            additional notes.
     * @return &lt;notes&gt; element.
     */
    Xpp3Dom createNotes(final String notesValue) {
        final Xpp3Dom notes = new Xpp3Dom("notes");
        notes.setValue(notesValue);
        return notes;
    }

    /**
     * Creates a new result element.
     * 
     * @param testState
     *            to report
     * @return &lt;result&gt; element.
     */
    Xpp3Dom createResult(final TestState testState) {
        final Xpp3Dom result = new Xpp3Dom("result");
        result.setValue(testState.toString());
        return result;
    }

    /**
     * @return the results
     */
    public Xpp3Dom getResults() {
        return results;
    }

}