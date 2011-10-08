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
    interface Strategy {

        /**
         * @param description
         */
        void addNewTestCase(Description description);

        /**
         * @param description
         */
        void addIgnore(Description description);

        /**
         * @param failure
         */
        void addFailure(Failure failure);

        /**
         * @param description
         */
        void addFinished(Description description);

    }

    static class InTestLinkStrategy implements Strategy {

        private final Xpp3Dom results;

        private final String userName;

        private Xpp3Dom currentTestCase = null;

        private Failure currentFailure = null;

        /**
         * 
         */
        public InTestLinkStrategy() {
            results = new Xpp3Dom("results");
            userName = System.getProperty("testlink.userName", System.getProperty("user.name", "UNKNOWN"));
        }

        /** {@inheritDoc} */
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

        /** {@inheritDoc} */
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
                currentTestCase.addChild(createNotes(String.format("'%s' FAILED because '%s'.", failure.getTestHeader(),
                        message)));
            } else {
                currentTestCase.addChild(createNotes(String.format("'%s' FAILED because '%s'.", failure.getTestHeader(),
                        failure.getTrace())));
            }
        }

        /** {@inheritDoc} */
        @Override
        public void addFinished(Description description) {
            final Ignore ignore = description.getAnnotation(Ignore.class);
            if (ignore == null && currentFailure == null) {
                currentTestCase.addChild(createResult(TestState.p));
                currentTestCase.addChild(createNotes(String.format("'%s' PASSED.",
                        description.getDisplayName())));
            }
        }

        /** {@inheritDoc} */
        public String toString() {
            return String.valueOf(results);
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
        Xpp3Dom createNotes(final String notesValue) {
            final Xpp3Dom notes = new Xpp3Dom("notes");
            notes.setValue(notesValue);
            return notes;
        }

        /**
         * @param resultValue
         * @return
         */
        Xpp3Dom createResult(final TestState resultValue) {
            final Xpp3Dom result = new Xpp3Dom("result");
            result.setValue(resultValue.toString());
            return result;
        }

        /**
         * @return the results
         */
        public Xpp3Dom getResults() {
            return results;
        }

    }

    static class NoTestLinkStrategy implements Strategy {

        /** {@inheritDoc} */
        @Override
        public void addNewTestCase(Description description) {
        }

        /** {@inheritDoc} */
        @Override
        public void addIgnore(Description description) {
        }

        /** {@inheritDoc} */
        @Override
        public void addFailure(Failure failure) {
        }

        /** {@inheritDoc} */
        @Override
        public void addFinished(Description description) {
        }
    }

    static enum TestState {
        p, // PASSED
        b, // BLOCKED
        f; // FAILED 
    }

    private final PrintStream out;

    private final InTestLinkStrategy inTestLinkstrategy;

    private final NoTestLinkStrategy noTestLinkStrategy;

    private Strategy currentStrategy = null;

    public TestLinkRunListener() {
        this(System.out);
    }

    public TestLinkRunListener(final PrintStream out) {
        this.out = out;
        inTestLinkstrategy = new InTestLinkStrategy();
        noTestLinkStrategy = new NoTestLinkStrategy();
    }

    /**
     * {@inheritDoc}
     * 
     * This method is <b>not</b> called for tests annotated with {@link Ignore}.
     */
    @Override
    public void testStarted(Description description) throws Exception {
        super.testStarted(description);
        switchStrategy(description).addNewTestCase(description);
    }

    /**
     * {@inheritDoc}
     * 
     * We have to select the correct strategy again, as {@link RunListener#testStarted(Description)} is not called for
     * tests annotated with {@link Ignore}.
     */
    @Override
    public void testIgnored(Description description) throws Exception {
        super.testIgnored(description);
        switchStrategy(description).addIgnore(description);
    }

    /** {@inheritDoc} */
    @Override
    public void testFailure(Failure failure) throws Exception {
        super.testFailure(failure);
        currentStrategy.addFailure(failure);
    }

    /** {@inheritDoc} */
    @Override
    public void testFinished(Description description) throws Exception {
        super.testFinished(description);
        currentStrategy.addFinished(description);
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
        out.print(inTestLinkstrategy.toString());
    }

    /**
     * Switches between the strategies for testcases with or without {@link TestLink} annotation and returns the
     * currentStrategy.
     * 
     * @param description
     *            of the test case.
     * @return currentStrategy
     */
    Strategy switchStrategy(Description description) {
        final TestLink testLink = description.getAnnotation(TestLink.class);
        if (testLink != null) {
            currentStrategy = inTestLinkstrategy;
        } else {
            currentStrategy = noTestLinkStrategy;
        }
        return currentStrategy;
    }

    /**
     * Returns the results of the {@link InTestLinkStrategy} for unit testing.
     * 
     * @return the results
     */
    Xpp3Dom getResults() {
        return inTestLinkstrategy.getResults();
    }
}
