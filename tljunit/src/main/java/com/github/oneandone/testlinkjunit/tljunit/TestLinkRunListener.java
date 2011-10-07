package com.github.oneandone.testlinkjunit.tljunit;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.junit.Ignore;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

public class TestLinkRunListener extends RunListener {

    static enum TestState {
        p,
        b,
        f;
    }

    private final PrintStream out;

    private final Xpp3Dom results;

    private final String userName;

    private Xpp3Dom currentTestCase = null;

    private Failure currentFailure = null;

    public TestLinkRunListener() {
        this(System.out);
    }

    public TestLinkRunListener(final PrintStream out) {
        this.out = out;
        results = new Xpp3Dom("results");
        userName = System.getProperty("user.name", "UNKNOWN");
    }

    /** {@inheritDoc} */
    @Override
    public void testStarted(Description description) throws Exception {
        super.testStarted(description);
        currentFailure = null;
        addNewTestCase(description);
    }

    /**
     * @param description
     * @return
     */
    boolean addNewTestCase(Description description) {
        final TestLink testLink = description.getAnnotation(TestLink.class);
        if (testLink != null) {
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
                throw new RuntimeException("Must set either internalId or externalId on "
                        + description.getDisplayName());
            }
            return true;
        } else {
            return false;
        }
    }

    /** {@inheritDoc} */
    @Override
    public void testIgnored(Description description) throws Exception {
        super.testIgnored(description);
        if (addNewTestCase(description)) {
            addResult(TestState.b);
            final String ignoreValue = description.getAnnotation(Ignore.class).value();
            addNotes(String.format("'%s' BLOCKED because '%s'", description.getDisplayName(), ignoreValue));
        }
    }

    /**
     * @param userName
     * @return 
     */
    Xpp3Dom createTester(final String userName) {
        final Xpp3Dom tester = new Xpp3Dom("tester");
        tester.setValue(userName);
        return tester;
    }

    /**
     * @param date
     * @return
     */
    Xpp3Dom createTimeStamp(final Date date) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        final Xpp3Dom timestamp = new Xpp3Dom("timestamp");
        timestamp.setValue(dateFormat.format(date));
        return timestamp;
    }

    /**
     * @param notesValue
     * @return 
     */
    Xpp3Dom addNotes(final String notesValue) {
        final Xpp3Dom notes = new Xpp3Dom("notes");
        notes.setValue(notesValue);
        currentTestCase.addChild(notes);
        return notes;
    }

    /**
     * @param resultValue
     * @return 
     */
    Xpp3Dom addResult(final TestState resultValue) {
        final Xpp3Dom result = new Xpp3Dom("result");
        result.setValue(resultValue.toString());
        currentTestCase.addChild(result);
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public void testFailure(Failure failure) throws Exception {
        super.testFailure(failure);
        final TestLink testLink = failure.getDescription().getAnnotation(TestLink.class);
        currentFailure = failure;
        if (testLink != null) {
            addResult(TestState.f);
            final String message = failure.getMessage();
            if (message != null) {
                addNotes(String.format("'%s' failed because '%s'", failure.getTestHeader(), message));
            } else {
                addNotes(String.format("'%s' failed because '%s'", failure.getTestHeader(), failure.getTrace()));
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public void testFinished(Description description) throws Exception {
        super.testFinished(description);
        final TestLink testLink = description.getAnnotation(TestLink.class);
        final Ignore ignore = description.getAnnotation(Ignore.class);
        if (testLink != null) {
            if (ignore == null && currentFailure == null) {
                addResult(TestState.p);
                addNotes(String.format("'%s' ran successfully", description.getDisplayName()));
                currentFailure = null;
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public void testRunStarted(Description description) throws Exception {
        super.testRunStarted(description);
    }

    /** {@inheritDoc} */
    @Override
    public void testRunFinished(Result result) throws Exception {
        super.testRunFinished(result);
        out.print(results.toString());
    }

    /**
     * @return the results
     */
    Xpp3Dom getResults() {
        return results;
    }
}
