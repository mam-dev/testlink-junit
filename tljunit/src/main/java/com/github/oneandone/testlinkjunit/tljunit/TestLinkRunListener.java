package com.github.oneandone.testlinkjunit.tljunit;

import java.io.PrintStream;

import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.junit.Ignore;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

public class TestLinkRunListener extends RunListener {

    private final PrintStream out;

    private Failure currentFailure = null;

    private final Xpp3Dom results;

    private Xpp3Dom currentTestCase;

    public TestLinkRunListener() {
        this(System.out);
    }

    public TestLinkRunListener(final PrintStream out) {
        this.out = out;
        results = new Xpp3Dom("results");
    }

    /** {@inheritDoc} */
    @Override
    public void testStarted(Description description) throws Exception {
        super.testStarted(description);
        currentFailure = null;
        startNewTestCase(description);
    }

    /**
     * @param description
     * @return 
     */
    boolean startNewTestCase(Description description) {
        final TestLink testLink = description.getAnnotation(TestLink.class);
        if (testLink != null) {
            currentTestCase = new Xpp3Dom("testcase");
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
        if (startNewTestCase(description)) {
            addResult("b");
            final String ignoreValue = description.getAnnotation(Ignore.class).value();
            addNotes(String.format("'%s' blocked because '%s'", description.getDisplayName(), ignoreValue));
        }
    }

    /**
     * @param notesValue
     */
    void addNotes(final String notesValue) {
        final Xpp3Dom notes = new Xpp3Dom("notes");
        currentTestCase.addChild(notes);
        notes.setValue(notesValue);
    }

    /**
     * @param resultValue
     */
    void addResult(final String resultValue) {
        final Xpp3Dom result = new Xpp3Dom("result");
        currentTestCase.addChild(result);
        result.setValue(resultValue);
    }

    /** {@inheritDoc} */
    @Override
    public void testFailure(Failure failure) throws Exception {
        super.testFailure(failure);
        final TestLink testLink = failure.getDescription().getAnnotation(TestLink.class);
        currentFailure = failure;
        if (testLink != null) {
            addResult("f");
            addNotes(String.format("'%s' failed because '%s'", failure.getDescription().getDisplayName(), failure.getMessage()));
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
                addResult("p");
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
}
