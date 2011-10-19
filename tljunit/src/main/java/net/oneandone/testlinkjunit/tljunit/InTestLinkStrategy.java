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

    private final String testerName;

    private ThreadLocal<Xpp3Dom> currentTestCase = new ThreadLocal<Xpp3Dom>();

    private ThreadLocal<Failure> currentFailure = new ThreadLocal<Failure>();

    /**
     * 
     */
    public InTestLinkStrategy(final String testerName) {
        results = new Xpp3Dom("results");
        this.testerName = testerName;
    }

    /** {@inheritDoc}
     *
     * {@link TestLink} annotation must have either {@link TestLink#internalId()} or {@link TestLink#externalId()} set.
     */
    @Override
    public void addNewTestCase(Description description) {
        setCurrentFailure(null);
        final TestLink testLink = description.getAnnotation(TestLink.class);
        final Xpp3Dom testCase = new Xpp3Dom("testcase");
        setCurrentTestCase(testCase);
        testCase.addChild(createTester(testerName));
        testCase.addChild(createTimeStamp(new Date()));
        synchronized (results) {
            results.addChild(testCase);
        }
        final String externalId = testLink.externalId();
        final long internalId = testLink.internalId();
        if (!externalId.equals(TestLink.NOT_AVAILABLE)) {
            testCase.setAttribute("external_id", externalId);
        } else if (internalId != 0) {
            testCase.setAttribute("internal_id", String.valueOf(internalId));
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
        final Xpp3Dom testCase = getCurrentTestCase();
        testCase.addChild(createResult(TestState.b));
        final String ignoreValue = description.getAnnotation(Ignore.class).value();
        testCase.addChild(createNotes(String.format("'%s' BLOCKED because '%s'.",
                description.getDisplayName(), ignoreValue)));
    }

    /** {@inheritDoc} */
    @Override
    public void addFailureOrAssumptionFailure(Failure failure, TestState testState) {
        setCurrentFailure(failure);
        final Xpp3Dom testCase = getCurrentTestCase();
        testCase.addChild(createResult(testState));
        final String message = failure.getMessage();
        final Xpp3Dom notes;
        if (message != null) {
            notes = createNotes(String.format("'%s' " + testState.getDescription() + " because '%s'.",
                    failure.getTestHeader(), message));
        } else {
            notes = createNotes(String.format("'%s' " + testState.getDescription() + " because '%s'.",
                    failure.getTestHeader(), failure.getTrace()));
        }
        testCase.addChild(notes);
    }

    /** {@inheritDoc} */
    @Override
    public void addFinished(Description description) {
        if (getCurrentFailure() == null) {
            final Xpp3Dom testCase = getCurrentTestCase();
            testCase.addChild(createResult(TestState.p));
            testCase.addChild(createNotes(String.format("'%s' PASSED.", description.getDisplayName())));
        }
    }

    /**
     * Creates a new tester element filled with the tester.
     * 
     * @param userName
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
    Xpp3Dom getResults() {
        return results;
    }

    /**
     * @return the currentTestCase
     */
    private Xpp3Dom getCurrentTestCase() {
        return currentTestCase.get();
    }

    /**
     * @param currentTestCase the currentTestCase to set
     */
    private void setCurrentTestCase(Xpp3Dom currentTestCase) {
        this.currentTestCase.set(currentTestCase);
    }

    /**
     * @return the currentFailure
     */
    private Failure getCurrentFailure() {
        return currentFailure.get();
    }

    /**
     * @param currentFailure the currentFailure to set
     */
    private void setCurrentFailure(Failure currentFailure) {
        this.currentFailure.set(currentFailure);
    }

}